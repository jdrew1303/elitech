package elitech;

import elitech.api.ElitechDevice;
import elitech.api.MockUsbTransport;
import elitech.model.Parameters;
import elitech.model.Record;
import elitech.report.PdfExporter;

import java.io.IOException;
import java.util.List;

public class PdfGeneratorMain {
    public static void main(String[] args) {
        try {
            System.out.println("Initializing ElitechDevice with MockUsbTransport...");
            ElitechDevice device = new ElitechDevice(new MockUsbTransport());

            System.out.println("Executing fluent calls: connect -> readParameters -> downloadRecords...");
            device.connect()
                  .readParameters()
                  .downloadRecords();

            Parameters params = device.getParameters();
            List<Record> records = device.getRecords();

            System.out.println("Exporting PDF report to elitech_report.pdf...");
            PdfExporter.exportToPdf("elitech_report.pdf", params, records);

            System.out.println("Export completed successfully!");
            device.disconnect();
        } catch (IOException e) {
            System.err.println("Failed to run PDF Generator Main:");
            e.printStackTrace();
        }
    }
}
