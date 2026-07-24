package elitech.report;

import elitech.model.Parameters;
import elitech.model.Record;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exports data logger configurations, parsed records, and statistics into structured text format.
 */
public class TxtExporter {

    public static void exportToTxt(String filePath, Parameters params, List<Record> records) throws IOException {
        ReportStats stats = new ReportStats(records);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("========================================================================\n");
            writer.write("                   ELITECH DATA LOGGER TXT REPORT                       \n");
            writer.write("========================================================================\n\n");

            writer.write("DEVICE CONFIGURATION:\n");
            writer.write(String.format("  Device Serial Number  : %s\n", params.getSerialNum()));
            writer.write(String.format("  Device Name           : %s\n", params.getDeviceName()));
            writer.write(String.format("  Logger State Value    : %d\n", params.getDeviceStateValue()));
            writer.write(String.format("  Log Interval          : %d seconds\n", params.getIntervalValue()));
            writer.write(String.format("  Battery Level         : %d%%\n\n", params.getBattery()));

            writer.write("SUMMARY STATISTICS:\n");
            writer.write(String.format("  Total Records         : %d\n", stats.getTotalRecords()));
            writer.write(String.format("  Min Temperature       : %s C\n", Double.isNaN(stats.getMinTemperature()) ? "N/A" : String.format("%.1f", stats.getMinTemperature())));
            writer.write(String.format("  Max Temperature       : %s C\n", Double.isNaN(stats.getMaxTemperature()) ? "N/A" : String.format("%.1f", stats.getMaxTemperature())));
            writer.write(String.format("  Average Temperature   : %s C\n", Double.isNaN(stats.getAverageTemperature()) ? "N/A" : String.format("%.1f", stats.getAverageTemperature())));
            if (params.getSensorTypeValue() > 1) {
                writer.write(String.format("  Min Humidity          : %s %%\n", Double.isNaN(stats.getMinHumidity()) ? "N/A" : String.format("%.1f", stats.getMinHumidity())));
                writer.write(String.format("  Max Humidity          : %s %%\n", Double.isNaN(stats.getMaxHumidity()) ? "N/A" : String.format("%.1f", stats.getMaxHumidity())));
                writer.write(String.format("  Average Humidity      : %s %%\n", Double.isNaN(stats.getAverageHumidity()) ? "N/A" : String.format("%.1f", stats.getAverageHumidity())));
            }
            writer.write(String.format("  Marks Count           : %d\n", stats.getMarkCount()));
            writer.write(String.format("  Pauses Count          : %d\n", stats.getPauseCount()));
            writer.write(String.format("  Stops Count           : %d\n\n", stats.getStopCount()));

            writer.write("RECORDED DATA:\n");
            if (params.getSensorTypeValue() > 1) {
                writer.write(String.format("  %-6s | %-19s | %-12s | %-12s | %-5s | %-5s | %-5s | %-12s | %-12s\n",
                        "Index", "Timestamp", "Temp(C)", "Humi(%)", "Mark", "Pause", "Stop", "Illumination", "Vibration"));
                writer.write("  -----------------------------------------------------------------------------------------------------------------\n");
            } else {
                writer.write(String.format("  %-6s | %-19s | %-12s | %-5s | %-5s | %-5s | %-12s | %-12s\n",
                        "Index", "Timestamp", "Temp(C)", "Mark", "Pause", "Stop", "Illumination", "Vibration"));
                writer.write("  ---------------------------------------------------------------------------------------------------\n");
            }

            for (int i = 0; i < records.size(); i++) {
                Record r = records.get(i);
                if (params.getSensorTypeValue() > 1) {
                    writer.write(String.format("  %-6d | %-19s | %-12.1f | %-12.1f | %-5b | %-5b | %-5b | %-12s | %-12s\n",
                            (i + 1),
                            r.getTime().format(formatter),
                            r.getTemperature(),
                            r.getHumidity() != null ? r.getHumidity() : 0.0,
                            r.isMark(),
                            r.isPause(),
                            r.isStop(),
                            r.getIllumination(),
                            r.getVibration()
                    ));
                } else {
                    writer.write(String.format("  %-6d | %-19s | %-12.1f | %-5b | %-5b | %-5b | %-12s | %-12s\n",
                            (i + 1),
                            r.getTime().format(formatter),
                            r.getTemperature(),
                            r.isMark(),
                            r.isPause(),
                            r.isStop(),
                            r.getIllumination(),
                            r.getVibration()
                    ));
                }
            }
        }
    }
}
