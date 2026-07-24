package elitech.model;

/**
 * Parameter configuration specific to Serial/COM port-connected Elitech data loggers.
 */
public class COMParameters extends Parameters {
    private String comPortName;
    private int baudRate = 115200;

    public String getComPortName() {
        return comPortName;
    }

    public void setComPortName(String comPortName) {
        this.comPortName = comPortName;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }
}
