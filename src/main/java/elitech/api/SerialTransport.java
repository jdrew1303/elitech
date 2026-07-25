package elitech.api;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

/**
 * Real-world Serial/COM port connection using jSerialComm driver.
 */
public class SerialTransport implements TransportConnection {
    private final String portName;
    private final int baudRate;
    private SerialPort serialPort;

    public SerialTransport(String portName, int baudRate) {
        this.portName = portName;
        this.baudRate = baudRate;
    }

    @Override
    public void connect() throws IOException {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);
        serialPort.setNumDataBits(8);
        serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        serialPort.setParity(SerialPort.NO_PARITY);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        if (!serialPort.openPort()) {
            throw new IOException("Failed to open serial port: " + portName);
        }
    }

    @Override
    public void disconnect() throws IOException {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }
    }

    @Override
    public boolean isConnected() {
        return serialPort != null && serialPort.isOpen();
    }

    @Override
    public void write(byte[] data) throws IOException {
        if (!isConnected()) {
            throw new IOException("Serial port is not connected");
        }
        int bytesWritten = serialPort.writeBytes(data, data.length);
        if (bytesWritten < 0) {
            throw new IOException("Failed to write bytes to serial port");
        }
    }

    @Override
    public byte[] read(int length) throws IOException {
        if (!isConnected()) {
            throw new IOException("Serial port is not connected");
        }
        byte[] buffer = new byte[length];
        int bytesRead = serialPort.readBytes(buffer, length);
        if (bytesRead < 0) {
            throw new IOException("Failed to read bytes from serial port");
        }
        return buffer;
    }
}
