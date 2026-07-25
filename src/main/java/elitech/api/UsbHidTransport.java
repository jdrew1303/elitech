package elitech.api;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

import java.io.IOException;

/**
 * Real-world USB HID transport implementation using direct USB HID library wrappers.
 */
public class UsbHidTransport implements TransportConnection {
    private final int vendorId;
    private final int productId;
    private HidDevice hidDevice;
    private HidServices hidServices;

    public UsbHidTransport(int vendorId, int productId) {
        this.vendorId = vendorId;
        this.productId = productId;
    }

    @Override
    public void connect() throws IOException {
        hidServices = HidManager.getHidServices();
        hidDevice = hidServices.getHidDevice(vendorId, productId, null);
        if (hidDevice == null) {
            throw new IOException(String.format("Device not found: VID=0x%04X, PID=0x%04X", vendorId, productId));
        }
        if (!hidDevice.open()) {
            throw new IOException("Failed to open connection to USB HID device.");
        }
    }

    @Override
    public void disconnect() throws IOException {
        if (hidDevice != null) {
            hidDevice.close();
        }
        if (hidServices != null) {
            hidServices.shutdown();
        }
    }

    @Override
    public boolean isConnected() {
        return hidDevice != null && hidDevice.isOpen();
    }

    @Override
    public void write(byte[] data) throws IOException {
        if (!isConnected()) {
            throw new IOException("USB HID device is not connected.");
        }
        // Frame payload padding to 64 bytes if required
        byte[] writeBuffer = new byte[64];
        System.arraycopy(data, 0, writeBuffer, 0, Math.min(data.length, 64));

        int bytesWritten = hidDevice.write(writeBuffer, 64, (byte) 0x00);
        if (bytesWritten < 0) {
            throw new IOException("Failed to write report to USB HID device: " + hidDevice.getLastErrorMessage());
        }
    }

    @Override
    public byte[] read(int length) throws IOException {
        if (!isConnected()) {
            throw new IOException("USB HID device is not connected.");
        }
        byte[] buffer = new byte[length];
        int bytesRead = hidDevice.read(buffer, 1000); // 1-second timeout
        if (bytesRead < 0) {
            throw new IOException("Failed to read report from USB HID device: " + hidDevice.getLastErrorMessage());
        }
        return buffer;
    }
}
