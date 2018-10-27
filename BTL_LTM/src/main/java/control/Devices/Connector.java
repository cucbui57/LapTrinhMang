package control.Devices;

public interface Connector {
    boolean connect();

    boolean connect(int device_index);

    void disconnect();

}
