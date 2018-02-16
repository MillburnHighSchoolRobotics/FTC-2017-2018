package virtualRobot.hardware;

/**
 * Created by david on 2/16/18.
 */

public class ContinuousRotationSensor extends Sensor {
    //offset is the amount of revolutions

    @Override
    public synchronized void clearValue() {
        offset = 0;
    }

    @Override
    public synchronized double getValue() {
        return hardValue + (offset * 360);
    }

    @Override
    public synchronized void setRawValue(double hardValue) {
        if (hardValue > 180 || hardValue < -180) return;
        double oldValue = super.hardValue;
        if (Math.abs(oldValue - hardValue) > 180) {
            int oldSign = (int)Math.signum(oldValue);
            offset += oldSign; //up one revolution if 1 --> -1, down one if -1 --> 1
        }
        super.setRawValue(hardValue);
    }

    //TODO: Test this function
    @Override
    public synchronized void setValue(double relVal) {
        offset = (int)Math.floor((relVal + 180)/360);
        hardValue = relVal - (offset * 360);
    }
}
