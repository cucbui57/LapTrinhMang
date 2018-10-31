package control.devices.speakers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;

public class AudioPlayer implements Runnable { // Luồng chơi âm thanh
    private AudioInputStream audioInputStream;
    private SourceDataLine speaker; // Speaker đang được sử dụng
    private boolean is_playing;

    int cnt;
    byte[] tempBuffer;

    public AudioPlayer(AudioInputStream audioInputStream, SourceDataLine sourceDataLine) {
        this.audioInputStream = audioInputStream;
        this.speaker = sourceDataLine;
        cnt = 0;
        tempBuffer = new byte[speaker.getBufferSize()]; // Tạo bộ đệm với độ dài bằng "speaker.getBufferSize()" là độ dài bộ đệm của speaker
    }

    @Override
    public void run() {
        audioInputStream = AudioSystem.getAudioInputStream(speaker.getFormat(), this.audioInputStream); // Chuyển đổi this.audioInputStream từ đầu vào sang định dạng của speaker hiện tại

        play();
    }

    public void play() {
        try {
            is_playing = true;
            while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1 && is_playing == true) { // Ghi dữ liệu vào bộ đệm
                if (cnt > 0) {
                    speaker.write(tempBuffer, 0, cnt); // Đưa dữ liệu ra speaker để tạo âm thanh
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (speaker.isOpen()) {
            try {
                audioInputStream.close();
                stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        is_playing = false;
    }

    public void stop() {
        is_playing = false;
        speaker.drain(); // Xoá bộ đệm của speaker
        speaker.stop();
        speaker.close();
    }

    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    public void setAudioInputStream(AudioInputStream audioInputStream) {
        this.audioInputStream = audioInputStream;
    }

    public SourceDataLine getSpeaker() {
        return speaker;
    }

    public void setSpeaker(SourceDataLine speaker) {
        this.speaker = speaker;
    }

    public boolean isIs_playing() {
        return is_playing;
    }

    public void setIs_playing(boolean is_playing) {
        this.is_playing = is_playing;
    }
}