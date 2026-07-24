package elitech.report;

import elitech.model.Record;

import java.util.List;

/**
 * Summary statistics of parsed data logger records.
 */
public class ReportStats {
    private int totalRecords;
    private double minTemperature = Double.NaN;
    private double maxTemperature = Double.NaN;
    private double averageTemperature = Double.NaN;

    private double minHumidity = Double.NaN;
    private double maxHumidity = Double.NaN;
    private double averageHumidity = Double.NaN;

    private int markCount;
    private int pauseCount;
    private int stopCount;

    public ReportStats(List<Record> records) {
        if (records == null || records.isEmpty()) {
            return;
        }

        totalRecords = records.size();
        double tempSum = 0;
        int tempCount = 0;
        double humSum = 0;
        int humCount = 0;

        for (Record r : records) {
            double temp = r.getTemperature();
            if (!Double.isNaN(temp)) {
                if (Double.isNaN(minTemperature) || temp < minTemperature) {
                    minTemperature = temp;
                }
                if (Double.isNaN(maxTemperature) || temp > maxTemperature) {
                    maxTemperature = temp;
                }
                tempSum += temp;
                tempCount++;
            }

            Double hum = r.getHumidity();
            if (hum != null && !Double.isNaN(hum)) {
                if (Double.isNaN(minHumidity) || hum < minHumidity) {
                    minHumidity = hum;
                }
                if (Double.isNaN(maxHumidity) || hum > maxHumidity) {
                    maxHumidity = hum;
                }
                humSum += hum;
                humCount++;
            }

            if (r.isMark()) {
                markCount++;
            }
            if (r.isPause()) {
                pauseCount++;
            }
            if (r.isStop()) {
                stopCount++;
            }
        }

        if (tempCount > 0) {
            averageTemperature = tempSum / tempCount;
        }
        if (humCount > 0) {
            averageHumidity = humSum / humCount;
        }
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public double getMinHumidity() {
        return minHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }

    public double getAverageHumidity() {
        return averageHumidity;
    }

    public int getMarkCount() {
        return markCount;
    }

    public int getPauseCount() {
        return pauseCount;
    }

    public int getStopCount() {
        return stopCount;
    }
}
