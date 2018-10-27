package model;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.io.Serializable;

public class MyAudioDataFrame implements Serializable {
    private static final long serialVersionUID = 70L;

    private int frame_index;
    private int from_user_id;
    private float sampleRate;
    private int sampleSizeInBits;
    private int channels;
    private boolean signed;
    private boolean bigEndian;
    private byte[] data;

    public MyAudioDataFrame(int from_user_id, int frame_index, float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian, byte[] data) {
        this.from_user_id = from_user_id;
        this.sampleRate = sampleRate;
        this.sampleSizeInBits = sampleSizeInBits;
        this.channels = channels;
        this.signed = signed;
        this.bigEndian = bigEndian;
        this.data = data;
    }

    public MyAudioDataFrame(int from_user_id, int frame_index, AudioFormat audioFormat, byte[] data) {
        this.from_user_id = from_user_id;
        this.frame_index = frame_index;
        this.sampleRate = audioFormat.getSampleRate();
        this.sampleSizeInBits = audioFormat.getSampleSizeInBits();
        this.channels = audioFormat.getChannels();
        this.signed = audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
        this.bigEndian = audioFormat.isBigEndian();
        this.data = data;
    }

    public MyAudioDataFrame(int from_user_id, int frame_index, AudioInputStream audioInputStream) {
        this.from_user_id = from_user_id;
        this.frame_index = frame_index;
        this.sampleRate = audioInputStream.getFormat().getSampleRate();
        this.sampleSizeInBits = audioInputStream.getFormat().getSampleSizeInBits();
        this.channels = audioInputStream.getFormat().getChannels();
        this.signed = audioInputStream.getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
        this.bigEndian = audioInputStream.getFormat().isBigEndian();
        try {
            audioInputStream.read(data);
        } catch (IOException e) {
            data = new byte[]{0};
//            e.printStackTrace();
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getFrame_index() {
        return frame_index;
    }

    public void setFrame_index(int frame_index) {
        this.frame_index = frame_index;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(int from_user_id) {
        this.from_user_id = from_user_id;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public void setSampleSizeInBits(int sampleSizeInBits) {
        this.sampleSizeInBits = sampleSizeInBits;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public boolean isBigEndian() {
        return bigEndian;
    }

    public void setBigEndian(boolean bigEndian) {
        this.bigEndian = bigEndian;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
