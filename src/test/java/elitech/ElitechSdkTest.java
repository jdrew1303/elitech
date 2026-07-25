package elitech;

import elitech.api.ElitechDevice;
import elitech.api.MockUsbTransport;
import elitech.model.DeviceModel;
import elitech.model.Parameters;
import elitech.model.Record;
import elitech.model.USBParameters;
import elitech.protocol.ProtocolEngine;
import elitech.report.CsvExporter;
import elitech.report.ExcelExporter;
import elitech.report.ReportStats;
import elitech.report.TxtExporter;
import elitech.report.PdfExporter;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ElitechSdkTest {

    @Test
    public void testChecksum() {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        byte checksum = ProtocolEngine.calculateChecksum(data, 5);
        assertEquals(15, checksum);
    }

    @Test
    public void testGetParameterPackets() {
        List<byte[]> packets = ProtocolEngine.buildGetParameterPackets();
        assertNotNull(packets);
        assertEquals(16, packets.size());
        for (byte[] packet : packets) {
            assertEquals(64, packet.length);
            assertEquals(ProtocolEngine.HEADER_1, packet[0]);
            assertEquals(ProtocolEngine.HEADER_2, packet[1]);
        }
    }

    @Test
    public void testDeviceModels() {
        DeviceModel bleModel = DeviceModel.fromValue(13064);
        assertNotNull(bleModel);
        assertEquals("LogEt 8 BLE", bleModel.getModelDesc());

        DeviceModel tlogModel = DeviceModel.fromValue(49313);
        assertNotNull(tlogModel);
        assertEquals("Tlog B100", tlogModel.getModelDesc());
    }

    @Test
    public void testReportStats() {
        List<Record> records = new ArrayList<>();
        records.add(new Record(LocalDateTime.now(), 20.0, 50.0, true, false, false, "Normal", "None"));
        records.add(new Record(LocalDateTime.now(), 30.0, 60.0, false, true, false, "Normal", "None"));

        ReportStats stats = new ReportStats(records);
        assertEquals(2, stats.getTotalRecords());
        assertEquals(20.0, stats.getMinTemperature());
        assertEquals(30.0, stats.getMaxTemperature());
        assertEquals(25.0, stats.getAverageTemperature());
        assertEquals(50.0, stats.getMinHumidity());
        assertEquals(60.0, stats.getMaxHumidity());
        assertEquals(55.0, stats.getAverageHumidity());
        assertEquals(1, stats.getMarkCount());
        assertEquals(1, stats.getPauseCount());
        assertEquals(0, stats.getStopCount());
    }

    @Test
    public void testFluentMockDeviceWorkflow() throws IOException {
        // Instantiate ElitechDevice using direct dependency injection via Constructor
        ElitechDevice device = new ElitechDevice(new MockUsbTransport());
        assertNotNull(device);

        // Run full fluent sequence
        device.connect()
              .readParameters()
              .downloadRecords();

        Parameters params = device.getParameters();
        assertNotNull(params);
        assertEquals(13064, params.getModelValue());
        assertEquals("EF8123456789", params.getSerialNum());
        assertEquals(2, params.getDeviceStateValue());

        List<Record> records = device.getRecords();
        assertFalse(records.isEmpty());

        // Perform configuration, stop and format operations
        device.configure(params)
              .stop()
              .format()
              .disconnect();
    }

    @Test
    public void testExporters() throws IOException {
        Parameters params = new USBParameters();
        params.setSerialNum("TEST_SERIAL");
        params.setDeviceName("Logger Unit Test");
        params.setSensorTypeValue(2); // Enable temperature & humidity

        List<Record> records = new ArrayList<>();
        records.add(new Record(LocalDateTime.now().minusHours(2), 22.1, 55.4, true, false, false, "Normal", "None"));
        records.add(new Record(LocalDateTime.now().minusHours(1), 24.5, 58.2, false, false, true, "Normal", "None"));

        File tempCsv = File.createTempFile("elitech_report", ".csv");
        File tempTxt = File.createTempFile("elitech_report", ".txt");
        File tempPdf = File.createTempFile("elitech_report", ".pdf");
        File tempExcel = File.createTempFile("elitech_report", ".xlsx");

        tempCsv.deleteOnExit();
        tempTxt.deleteOnExit();
        tempPdf.deleteOnExit();
        tempExcel.deleteOnExit();

        CsvExporter.exportToCsv(tempCsv.getAbsolutePath(), params, records);
        TxtExporter.exportToTxt(tempTxt.getAbsolutePath(), params, records);
        PdfExporter.exportToPdf(tempPdf.getAbsolutePath(), params, records);
        ExcelExporter.exportToExcel(tempExcel.getAbsolutePath(), params, records);

        assertTrue(tempCsv.exists() && tempCsv.length() > 0);
        assertTrue(tempTxt.exists() && tempTxt.length() > 0);
        assertTrue(tempPdf.exists() && tempPdf.length() > 0);
        assertTrue(tempExcel.exists() && tempExcel.length() > 0);
    }
}
