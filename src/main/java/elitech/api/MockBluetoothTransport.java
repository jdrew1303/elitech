package elitech.api;

import java.io.IOException;

/**
 * A mock Bluetooth/BLE transport simulator for cross-platform integration and robust unit testing.
 */
public class MockBluetoothTransport implements TransportConnection {
    private boolean connected = false;

    @Override
    public void connect() throws IOException {
        connected = true;
    }

    @Override
    public void disconnect() throws IOException {
        connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void write(byte[] data) throws IOException {
        if (!connected) {
            throw new IOException("Not connected!");
        }
    }

    @Override
    public byte[] read(int length) throws IOException {
        if (!connected) {
            throw new IOException("Not connected!");
        }
        return new byte[length];
    }
}
