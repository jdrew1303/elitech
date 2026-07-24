package elitech.report;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import elitech.model.Parameters;
import elitech.model.Record;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exports Elitech data logger configuration, record list, and calculated statistics into PDF format using OpenPDF.
 */
public class PdfExporter {

    public static void exportToPdf(String filePath, Parameters params, List<Record> records) throws IOException {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ReportStats stats = new ReportStats(records);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Font configurations
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.BOLD);

            // Document Title
            Paragraph title = new Paragraph("ELITECH DATA LOGGER REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Device Configuration Section
            Paragraph secConfig = new Paragraph("Device Configuration", sectionFont);
            secConfig.setSpacingAfter(5);
            document.add(secConfig);

            PdfPTable configTable = new PdfPTable(2);
            configTable.setWidthPercentage(100);
            configTable.setSpacingAfter(15);
            configTable.addCell(new PdfPCell(new Phrase("Device Serial Number", headerFont)));
            configTable.addCell(new PdfPCell(new Phrase(params.getSerialNum(), normalFont)));
            configTable.addCell(new PdfPCell(new Phrase("Device Name", headerFont)));
            configTable.addCell(new PdfPCell(new Phrase(params.getDeviceName(), normalFont)));
            configTable.addCell(new PdfPCell(new Phrase("Log Interval", headerFont)));
            configTable.addCell(new PdfPCell(new Phrase(params.getIntervalValue() + " seconds", normalFont)));
            configTable.addCell(new PdfPCell(new Phrase("Battery Level", headerFont)));
            configTable.addCell(new PdfPCell(new Phrase(params.getBattery() + "%", normalFont)));
            document.add(configTable);

            // Statistics Section
            Paragraph secStats = new Paragraph("Summary Statistics", sectionFont);
            secStats.setSpacingAfter(5);
            document.add(secStats);

            PdfPTable statsTable = new PdfPTable(2);
            statsTable.setWidthPercentage(100);
            statsTable.setSpacingAfter(15);
            statsTable.addCell(new PdfPCell(new Phrase("Total Records", headerFont)));
            statsTable.addCell(new PdfPCell(new Phrase(String.valueOf(stats.getTotalRecords()), normalFont)));
            statsTable.addCell(new PdfPCell(new Phrase("Min Temperature", headerFont)));
            statsTable.addCell(new PdfPCell(new Phrase(Double.isNaN(stats.getMinTemperature()) ? "N/A" : String.format("%.1f C", stats.getMinTemperature()), normalFont)));
            statsTable.addCell(new PdfPCell(new Phrase("Max Temperature", headerFont)));
            statsTable.addCell(new PdfPCell(new Phrase(Double.isNaN(stats.getMaxTemperature()) ? "N/A" : String.format("%.1f C", stats.getMaxTemperature()), normalFont)));
            statsTable.addCell(new PdfPCell(new Phrase("Average Temperature", headerFont)));
            statsTable.addCell(new PdfPCell(new Phrase(Double.isNaN(stats.getAverageTemperature()) ? "N/A" : String.format("%.1f C", stats.getAverageTemperature()), normalFont)));

            if (params.getSensorTypeValue() > 1) {
                statsTable.addCell(new PdfPCell(new Phrase("Min Humidity", headerFont)));
                statsTable.addCell(new PdfPCell(new Phrase(Double.isNaN(stats.getMinHumidity()) ? "N/A" : String.format("%.1f %%", stats.getMinHumidity()), normalFont)));
                statsTable.addCell(new PdfPCell(new Phrase("Max Humidity", headerFont)));
                statsTable.addCell(new PdfPCell(new Phrase(Double.isNaN(stats.getMaxHumidity()) ? "N/A" : String.format("%.1f %%", stats.getMaxHumidity()), normalFont)));
                statsTable.addCell(new PdfPCell(new Phrase("Average Humidity", headerFont)));
                statsTable.addCell(new PdfPCell(new Phrase(Double.isNaN(stats.getAverageHumidity()) ? "N/A" : String.format("%.1f %%", stats.getAverageHumidity()), normalFont)));
            }

            statsTable.addCell(new PdfPCell(new Phrase("Marks", headerFont)));
            statsTable.addCell(new PdfPCell(new Phrase(String.valueOf(stats.getMarkCount()), normalFont)));
            document.add(statsTable);

            // Log Records Section
            Paragraph secRecords = new Paragraph("Recorded Log Data", sectionFont);
            secRecords.setSpacingAfter(5);
            document.add(secRecords);

            int columnsCount = params.getSensorTypeValue() > 1 ? 9 : 8;
            PdfPTable recordsTable = new PdfPTable(columnsCount);
            recordsTable.setWidthPercentage(100);

            recordsTable.addCell(new Phrase("#", headerFont));
            recordsTable.addCell(new Phrase("Timestamp", headerFont));
            recordsTable.addCell(new Phrase("Temp (C)", headerFont));
            if (params.getSensorTypeValue() > 1) {
                recordsTable.addCell(new Phrase("Humi (%)", headerFont));
            }
            recordsTable.addCell(new Phrase("Mark", headerFont));
            recordsTable.addCell(new Phrase("Pause", headerFont));
            recordsTable.addCell(new Phrase("Stop", headerFont));
            recordsTable.addCell(new Phrase("Light", headerFont));
            recordsTable.addCell(new Phrase("Vibr", headerFont));

            for (int i = 0; i < records.size(); i++) {
                Record r = records.get(i);
                recordsTable.addCell(new Phrase(String.valueOf(i + 1), normalFont));
                recordsTable.addCell(new Phrase(r.getTime().format(formatter), normalFont));
                recordsTable.addCell(new Phrase(String.format("%.1f", r.getTemperature()), normalFont));
                if (params.getSensorTypeValue() > 1) {
                    recordsTable.addCell(new Phrase(r.getHumidity() != null ? String.format("%.1f", r.getHumidity()) : "0.0", normalFont));
                }
                recordsTable.addCell(new Phrase(String.valueOf(r.isMark()), normalFont));
                recordsTable.addCell(new Phrase(String.valueOf(r.isPause()), normalFont));
                recordsTable.addCell(new Phrase(String.valueOf(r.isStop()), normalFont));
                recordsTable.addCell(new Phrase(r.getIllumination(), normalFont));
                recordsTable.addCell(new Phrase(r.getVibration(), normalFont));
            }

            document.add(recordsTable);
        } catch (DocumentException e) {
            throw new IOException("Failed to generate PDF: " + e.getMessage(), e);
        } finally {
            document.close();
        }
    }
}
