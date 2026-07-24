package elitech.model;

import java.time.LocalDateTime;

/**
 * Represents a data log record from an Elitech device.
 */
public class Record {
    private LocalDateTime time;
    private double temperature;
    private Double humidity; // Nullable if device doesn't support humidity
    private boolean mark;
    private boolean pause;
    private boolean stop;
    private String illumination = "None";
    private String vibration = "None";

    public Record() {}

    public Record(LocalDateTime time, double temperature, Double humidity, boolean mark, boolean pause, boolean stop, String illumination, String vibration) {
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.mark = mark;
        this.pause = pause;
        this.stop = stop;
        this.illumination = illumination;
        this.vibration = vibration;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public String getIllumination() {
        return illumination;
    }

    public void setIllumination(String illumination) {
        this.illumination = illumination;
    }

    public String getVibration() {
        return vibration;
    }

    public void setVibration(String vibration) {
        this.vibration = vibration;
    }

    @Override
    public String toString() {
        return "Record{" +
                "time=" + time +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", mark=" + mark +
                ", pause=" + pause +
                ", stop=" + stop +
                ", illumination='" + illumination + '\'' +
                ", vibration='" + vibration + '\'' +
                '}';
    }
}
