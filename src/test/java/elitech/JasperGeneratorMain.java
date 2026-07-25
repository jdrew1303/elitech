package elitech;

import elitech.model.Parameters;
import elitech.model.Record;
import elitech.model.USBParameters;
import elitech.report.JasperExporter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JasperGeneratorMain {
    public static void main(String[] args) {
        try {
            System.out.println("Generating dummy records...");
            Parameters params = new USBParameters();
            params.setSerialNum("EFI216101650");
            params.setDeviceName("RC-5+");
            params.setSensorTypeValue(1); // Enable temperature

            List<Record> records = new ArrayList<>();
            // Let's generate about 30 records to span multiple pages
            LocalDateTime baseTime = LocalDateTime.of(2022, 5, 24, 1, 9, 29);
            for (int i = 0; i < 40; i++) {
                double temp = 20.0 + Math.sin(i * 0.5) * 3.0;
                // Add an anomaly/alarm point matching the image's lowest curve
                if (i == 10) {
                    temp = 16.2;
                }
                records.add(new Record(
                        baseTime.plusMinutes(i * 2),
                        temp,
                        null,
                        false,
                        false,
                        false,
                        "Normal",
                        "None"
                ));
            }

            System.out.println("Exporting Jasper Report to elitech_jasper_report.pdf...");
            JasperExporter.exportToPdf("elitech_jasper_report.pdf", params, records);
            System.out.println("Report generated successfully!");

        } catch (Exception e) {
            System.err.println("Failed to run Jasper Generator Main:");
            e.printStackTrace();
        }
    }
}
