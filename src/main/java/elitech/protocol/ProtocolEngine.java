package elitech.protocol;

import elitech.model.Parameters;
import elitech.model.Record;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates lower-level byte-level frames, checksum algorithms, and record parsing logic.
 */
public class ProtocolEngine {

    public static final byte HEADER_1 = 51;   // 0x33
    public static final byte HEADER_2 = (byte) 204; // 0xCC

    /**
     * Calculates the sum checksum for the first N bytes of an array.
     */
    public static byte calculateChecksum(byte[] data, int length) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += data[i];
        }
        return (byte) (sum & 0xFF);
    }

    /**
     * Builds standard parameter reading command packet.
     */
    public static List<byte[]> buildGetParameterPackets() {
        List<byte[]> packets = new ArrayList<>();
        // Total packets requested for full parameters in ElitechLog is usually 16 packets.
        for (int i = 0; i < 16; i++) {
            byte[] array = new byte[64];
            array[0] = HEADER_1;
            array[1] = HEADER_2;
            array[2] = 0;
            array[3] = 12;
            array[4] = 3;
            array[5] = 0;
            array[6] = 0;
            array[7] = 0;

            switch (i) {
                case 0:  array[8] = 0;   array[9] = 0; array[10] = 48; break;
                case 1:  array[8] = 48;  array[9] = 0; array[10] = 48; break;
                case 2:  array[8] = 96;  array[9] = 0; array[10] = 48; break;
                case 3:  array[8] = (byte) 144; array[9] = 0; array[10] = 8;  break;
                case 4:  array[8] = (byte) 152; array[9] = 0; array[10] = 52; break;
                case 5:  array[8] = (byte) 204; array[9] = 0; array[10] = 48; break;
                case 6:  array[8] = 0;   array[9] = 0; array[10] = 32; array[4] = 5; break;
                case 7:  array[8] = 112; array[9] = 0; array[10] = 16; array[4] = 5; break;
                case 8:  array[8] = (byte) 128; array[9] = 0; array[10] = 48; array[4] = 5; break;
                case 9:  array[8] = (byte) 176; array[9] = 0; array[10] = 48; array[4] = 5; break;
                case 10: array[8] = (byte) 224; array[9] = 0; array[10] = 48; array[4] = 5; break;
                case 11: array[8] = 16;  array[9] = 1; array[10] = 48; array[4] = 5; break;
                case 12: array[8] = 64;  array[9] = 1; array[10] = 48; array[4] = 5; break;
                case 13: array[8] = 32;  array[9] = 0; array[10] = 48; array[4] = 5; break;
                case 14: array[8] = 80;  array[9] = 0; array[10] = 32; array[4] = 5; break;
                default: array[8] = 0;   array[9] = 0; array[10] = 48; break;
            }

            array[12] = calculateChecksum(array, 12);
            packets.add(array);
        }
        return packets;
    }

    /**
     * Builds a request record command packet.
     */
    public static byte[] buildGetRecordPacket(int startAdd, int len, byte extendedCommand) {
        byte[] array = new byte[64];
        array[0] = HEADER_1;
        array[1] = HEADER_2;
        array[2] = 0;
        array[3] = 12;
        array[4] = 1; // Read records command
        array[5] = extendedCommand;
        array[6] = 0;
        array[7] = (byte) ((startAdd >> 8) & 0xFF);
        array[8] = (byte) (startAdd & 0xFF);
        array[9] = (byte) ((startAdd >> 16) & 0xFF);
        array[10] = (byte) (len & 0xFF);

        array[12] = calculateChecksum(array, 12);
        return array;
    }

    /**
     * Builds Format Command packet.
     */
    public static byte[] buildFormatCommand() {
        byte[] array = new byte[64];
        array[0] = HEADER_1;
        array[1] = HEADER_2;
        array[2] = 0;
        array[3] = 13;
        array[4] = (byte) 192;
        array[5] = 2; // Format subcommand
        array[6] = 0;
        array[7] = 0;
        array[8] = 0;
        array[9] = 0;
        array[10] = 1;
        array[11] = 0;

        array[12] = calculateChecksum(array, 12);
        return array;
    }

    /**
     * Builds Stop Command packet.
     */
    public static byte[] buildStopCommand() {
        byte[] array = new byte[64];
        array[0] = HEADER_1;
        array[1] = HEADER_2;
        array[2] = 0;
        array[3] = 13;
        array[4] = (byte) 192;
        array[5] = 3; // Stop subcommand
        array[6] = 0;
        array[7] = 0;
        array[8] = 0;
        array[9] = 0;
        array[10] = 1;
        array[11] = 0;

        array[12] = calculateChecksum(array, 12);
        return array;
    }

    /**
     * Parses standard raw records (each record is 8 bytes).
     *
     * @param recordBytes Full payload bytes (including 11-byte header prefix in response).
     * @param len Number of records.
     * @param initialTime Start base time.
     * @param params Device configuration parameters.
     * @return List of parsed Record objects.
     */
    public static List<Record> parseRecords(byte[] recordBytes, int len, LocalDateTime initialTime, Parameters params) {
        List<Record> records = new ArrayList<>();
        int prefixHeaderLength = 11;
        LocalDateTime currentTime = initialTime;

        for (int i = 0; i < len; i++) {
            int offset = prefixHeaderLength + i * 8;
            if (offset + 8 > recordBytes.length) {
                break;
            }

            // Flag values
            boolean mark = (recordBytes[offset] & 1) == 1;
            boolean pause = ((recordBytes[offset] >> 1) & 1) == 1;
            boolean stop = ((recordBytes[offset] >> 2) & 1) == 1;

            // Date Time
            int second = (recordBytes[offset + 1] >> 2) & 0x3F;
            int year = 2000 + (recordBytes[offset + 2] & 0x7F);
            int month = (recordBytes[offset + 3] & 7) * 2 + ((recordBytes[offset + 2] >> 7) & 1);
            int day = (recordBytes[offset + 3] >> 3) & 0x1F;
            int hour = recordBytes[offset + 4] & 0x1F;
            int minute = recordBytes[offset + 6] & 0x3F;

            LocalDateTime parsedTime;
            try {
                parsedTime = LocalDateTime.of(year, month, day, hour, minute, second);
            } catch (Exception e) {
                parsedTime = currentTime.plusSeconds(params.getIntervalValue());
            }
            currentTime = parsedTime;

            // Parse Temperature
            double temp;
            if (params.getProtocolVersion() >= 35) {
                int tempRaw = (((recordBytes[offset + 1] >> 1) & 1) << 11)
                        + ((recordBytes[offset + 5] & 0xFF) << 3)
                        + ((recordBytes[offset + 4] & 0xFF) >> 5);
                boolean isNegative = ((recordBytes[offset] >> 3) & 1) != 0;
                temp = isNegative ? (0.0 - tempRaw / 10.0) : (tempRaw / 10.0);
            } else {
                int tempRaw = ((recordBytes[offset + 5] & 0xFF) << 3)
                        + ((recordBytes[offset + 4] & 0xFF) >> 5);
                boolean isNegative = ((recordBytes[offset] >> 3) & 1) != 0;
                temp = isNegative ? (0.0 - tempRaw / 10.0) : (tempRaw / 10.0);
            }

            // Parse Humidity
            int humRaw = ((recordBytes[offset + 7] & 0xFF) << 2)
                    + ((recordBytes[offset + 6] & 0xFF) >> 6);
            boolean humNegative = ((recordBytes[offset] >> 6) & 1) != 0;
            double hum = humNegative ? (0.0 - humRaw / 10.0) : (humRaw / 10.0);

            Record r = new Record(
                parsedTime,
                temp,
                (params.getSensorTypeValue() > 1) ? hum : null,
                mark,
                pause,
                stop,
                "Normal",
                "None"
            );
            records.add(r);
        }
        return records;
    }
}
