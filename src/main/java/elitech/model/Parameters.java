package elitech.model;

import java.time.LocalDateTime;

/**
 * Common configuration parameters for Elitech data loggers.
 */
public class Parameters {
    protected int modelValue;
    protected String serialNum = "000000000000";
    protected String deviceName = "Data Logger";
    protected int deviceStateValue; // 0=unopened, 1=starting, 2=running, 3=stopped
    protected int recordsNumberActual;
    protected int deviceCapacityActual = 16000;
    protected int intervalValue = 300; // default 5 mins
    protected int timeZoneHour = 8; // UTC+8
    protected int timeZoneMintue = 0;
    protected int startModelValue = 0; // 0=immediate, 1=button, 2=delay
    protected int stopModeSetByButtonValue = 1;
    protected int stopModeSetBySoftwareValue = 1;
    protected int productProperty = 0;
    protected int sensorTypeValue = 1; // 1=temp, 2=temp+humi
    protected int sensorTempUnitVAL = 0; // 0=C, 1=F
    protected int alarmModeTempValue = 1; // 0=none, 1=limit, 2=zone
    protected int repeatedStartValue = 0;
    protected int pauseAllowValue = 0;
    protected int passwordAllowValue = 0;
    protected String travelNum = "000000000000";
    protected int pdfLanguageValue = 1; // English
    protected LocalDateTime deviceClock = LocalDateTime.now();
    protected LocalDateTime loggerStartTimeValue = LocalDateTime.now();
    protected int battery = 100;
    protected int protocolVersion = 20; // default V2.0

    public int getModelValue() {
        return modelValue;
    }

    public void setModelValue(int modelValue) {
        this.modelValue = modelValue;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceStateValue() {
        return deviceStateValue;
    }

    public void setDeviceStateValue(int deviceStateValue) {
        this.deviceStateValue = deviceStateValue;
    }

    public int getRecordsNumberActual() {
        return recordsNumberActual;
    }

    public void setRecordsNumberActual(int recordsNumberActual) {
        this.recordsNumberActual = recordsNumberActual;
    }

    public int getDeviceCapacityActual() {
        return deviceCapacityActual;
    }

    public void setDeviceCapacityActual(int deviceCapacityActual) {
        this.deviceCapacityActual = deviceCapacityActual;
    }

    public int getIntervalValue() {
        return intervalValue;
    }

    public void setIntervalValue(int intervalValue) {
        this.intervalValue = intervalValue;
    }

    public int getTimeZoneHour() {
        return timeZoneHour;
    }

    public void setTimeZoneHour(int timeZoneHour) {
        this.timeZoneHour = timeZoneHour;
    }

    public int getTimeZoneMintue() {
        return timeZoneMintue;
    }

    public void setTimeZoneMintue(int timeZoneMintue) {
        this.timeZoneMintue = timeZoneMintue;
    }

    public int getStartModelValue() {
        return startModelValue;
    }

    public void setStartModelValue(int startModelValue) {
        this.startModelValue = startModelValue;
    }

    public int getStopModeSetByButtonValue() {
        return stopModeSetByButtonValue;
    }

    public void setStopModeSetByButtonValue(int stopModeSetByButtonValue) {
        this.stopModeSetByButtonValue = stopModeSetByButtonValue;
    }

    public int getStopModeSetBySoftwareValue() {
        return stopModeSetBySoftwareValue;
    }

    public void setStopModeSetBySoftwareValue(int stopModeSetBySoftwareValue) {
        this.stopModeSetBySoftwareValue = stopModeSetBySoftwareValue;
    }

    public int getProductProperty() {
        return productProperty;
    }

    public void setProductProperty(int productProperty) {
        this.productProperty = productProperty;
    }

    public int getSensorTypeValue() {
        return sensorTypeValue;
    }

    public void setSensorTypeValue(int sensorTypeValue) {
        this.sensorTypeValue = sensorTypeValue;
    }

    public int getSensorTempUnitVAL() {
        return sensorTempUnitVAL;
    }

    public void setSensorTempUnitVAL(int sensorTempUnitVAL) {
        this.sensorTempUnitVAL = sensorTempUnitVAL;
    }

    public int getAlarmModeTempValue() {
        return alarmModeTempValue;
    }

    public void setAlarmModeTempValue(int alarmModeTempValue) {
        this.alarmModeTempValue = alarmModeTempValue;
    }

    public int getRepeatedStartValue() {
        return repeatedStartValue;
    }

    public void setRepeatedStartValue(int repeatedStartValue) {
        this.repeatedStartValue = repeatedStartValue;
    }

    public int getPauseAllowValue() {
        return pauseAllowValue;
    }

    public void setPauseAllowValue(int pauseAllowValue) {
        this.pauseAllowValue = pauseAllowValue;
    }

    public int getPasswordAllowValue() {
        return passwordAllowValue;
    }

    public void setPasswordAllowValue(int passwordAllowValue) {
        this.passwordAllowValue = passwordAllowValue;
    }

    public String getTravelNum() {
        return travelNum;
    }

    public void setTravelNum(String travelNum) {
        this.travelNum = travelNum;
    }

    public int getPdfLanguageValue() {
        return pdfLanguageValue;
    }

    public void setPdfLanguageValue(int pdfLanguageValue) {
        this.pdfLanguageValue = pdfLanguageValue;
    }

    public LocalDateTime getDeviceClock() {
        return deviceClock;
    }

    public void setDeviceClock(LocalDateTime deviceClock) {
        this.deviceClock = deviceClock;
    }

    public LocalDateTime getLoggerStartTimeValue() {
        return loggerStartTimeValue;
    }

    public void setLoggerStartTimeValue(LocalDateTime loggerStartTimeValue) {
        this.loggerStartTimeValue = loggerStartTimeValue;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
