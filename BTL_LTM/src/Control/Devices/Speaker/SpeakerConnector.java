package Control.Devices.Speaker;

import Control.Devices.Connector;
import javafx.util.Pair;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SpeakerConnector implements Connector {
    private int speaker_num; // Số lượng thiết bị speaker
    private ArrayList<Pair<String, AudioFormat>> speakerList; // Danh sách các thiết bị speaker (Tên, AudioFormat)
    private SourceDataLine speaker; // speaker đang được sử dụng
    private boolean is_listening; // Có đang phát trên speaker hay không (Dùng để kiếm soát luồng ghi dữ liệu ra speaker có được ghi tiếp hay dừng lại)
    private int current_speaker_index; // Đang sử dụng speaker ở vị trí nào trong danh sách (ArrayList<Pair<String, AudioFormat>> speakerList)
    private ByteArrayOutputStream byteArrayOutputStream; // Dữ liệu đàu vào cho speaker
    private int numBytesRead; // Số lượng bytes đã đọc trong 1 lần để ghi ra speaker
    private int n; // Hệ số nhân của FrameSize (số byte lấy được từ 1 frame của microphone) dùng để thiết lập CHUNK_SIZE
    private int CHUNK_SIZE; // Số lượng bytes được phép đọc trong 1 lần từ speaker
    private byte[] data; // Dữ liệu được đọc trong 1 lần đọc từ speaker
    private Thread listen_to_speaker; // Thread đọc dữ liệu từ speaker
    
    @Override
    public boolean connect() {
        return false;
    }

    @Override
    public boolean connect(int device_index) {
        return false;
    }

    @Override
    public void disconnect() {

    }
}
