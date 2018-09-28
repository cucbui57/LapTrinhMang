package Control.Devices;

public interface Connector {
    public boolean connect();

    public boolean connect(int device_index);

    public void disconnect();

}
