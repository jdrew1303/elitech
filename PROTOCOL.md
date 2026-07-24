# Elitech Data Logger Communication Protocol Documentation

This document describes the reverse-engineered communication protocol for the Elitech Data Logger family (including Bluetooth, USB/HID, and USB/Serial models).

## 1. Physical and Transport Layers

Elitech devices communicate over three main channels:
1. **USB HID**: Devices expose a custom USB HID interface. Packets are typically sent and received in 64-byte or 63-byte raw reports.
2. **Serial (COM/RS232/TTL)**: Connected via built-in USB-to-Serial converter chips (Silicon Labs CP210x or WCH CH340). Baud rate is uniformly configured at `115200`, with `8` data bits, `1` stop bit, and `no parity`.
3. **Bluetooth BLE**: Standard GATT characteristics where written commands and notifications emulate the HID packets.

---

## 2. Frame Structure

All protocol messages adhere to a standardized binary command envelope of 64 bytes.

| Byte Index | Field Name | Description |
|---|---|---|
| `0` | `HEADER_1` | Constant value `51` (`0x33`) |
| `1` | `HEADER_2` | Constant value `204` (`0xCC`) |
| `2` | `Payload Mode` | Usually `0` |
| `3` | `Prefix Length` | Command header prefix length (typically `12` or `13`) |
| `4` | `Command Type` | `3` = Read Param, `4` = Set Param, `1` = Read Records, `192` = Format/Control |
| `5` | `Subcommand / Flag`| Subcommand flags or extended commands |
| `6` | `Reserved` | Typically `0` |
| `7 - 11` | `Arguments` | Depends on the command (e.g. read address, read length) |
| `12` | `Checksum` | 8-bit sum of the preceding command prefix bytes (bytes `0` to `11`) |
| `13 - 63` | `Padding / Payload`| Padded with `0x00` or populated with configuration parameters |

### Checksum Algorithm
```java
int sum = 0;
for (int i = 0; i < prefixLength; i++) {
    sum += packet[i];
}
byte checksum = (byte) (sum & 0xFF);
```

---

## 3. Supported Core Commands

### Read Parameters (GetParameter)
Requests the general logger status, serial number, sample count, model configurations, and thresholds.
- **Bytes**: `51, 204, 0, 12, 3, 0, 0, 0, 0, 0, 48, 0, checksum`

### Write Parameters (SetParameter)
Overwrites device configurations. Requires subsequent FormatCommand to restart logging.
- **Bytes**: Includes name, timezone, interval, alarm thresholds, and starts at byte `41` of case parameters block.

### Read Records (GetRecord)
Fetches log records from a specified address memory range.
- **Bytes**: `51, 204, 0, 12, 1, [extended_cmd], 0, [start_addr_high], [start_addr_low], [start_addr_mid], [length], 0, checksum`

### Control Commands (Format & Stop)
- **Format**: `51, 204, 0, 13, 192, 2, 0, 0, 0, 0, 1, 0, checksum`
- **Stop**: `51, 204, 0, 13, 192, 3, 0, 0, 0, 0, 1, 0, checksum`

---

## 4. Log Record Payload Structure (8 Bytes/Record)

Each downloaded record consists of `8 bytes`, unpacked as follows:

| Bit Range | Field Name | Description |
|---|---|---|
| `[0]` | Mark | Event marker flag |
| `[1]` | Pause | Pause logging flag |
| `[2]` | Stop | Stop logging flag |
| `[3]` | Temp Sign | `1` if temperature is negative, `0` if positive |
| `[6]` | Humi Sign | `1` if humidity is negative, `0` if positive |
| `[10..15]` | Seconds | Timestamp seconds divided by 4 (or shifted) |
| `[16..22]` | Year | Offset from year 2000 |
| `[23..26]` | Month | Timestamp month |
| `[27..31]` | Day | Timestamp day |
| `[32..36]` | Hour | Timestamp hour |
| `[37..47]` | Temperature | Encoded temperature value |
| `[48..53]` | Minute | Timestamp minute |
| `[54..63]` | Humidity | Encoded humidity value |
