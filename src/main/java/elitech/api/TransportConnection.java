package elitech.api;

import java.io.IOException;

/**
 * Interface representing a physical or logical transport layer (USB, Serial, Bluetooth, etc.).
 */
public interface TransportConnection {
    void connect() throws IOException;
    void disconnect() throws IOException;
    boolean isConnected();
    void write(byte[] data) throws IOException;
    byte[] read(int length) throws IOException;
}
