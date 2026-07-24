package elitech.report;

import elitech.model.Parameters;
import elitech.model.Record;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exports data logger configurations, parsed records, and statistics into Excel (XLSX) format.
 */
public class ExcelExporter {

    public static void exportToExcel(String filePath, Parameters params, List<Record> records) throws IOException {
        ReportStats stats = new ReportStats(records);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Elitech Report");

            // Fonts & Styles
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);

            Font sectionFont = workbook.createFont();
            sectionFont.setBold(true);
            sectionFont.setFontHeightInPoints((short) 11);

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);

            CellStyle sectionStyle = workbook.createCellStyle();
            sectionStyle.setFont(sectionFont);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            int rowNum = 0;

            // Title
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("ELITECH DATA LOGGER EXCEL REPORT");
            titleCell.setCellStyle(titleStyle);
            rowNum++; // Blank row

            // Device Configuration Section
            Row configHeaderRow = sheet.createRow(rowNum++);
            Cell configHeaderCell = configHeaderRow.createCell(0);
            configHeaderCell.setCellValue("Device Configuration");
            configHeaderCell.setCellStyle(sectionStyle);

            String[][] configData = {
                {"Device Serial Number", params.getSerialNum()},
                {"Device Name", params.getDeviceName()},
                {"Logger State Value", String.valueOf(params.getDeviceStateValue())},
                {"Log Interval (seconds)", String.valueOf(params.getIntervalValue())},
                {"Battery Level (%)", params.getBattery() + "%"}
            };

            for (String[] config : configData) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(config[0]);
                row.createCell(1).setCellValue(config[1]);
            }
            rowNum++; // Blank row

            // Statistics Section
            Row statsHeaderRow = sheet.createRow(rowNum++);
            Cell statsHeaderCell = statsHeaderRow.createCell(0);
            statsHeaderCell.setCellValue("Summary Statistics");
            statsHeaderCell.setCellStyle(sectionStyle);

            Row rTotal = sheet.createRow(rowNum++);
            rTotal.createCell(0).setCellValue("Total Logged Records");
            rTotal.createCell(1).setCellValue(stats.getTotalRecords());

            Row rMinTemp = sheet.createRow(rowNum++);
            rMinTemp.createCell(0).setCellValue("Min Temperature (C)");
            rMinTemp.createCell(1).setCellValue(Double.isNaN(stats.getMinTemperature()) ? 0.0 : stats.getMinTemperature());

            Row rMaxTemp = sheet.createRow(rowNum++);
            rMaxTemp.createCell(0).setCellValue("Max Temperature (C)");
            rMaxTemp.createCell(1).setCellValue(Double.isNaN(stats.getMaxTemperature()) ? 0.0 : stats.getMaxTemperature());

            Row rAvgTemp = sheet.createRow(rowNum++);
            rAvgTemp.createCell(0).setCellValue("Average Temperature (C)");
            rAvgTemp.createCell(1).setCellValue(Double.isNaN(stats.getAverageTemperature()) ? 0.0 : stats.getAverageTemperature());

            if (params.getSensorTypeValue() > 1) {
                Row rMinHum = sheet.createRow(rowNum++);
                rMinHum.createCell(0).setCellValue("Min Humidity (%)");
                rMinHum.createCell(1).setCellValue(Double.isNaN(stats.getMinHumidity()) ? 0.0 : stats.getMinHumidity());

                Row rMaxHum = sheet.createRow(rowNum++);
                rMaxHum.createCell(0).setCellValue("Max Humidity (%)");
                rMaxHum.createCell(1).setCellValue(Double.isNaN(stats.getMaxHumidity()) ? 0.0 : stats.getMaxHumidity());

                Row rAvgHum = sheet.createRow(rowNum++);
                rAvgHum.createCell(0).setCellValue("Average Humidity (%)");
                rAvgHum.createCell(1).setCellValue(Double.isNaN(stats.getAverageHumidity()) ? 0.0 : stats.getAverageHumidity());
            }

            Row rMarks = sheet.createRow(rowNum++);
            rMarks.createCell(0).setCellValue("Marks Count");
            rMarks.createCell(1).setCellValue(stats.getMarkCount());

            rowNum++; // Blank row

            // Records Block Header
            Row dataHeaderRow = sheet.createRow(rowNum++);
            String[] headers = params.getSensorTypeValue() > 1
                ? new String[]{"#", "Timestamp", "Temperature (C)", "Humidity (%)", "Mark", "Pause", "Stop", "Illumination", "Vibration"}
                : new String[]{"#", "Timestamp", "Temperature (C)", "Mark", "Pause", "Stop", "Illumination", "Vibration"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = dataHeaderRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Raw Records Data
            for (int i = 0; i < records.size(); i++) {
                Record r = records.get(i);
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(r.getTime().format(formatter));
                row.createCell(2).setCellValue(r.getTemperature());

                int cellIdx = 3;
                if (params.getSensorTypeValue() > 1) {
                    row.createCell(cellIdx++).setCellValue(r.getHumidity() != null ? r.getHumidity() : 0.0);
                }
                row.createCell(cellIdx++).setCellValue(r.isMark());
                row.createCell(cellIdx++).setCellValue(r.isPause());
                row.createCell(cellIdx++).setCellValue(r.isStop());
                row.createCell(cellIdx++).setCellValue(r.getIllumination());
                row.createCell(cellIdx++).setCellValue(r.getVibration());
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }
}
