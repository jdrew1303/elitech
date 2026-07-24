package elitech.api;

import elitech.protocol.ProtocolEngine;

import java.io.IOException;

/**
 * A mock USB HID transport simulator for cross-platform integration and robust unit testing.
 */
public class MockUsbTransport implements TransportConnection {
    private boolean connected = false;
    private byte[] lastCommand;

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
        lastCommand = data;
    }

    @Override
    public byte[] read(int length) throws IOException {
        if (!connected) {
            throw new IOException("Not connected!");
        }
        if (lastCommand == null) {
            return new byte[length];
        }

        byte[] response = new byte[length];
        // Check if command is Read Parameter (0x03)
        if (lastCommand[0] == ProtocolEngine.HEADER_1 && lastCommand[1] == ProtocolEngine.HEADER_2 && lastCommand[4] == 3) {
            // Fill some mock configuration bytes
            response[0] = ProtocolEngine.HEADER_1;
            response[1] = ProtocolEngine.HEADER_2;
            response[2] = 0;
            response[3] = 12;
            response[4] = 3;
            // Model: LogEt 8 BLE (value 13064 = 0x3308)
            response[11] = (byte) (13064 >> 8);
            response[12] = (byte) (13064 & 0xFF);
            // Serial Number
            response[13] = 'E'; response[14] = 'F'; response[15] = '8';
            response[16] = '1'; response[17] = '2'; response[18] = '3';
            response[19] = '4'; response[20] = '5'; response[21] = '6';
            response[22] = '7'; response[23] = '8'; response[24] = '9';
            // Device state: 2 (Running)
            response[37] = 2;
            // Actual records count: 5
            response[41] = 0; response[42] = 5;
        }
        // Read Records (0x01)
        else if (lastCommand[0] == ProtocolEngine.HEADER_1 && lastCommand[1] == ProtocolEngine.HEADER_2 && lastCommand[4] == 1) {
            // Fill mock record payload prefix header
            response[0] = ProtocolEngine.HEADER_1;
            response[1] = ProtocolEngine.HEADER_2;
            response[2] = 0;
            response[3] = 12;
            response[4] = 1;

            // Generate mock records of 8 bytes each
            // Record 1: Temp 22.5 C (225 = 0xE1), Year 2026 (26), Month 7 (07), Day 24, Hour 14, Min 30
            int recordOffset = 11;
            if (recordOffset + 8 <= length) {
                response[recordOffset] = 0; // Flags (Mark=0, Pause=0, Stop=0)
                response[recordOffset + 1] = (byte) (30 << 2); // seconds & fractional temp bits
                response[recordOffset + 2] = 26; // Year
                response[recordOffset + 3] = (byte) (7 >> 1); // Month bits
                response[recordOffset + 4] = (byte) (24 | ((225 & 7) << 5)); // Day & temp bits
                response[recordOffset + 5] = (byte) (225 >> 3); // Temp high bits
                response[recordOffset + 6] = 30; // Minute
                response[recordOffset + 7] = 0; // Humidity
            }
        }
        return response;
    }
}
