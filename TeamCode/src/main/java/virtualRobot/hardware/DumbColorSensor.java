package virtualRobot.hardware;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by david on 11/3/17.
 */

public class DumbColorSensor {
    private AtomicInteger red;
    private AtomicInteger green;
    private AtomicInteger blue;
//    private int red;
//    private int green;
//    private int blue;

	public DumbColorSensor() {
		red = new AtomicInteger();
		green = new AtomicInteger();
		blue = new AtomicInteger();
	}

    public int getRed() {
        return red.get();
    }

    public void setRed(int red) {
        this.red.set(red);
    }

    public int getGreen() {
        return green.get();
    }

    public void setGreen(int green) {
        this.green.set(green);
    }

    public int getBlue() {
        return blue.get();
    }

    public void setBlue(int blue) {
        this.blue.set(blue);
    }


}
