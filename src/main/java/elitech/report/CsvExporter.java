package elitech.report;

import elitech.model.Parameters;
import elitech.model.Record;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exports data logger configurations, parsed records, and statistics into CSV format.
 */
public class CsvExporter {

    public static void exportToCsv(String filePath, Parameters params, List<Record> records) throws IOException {
        ReportStats stats = new ReportStats(records);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write Header Configuration Block
            writer.write("Elitech Data Logger CSV Report\n");
            writer.write("Device Serial Number," + params.getSerialNum() + "\n");
            writer.write("Device Name," + params.getDeviceName() + "\n");
            writer.write("Logger State Value," + params.getDeviceStateValue() + "\n");
            writer.write("Log Interval (seconds)," + params.getIntervalValue() + "\n");
            writer.write("Battery Level (%)," + params.getBattery() + "%\n");
            writer.write("\n");

            // Write Statistics Block
            writer.write("Summary Statistics\n");
            writer.write("Total Logged Records," + stats.getTotalRecords() + "\n");
            writer.write("Min Temperature (C)," + (Double.isNaN(stats.getMinTemperature()) ? "N/A" : String.format("%.1f", stats.getMinTemperature())) + "\n");
            writer.write("Max Temperature (C)," + (Double.isNaN(stats.getMaxTemperature()) ? "N/A" : String.format("%.1f", stats.getMaxTemperature())) + "\n");
            writer.write("Average Temperature (C)," + (Double.isNaN(stats.getAverageTemperature()) ? "N/A" : String.format("%.1f", stats.getAverageTemperature())) + "\n");
            if (params.getSensorTypeValue() > 1) {
                writer.write("Min Humidity (%)," + (Double.isNaN(stats.getMinHumidity()) ? "N/A" : String.format("%.1f", stats.getMinHumidity())) + "\n");
                writer.write("Max Humidity (%)," + (Double.isNaN(stats.getMaxHumidity()) ? "N/A" : String.format("%.1f", stats.getMaxHumidity())) + "\n");
                writer.write("Average Humidity (%)," + (Double.isNaN(stats.getAverageHumidity()) ? "N/A" : String.format("%.1f", stats.getAverageHumidity())) + "\n");
            }
            writer.write("Marks Count," + stats.getMarkCount() + "\n");
            writer.write("Pauses Count," + stats.getPauseCount() + "\n");
            writer.write("Stops Count," + stats.getStopCount() + "\n");
            writer.write("\n");

            // Write Records Block
            if (params.getSensorTypeValue() > 1) {
                writer.write("Record Number,Timestamp,Temperature (C),Humidity (%),Mark,Pause,Stop,Illumination,Vibration\n");
            } else {
                writer.write("Record Number,Timestamp,Temperature (C),Mark,Pause,Stop,Illumination,Vibration\n");
            }

            for (int i = 0; i < records.size(); i++) {
                Record r = records.get(i);
                writer.write((i + 1) + ",");
                writer.write(r.getTime().format(formatter) + ",");
                writer.write(String.format("%.1f", r.getTemperature()) + ",");
                if (params.getSensorTypeValue() > 1) {
                    writer.write((r.getHumidity() != null ? String.format("%.1f", r.getHumidity()) : "N/A") + ",");
                }
                writer.write(r.isMark() + ",");
                writer.write(r.isPause() + ",");
                writer.write(r.isStop() + ",");
                writer.write(r.getIllumination() + ",");
                writer.write(r.getVibration() + "\n");
            }
        }
    }
}
