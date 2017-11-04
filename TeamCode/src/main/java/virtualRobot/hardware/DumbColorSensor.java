package virtualRobot.hardware;

/**
 * Created by david on 11/3/17.
 */

public class DumbColorSensor {
    private int red;
    private int green;
    private int blue;

    public synchronized int getRed() {
        return red;
    }

    public synchronized void setRed(int red) {
        this.red = red;
    }

    public synchronized int getGreen() {
        return green;
    }

    public synchronized void setGreen(int green) {
        this.green = green;
    }

    public synchronized int getBlue() {
        return blue;
    }

    public synchronized void setBlue(int blue) {
        this.blue = blue;
    }


}
