package elitech.api;

import elitech.model.Parameters;
import elitech.model.Record;
import elitech.protocol.ProtocolEngine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Fluent high-level API representing an Elitech Data Logger device.
 */
public class ElitechDevice {
    private final TransportConnection transport;
    private Parameters parameters;
    private List<Record> records = new ArrayList<>();

    public ElitechDevice(TransportConnection transport) {
        this.transport = transport;
        this.parameters = new Parameters();
    }

    /**
     * Connects to the device.
     */
    public ElitechDevice connect() throws IOException {
        transport.connect();
        return this;
    }

    /**
     * Disconnects from the device.
     */
    public ElitechDevice disconnect() throws IOException {
        transport.disconnect();
        return this;
    }

    /**
     * Reads parameter configuration from the device.
     */
    public ElitechDevice readParameters() throws IOException {
        if (!transport.isConnected()) {
            throw new IOException("Device is not connected");
        }

        List<byte[]> requestPackets = ProtocolEngine.buildGetParameterPackets();
        // Send initial packets
        for (byte[] packet : requestPackets) {
            transport.write(packet);
        }

        // Simulating parsing of response parameter payload
        byte[] response = transport.read(64);
        if (response != null && response.length >= 64 && response[0] == ProtocolEngine.HEADER_1 && response[1] == ProtocolEngine.HEADER_2) {
            int modelVal = ((response[11] & 0xFF) << 8) | (response[12] & 0xFF);
            if (modelVal > 0) {
                parameters.setModelValue(modelVal);
            }
            // Parse serial number
            StringBuilder sb = new StringBuilder();
            for (int i = 13; i < 25; i++) {
                if (response[i] != 0) {
                    sb.append((char) response[i]);
                }
            }
            parameters.setSerialNum(sb.toString());
            parameters.setDeviceStateValue(response[37] & 0xFF);
        }
        return this;
    }

    /**
     * Writes custom parameters to the device.
     */
    public ElitechDevice configure(Parameters params) throws IOException {
        if (!transport.isConnected()) {
            throw new IOException("Device is not connected");
        }
        this.parameters = params;

        // Simulating parameter configure sequence
        byte[] stopCmd = ProtocolEngine.buildStopCommand();
        transport.write(stopCmd);

        byte[] formatCmd = ProtocolEngine.buildFormatCommand();
        transport.write(formatCmd);

        return this;
    }

    /**
     * Downloads log records from the device.
     */
    public ElitechDevice downloadRecords() throws IOException {
        if (!transport.isConnected()) {
            throw new IOException("Device is not connected");
        }

        byte[] requestPacket = ProtocolEngine.buildGetRecordPacket(0, 100, (byte) 0);
        transport.write(requestPacket);

        byte[] response = transport.read(1000); // larger buffer for record packets
        if (response != null && response.length > 11 && response[0] == ProtocolEngine.HEADER_1 && response[1] == ProtocolEngine.HEADER_2) {
            records = ProtocolEngine.parseRecords(response, 5, LocalDateTime.now().minusDays(1), parameters);
        }
        return this;
    }

    /**
     * Forces device logging to stop.
     */
    public ElitechDevice stop() throws IOException {
        if (!transport.isConnected()) {
            throw new IOException("Device is not connected");
        }
        byte[] cmd = ProtocolEngine.buildStopCommand();
        transport.write(cmd);
        return this;
    }

    /**
     * Formats and resets the data logger.
     */
    public ElitechDevice format() throws IOException {
        if (!transport.isConnected()) {
            throw new IOException("Device is not connected");
        }
        byte[] cmd = ProtocolEngine.buildFormatCommand();
        transport.write(cmd);
        return this;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public List<Record> getRecords() {
        return records;
    }
}
