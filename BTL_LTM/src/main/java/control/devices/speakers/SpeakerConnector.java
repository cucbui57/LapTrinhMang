package control.devices.speakers;

import control.DataConvertor;
import control.devices.Connector;
import javafx.util.Pair;
import model.MyAudioDataFrame;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class SpeakerConnector implements Connector { // Kết nối đến speaker

    private int speaker_num; // Số lượng speaker
    private ArrayList<Pair<String, AudioFormat>> speakerList; // Danh sách speaker
    //    private SourceDataLine current_speaker;
    private int current_speaker_index; // Chỉ số của speaker hiện tại
    private boolean is_opened; // Đã khởi tạo chưa
//    private DataLine.Info dataLineInfo;

    public SpeakerConnector() {
        speakerList = new ArrayList<>();
        getDevicesList();
        is_opened = false;
        current_speaker_index = -1;
    }

    private void getDevicesList() { // Lấy danh sách tất cả những speaker có thể sử dụng
        ArrayList<Mixer.Info> mixerInfo = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo())); // Lấy danh sách thông tin về Mixer
        Mixer mixer;
        ArrayList<Line.Info> sourceLineInfo;
        ArrayList<Line> lines;
        for (int i = 0; i < mixerInfo.size(); ) { // Duyệt qua từng Mixer.Info
            mixer = AudioSystem.getMixer(mixerInfo.get(i));
            sourceLineInfo = new ArrayList<Line.Info>(Arrays.asList(mixer.getSourceLineInfo()));
            lines = new ArrayList<>();
            for (int j = 0; j < sourceLineInfo.size(); j++) {
                try {
                    lines.add(AudioSystem.getLine(sourceLineInfo.get(j))); // Thêm vào danh sách nếu là SourceLine(Info)
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
            for (Line line : lines) {
                if (line instanceof DataLine) { // Nếu line là DataLine thì thêm vào danh sách những speaker có thể sử dụng
                    DataLine dataLine = (DataLine) line;
                    speakerList.add(new Pair<>(mixerInfo.get(i).getName(), dataLine.getFormat()));
                }
            }
            i++;
            sourceLineInfo.clear();
            lines.clear();
        }
        speakerList.add(new Pair<>("Default App's setting", new AudioFormat(8000.0f, 16, 1, true, true))); // Thêm một cấu hình mặc định của ứng dụng
        speaker_num = speakerList.size();
        mixerInfo.clear();
    }

    @Override
    public boolean connect() { // Kết nối đến speaker đầu tiên có thể sử dụng trong danh sách
//        if (current_speaker != null) disconnect();
        current_speaker_index = 0;
        while (current_speaker_index < speaker_num) {
//            try {
            init_connection();
            break;
//            } catch (LineUnavailableException e) {
//                e.printStackTrace();
//            }
//            current_speaker_index++;
        }
        return current_speaker_index < speaker_num;
    }

    @Override
    public boolean connect(int device_index) { // Kết nối đến speaker có chỉ số devide_index trong danh sách
        if (device_index < 0 || speaker_num <= device_index) return false;
//        if (current_speaker != null) disconnect();
        current_speaker_index = device_index;
//        try {
        init_connection();
        return true;
//        } catch (LineUnavailableException e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    private void init_connection() {
//        current_speaker = AudioSystem.getSourceDataLine(speakerList.get(current_speaker_index).getValue());
//        dataLineInfo = new DataLine.Info(SourceDataLine.class, speakerList.get(current_speaker_index).getValue());
//        current_speaker = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
//        System.out.println(current_speaker.getFormat());
//        System.out.println("Current speaker buffer size: " + current_speaker.getBufferSize());
//        current_speaker.open();
//        current_speaker.start();
        is_opened = true;
    }

    public void play(MyAudioDataFrame myAudioDataFrame) {
        play(DataConvertor.MyAudioDataFrame_to_AudioInputStream(myAudioDataFrame));
    }

    public void play(ByteArrayOutputStream byteArrayOutputStream, AudioFormat audioFormat) {
        play(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), audioFormat, byteArrayOutputStream.size());
    }

    public void play(ByteArrayInputStream byteArrayInputStream, AudioFormat audioFormat, int size) {
        play(new AudioInputStream(byteArrayInputStream, audioFormat, size / audioFormat.getFrameSize()));
    }

    public void play(AudioInputStream inputStream) {
        SourceDataLine speaker = null;
//        DataLine.Info dataLineInfo = null;
        try {
            speaker = AudioSystem.getSourceDataLine(speakerList.get(current_speaker_index).getValue()); // Tạo một speaker có line mới
//            dataLineInfo = new DataLine.Info(SourceDataLine.class, speakerList.get(current_speaker_index).getValue());
//            speaker = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speaker.open();
            speaker.start();
            Thread player = new Thread(new AudioPlayer(inputStream, speaker)); // Tạo thread chơi âm thanh mới chạy song song để tránh block running code
            player.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void disconnect() {
        is_opened = false;
//        current_speaker.drain();
//        current_speaker.close();
    }

    public int getSpeaker_num() {
        return speaker_num;
    }

    public void setSpeaker_num(int speaker_num) {
        this.speaker_num = speaker_num;
    }

    public ArrayList<Pair<String, AudioFormat>> getSpeakerList() {
        return speakerList;
    }

    public void setSpeakerList(ArrayList<Pair<String, AudioFormat>> speakerList) {
        this.speakerList = speakerList;
    }

    public int getCurrent_speaker_index() {
        return current_speaker_index;
    }

    public void setCurrent_speaker_index(int current_speaker_index) {
        this.current_speaker_index = current_speaker_index;
    }

    public boolean isIs_opened() {
        return is_opened;
    }

    public void setIs_opened(boolean is_opened) {
        this.is_opened = is_opened;
    }
}
