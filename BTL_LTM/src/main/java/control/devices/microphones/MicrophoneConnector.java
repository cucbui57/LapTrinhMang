package control.devices.microphones;

import control.devices.Connector;
import javafx.util.Pair;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MicrophoneConnector implements Connector { // Kết nối, đọc dữ liệu từ micphone

    private int microphone_num; // Số lượng thiết bị microphone
    private ArrayList<Pair<String, AudioFormat>> microphoneList; // Danh sách các thiết bị microphone (Tên, AudioFormat)
    private TargetDataLine current_microphone; // Microphone đang được sử dụng
    private boolean is_listening; // Có đang lắng nghe trên microphone hay không (Dùng để kiếm soát luồng đọc dữ liệu từ microphone có được đọc tiếp hay dừng lại)
    private int current_microphone_index; // Đang sử dụng microphone ở vị trí nào trong danh sách (ArrayList<Pair<String, AudioFormat>> microphoneList)
    private ByteArrayOutputStream byteArrayOutputStream; // Lưu trữ dữ liệu đọc được từ microphone
    private int numBytesRead; // Số lượng bytes đã đọc trong 1 lần từ microphone
    private int n; // Hệ số nhân của FrameSize (số byte lấy được từ 1 frame của microphone) dùng để thiết lập CHUNK_SIZE
    private int CHUNK_SIZE; // Số lượng bytes được phép đọc trong 1 lần từ microphone
    private byte[] data; // Dữ liệu được đọc trong 1 lần đọc từ microphone
    private Thread listen_to_microphone; // Thread đọc dữ liệu từ microphone

    public MicrophoneConnector() {
        microphoneList = new ArrayList<>();
        getDevicesList();
        is_listening = false;
        current_microphone_index = -1;
        byteArrayOutputStream = new ByteArrayOutputStream();
        n = 8;
        CHUNK_SIZE = 256;
        listen_to_microphone = new Thread(new ListenMicrophone());
    }

    private void getDevicesList() { // Lấy danh sách cách thiết bị có thể dùng
        ArrayList<Mixer.Info> mixerInfo = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo())); // Lấy danh sách thông tin Mixer(?) của máy
        Mixer mixer;
        ArrayList<Line.Info> targetLineInfo;
        ArrayList<Line> lines;
        for (int i = 0; i < mixerInfo.size(); ) { // Duyệt từng thông tin của Mixer
            mixer = AudioSystem.getMixer(mixerInfo.get(i)); // Mỗi thông tin của Mixer tương ứng với 1 Mixer
            targetLineInfo = new ArrayList<Line.Info>(Arrays.asList(mixer.getTargetLineInfo())); // Mỗi Mixer sẽ chứa danh sách thông tin của thiết bị trong Mixer đó
            lines = new ArrayList<>(); // Chứa các kết nối có thể sử dụng đến microphone
            for (int j = 0; j < targetLineInfo.size(); j++) {
                try {
                    lines.add(AudioSystem.getLine(targetLineInfo.get(j))); // Thêm các kết nối có thể sử dụng vào danh sách
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
            for (Line line : lines) {
                if (line instanceof DataLine) { // Nếu các kết nối là DataLine thì lưu vào danh sách (Còn lại không phải DataLine thì có thể là port hoặc kiểu khác không dùng đến/không dùng được)
                    DataLine dataLine = (DataLine) line;
                    microphoneList.add(new Pair<>(mixerInfo.get(i).getName(), dataLine.getFormat()));
                }
            }
            i++;
            targetLineInfo.clear();
            lines.clear();
        }
        // Thêm một thiết bị mặc định được cấu hình vào
        microphoneList.add(new Pair<>("Deafault App's setting", new AudioFormat(8000.0f, 16, 1, true, false)));
        microphone_num = microphoneList.size();
        mixerInfo.clear();
    }

    @Override
    public boolean connect() { // Kết nối đến thiết bị đầu tiên trong danh sách có thể kết nối được
        if (current_microphone != null) disconnect();
        current_microphone_index = 0; // Bắt đầu duyệt từ thiết bị đầu tiên trong danh sách
        while (current_microphone_index < microphone_num) {
            try {
                init_connection();
                break; // Nếu kết nối được thì thoát
            } catch (LineUnavailableException e) { // Không kết nối được thì tiếp tục chạy ("current_microphone_index++")
                e.printStackTrace();
            }
            current_microphone_index++;
        }
        return current_microphone_index < microphone_num; // Nếu không kết nối được thiết bị nào thì trả về false
    }

    @Override
    public boolean connect(int device_index) { // Kết nối đến thiết bị có vị trí "device_index" trong danh sách
        if (device_index < 0 || microphone_num <= device_index) return false;
        if (current_microphone != null) disconnect();
        current_microphone_index = device_index;
        try {
            init_connection();
            return true;
        } catch (LineUnavailableException e) { // Không kết nối được thì tiếp tục chạy ("current_microphone_index++")
            e.printStackTrace();
            return false;
        }
    }

    private void init_connection() throws LineUnavailableException {
        current_microphone = AudioSystem.getTargetDataLine(microphoneList.get(current_microphone_index).getValue()); // khởi tạo kết nối đến micphone
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, microphoneList.get(current_microphone_index).getValue()); // Thông tin về kết nối
        current_microphone = (TargetDataLine) AudioSystem.getLine(dataLineInfo); // Kết nối vào microphone
        System.out.println(current_microphone.getFormat());
        current_microphone.open(); // Mở microphone lên
        CHUNK_SIZE = current_microphone.getFormat().getFrameSize() * n; // Thiết lập số byte được đọc trong 1 lần (trong 1 lần gọi hàm read();)
        data = new byte[current_microphone.getBufferSize()]; // Khởi tạo mảng byte để lưu dữ liệu được đọc từ microphone trong 1 lần (trong 1 lần gọi hàm read();)
        current_microphone.start();
        is_listening = true;
    }

    public boolean resetData() {
        try {
            byteArrayOutputStream.close();
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void listen() { // Lắng nghe microphone
        listen_to_microphone.start();
    }

    public void disconnect() { // Ngắt kết nối dến microphone
        is_listening = false;
        current_microphone.close();
    }

    public boolean still_listening() {
        return listen_to_microphone.isAlive();
    }

    class ListenMicrophone implements Runnable { // Luồng đọc dữ liệu từ microphone

        @Override
        public void run() {
            while (is_listening) {
                numBytesRead = current_microphone.read(data, 0, CHUNK_SIZE); // Đọc dữ liệu có độ dài "CHUNK_SIZE" byte vào mảng byte "data"
                byteArrayOutputStream.write(data, 0, numBytesRead); // Thêm dữ liệu được đọc vào luồng dữ liệu
            }
        }
    }

    public int getMicrophone_num() {
        return microphone_num;
    }

    public void setMicrophone_num(int microphone_num) {
        this.microphone_num = microphone_num;
    }

    public ArrayList<Pair<String, AudioFormat>> getMicrophoneList() {
        return microphoneList;
    }

    public void setMicrophoneList(ArrayList<Pair<String, AudioFormat>> microphoneList) {
        this.microphoneList = microphoneList;
    }

    public TargetDataLine getCurrent_microphone() {
        return current_microphone;
    }

    public void setCurrent_microphone(TargetDataLine current_microphone) {
        this.current_microphone = current_microphone;
    }

    public boolean isIs_listening() {
        return is_listening;
    }

    public void setIs_listening(boolean is_listening) {
        this.is_listening = is_listening;
    }

    public int getCurrent_microphone_index() {
        return current_microphone_index;
    }

    public void setCurrent_microphone_index(int current_microphone_index) {
        this.current_microphone_index = current_microphone_index;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public int getNumBytesRead() {
        return numBytesRead;
    }

    public void setNumBytesRead(int numBytesRead) {
        this.numBytesRead = numBytesRead;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getCHUNK_SIZE() {
        return CHUNK_SIZE;
    }

    public void setCHUNK_SIZE(int CHUNK_SIZE) {
        this.CHUNK_SIZE = CHUNK_SIZE;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Thread getListen_to_microphone() {
        return listen_to_microphone;
    }

    public void setListen_to_microphone(Thread listen_to_microphone) {
        this.listen_to_microphone = listen_to_microphone;
    }
}
