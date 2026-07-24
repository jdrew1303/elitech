package elitech.api;

import elitech.model.DeviceModel;

/**
 * Manager to query and instantiate connected devices.
 */
public class ElitechDeviceManager {

    /**
     * Instantiates an Elitech device using a mock USB transport for testing or simulation.
     */
    public static ElitechDevice createMockUsbDevice() {
        return new ElitechDevice(new MockUsbTransport());
    }

    /**
     * Instantiates an Elitech device using a mock Serial/COM transport.
     */
    public static ElitechDevice createMockSerialDevice() {
        return new ElitechDevice(new MockSerialTransport());
    }

    /**
     * Instantiates an Elitech device using a mock Bluetooth transport.
     */
    public static ElitechDevice createMockBluetoothDevice() {
        return new ElitechDevice(new MockBluetoothTransport());
    }
}
