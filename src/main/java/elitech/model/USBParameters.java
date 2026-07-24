package elitech.model;

/**
 * Parameter configuration specific to USB-connected Elitech data loggers.
 */
public class USBParameters extends Parameters {
    private boolean allowCycleAvailable = true;
    private boolean isFrontShadowExCommand = false;
    private boolean isDownloadRearShadowData = false;
    private boolean isDownloaFrontShadowData = false;
    private int requestRecordeNumber;
    private int totalRecordsNumber;

    public boolean isAllowCycleAvailable() {
        return allowCycleAvailable;
    }

    public void setAllowCycleAvailable(boolean allowCycleAvailable) {
        this.allowCycleAvailable = allowCycleAvailable;
    }

    public boolean isFrontShadowExCommand() {
        return isFrontShadowExCommand;
    }

    public void setFrontShadowExCommand(boolean frontShadowExCommand) {
        this.isFrontShadowExCommand = frontShadowExCommand;
    }

    public boolean isDownloadRearShadowData() {
        return isDownloadRearShadowData;
    }

    public void setDownloadRearShadowData(boolean downloadRearShadowData) {
        this.isDownloadRearShadowData = downloadRearShadowData;
    }

    public boolean isDownloaFrontShadowData() {
        return isDownloaFrontShadowData;
    }

    public void setDownloaFrontShadowData(boolean downloaFrontShadowData) {
        this.isDownloaFrontShadowData = downloaFrontShadowData;
    }

    public int getRequestRecordeNumber() {
        return requestRecordeNumber;
    }

    public void setRequestRecordeNumber(int requestRecordeNumber) {
        this.requestRecordeNumber = requestRecordeNumber;
    }

    public int getTotalRecordsNumber() {
        return totalRecordsNumber;
    }

    public void setTotalRecordsNumber(int totalRecordsNumber) {
        this.totalRecordsNumber = totalRecordsNumber;
    }
}
