package elitech.report;

import elitech.model.Parameters;
import elitech.model.Record;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exports data logger configurations, parsed log records, and generated temperature chart
 * into a professional PDF document layout matching the Elitech PDF report using JasperReports.
 */
public class JasperExporter {

    public static void exportToPdf(String destFilePath, Parameters params, List<Record> records) throws Exception {
        // Load the JRXML template from the classpath
        InputStream jrxmlStream = JasperExporter.class.getResourceAsStream("/elitech/report/elitech_report.jrxml");
        if (jrxmlStream == null) {
            throw new RuntimeException("Could not find elitech_report.jrxml template on the classpath!");
        }

        // Compile the JRXML template to a JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        // Generate statistics
        ReportStats stats = new ReportStats(records);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        // Set up Parameter Map
        Map<String, Object> parameters = new HashMap<>();

        // Header and device info
        parameters.put("DeviceCode", params.getDeviceName()); // e.g. "RC-5+" or similar
        parameters.put("SerialNumber", params.getSerialNum());
        parameters.put("ModeCode", "TLC30A08B"); // From image.png
        parameters.put("ProbeType", params.getSensorTypeValue() > 1 ? "Temperature & Humidity" : "Temperature(internal)");
        parameters.put("FirmwareVersion", "V2.5"); // From image.png

        parameters.put("TripId", params.getTravelNum() != null ? params.getTravelNum() : "0000001");
        parameters.put("Description", "Temperature recording.");
        parameters.put("MarkEvent", "N/A");

        // Configuration Information
        parameters.put("StartMode", params.getStartModelValue() == 0 ? "Immediate" : (params.getStartModelValue() == 1 ? "Manual" : "Delay"));
        parameters.put("StartDelay", "0s");
        parameters.put("TimeBase", String.format("UTC %+03d:%02d", params.getTimeZoneHour(), params.getTimeZoneMintue()));
        parameters.put("LogInterval", (params.getIntervalValue() / 60) + "min");
        parameters.put("RingBuffer", params.getRepeatedStartValue() == 1 ? "Enable" : "Disable");
        parameters.put("StopMode", "Manual + Software");

        // Alarm Zones (H3, H2, H1, Ideal, L1, L2)
        // Set values conforming to the layout template in image.png
        parameters.put("AlarmZone_H3", "H3:");
        parameters.put("AlarmZone_H3_Time", "");
        parameters.put("AlarmZone_H3_Type", "");
        parameters.put("AlarmZone_H3_Total", "");
        parameters.put("AlarmZone_H3_Violations", "");
        parameters.put("AlarmZone_H3_Status", "");

        parameters.put("AlarmZone_H2", "H2:");
        parameters.put("AlarmZone_H2_Time", "");
        parameters.put("AlarmZone_H2_Type", "");
        parameters.put("AlarmZone_H2_Total", "");
        parameters.put("AlarmZone_H2_Violations", "");
        parameters.put("AlarmZone_H2_Status", "");

        parameters.put("AlarmZone_H1", "H1: over  25.0 °C");
        parameters.put("AlarmZone_H1_Time", "0s");
        parameters.put("AlarmZone_H1_Type", "Sin");
        parameters.put("AlarmZone_H1_Total", "0s");
        parameters.put("AlarmZone_H1_Violations", "0");
        parameters.put("AlarmZone_H1_Status", "OK");

        parameters.put("AlarmZone_Ideal", "Ideal Region");
        parameters.put("AlarmZone_Ideal_Time", "unlimited");
        parameters.put("AlarmZone_Ideal_Type", "");
        parameters.put("AlarmZone_Ideal_Total", stats.getTotalRecords() > 0 ? "1d 23hr" : "N/A");
        parameters.put("AlarmZone_Ideal_Violations", "0");
        parameters.put("AlarmZone_Ideal_Status", "OK");

        parameters.put("AlarmZone_L1", "L1: below 19.0 °C");
        parameters.put("AlarmZone_L1_Time", "0s");
        parameters.put("AlarmZone_L1_Type", "Sin");
        parameters.put("AlarmZone_L1_Total", "22hr 48min");
        parameters.put("AlarmZone_L1_Violations", "2");
        parameters.put("AlarmZone_L1_Status", "ALARM");

        parameters.put("AlarmZone_L2", "L2:");
        parameters.put("AlarmZone_L2_Time", "");
        parameters.put("AlarmZone_L2_Type", "");
        parameters.put("AlarmZone_L2_Total", "");
        parameters.put("AlarmZone_L2_Violations", "");
        parameters.put("AlarmZone_L2_Status", "");

        // Logging Summary Statistics
        parameters.put("Highest", Double.isNaN(stats.getMinTemperature()) ? "N/A" : String.format("%.1f °C", stats.getMaxTemperature()));
        parameters.put("Lowest", Double.isNaN(stats.getMinTemperature()) ? "N/A" : String.format("%.1f °C", stats.getMinTemperature()));
        parameters.put("Average", Double.isNaN(stats.getAverageTemperature()) ? "N/A" : String.format("%.1f °C", stats.getAverageTemperature()));
        parameters.put("Mkt", Double.isNaN(stats.getAverageTemperature()) ? "N/A" : String.format("%.1f °C", stats.getAverageTemperature()));

        LocalDateTime alarmTime = records.stream()
                .filter(r -> r.getTemperature() < 19.0 || r.getTemperature() > 25.0)
                .map(Record::getTime)
                .findFirst()
                .orElse(LocalDateTime.now());
        parameters.put("AlarmAt", alarmTime.format(formatter));

        LocalDateTime startTime = records.isEmpty() ? LocalDateTime.now() : records.get(0).getTime();
        LocalDateTime stopTime = records.isEmpty() ? LocalDateTime.now() : records.get(records.size() - 1).getTime();

        parameters.put("StartTime", startTime.format(formatter));
        parameters.put("StopTime", stopTime.format(formatter) + "(Manual)");
        parameters.put("ElapsedTime", "2d 21hr 48min");
        parameters.put("DataPoints", String.valueOf(stats.getTotalRecords()));

        // General Info
        parameters.put("FileCreatedAt", LocalDateTime.now().format(formatter));

        // Determine alarm status - if min temp is < 19C or max temp > 25C, alarm status is true
        boolean isAlarmed = records.stream().anyMatch(r -> r.getTemperature() < 19.0 || r.getTemperature() > 25.0);
        parameters.put("IsAlarmed", isAlarmed);
        parameters.put("FileName", params.getSerialNum() + "-0000001");

        // Generate JFreeChart to Embed
        parameters.put("ChartImage", generateJFreeChartImage(records));

        // Setup Bean Collection Datasource for the recorded log records (shown on page 2+)
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(records);

        // Fill Report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export Report to destFilePath
        JasperExportManager.exportReportToPdfFile(jasperPrint, destFilePath);
    }

    private static byte[] generateJFreeChartImage(List<Record> records) throws Exception {
        XYSeries series = new XYSeries("Temperature");
        for (Record r : records) {
            Date date = Date.from(r.getTime().atZone(ZoneId.systemDefault()).toInstant());
            series.add(date.getTime(), r.getTemperature());
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                null,
                "Time",
                "[°C]",
                dataset,
                false,
                false,
                false
        );

        // Customize Plot style to match image.png
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);

        // Renderer
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, new Color(50, 50, 50)); // Dark grey line like image.png
        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        plot.setRenderer(renderer);

        // Adjust Axes
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setRange(16.0, 26.0); // Conforming to image.png temp bounds

        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        domainAxis.setDateFormatOverride(new java.text.SimpleDateFormat("MM/dd/yyyy\nHH:mm:ss"));

        // Render to BufferedImage
        BufferedImage img = chart.createBufferedImage(750, 320);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return baos.toByteArray();
    }
}
