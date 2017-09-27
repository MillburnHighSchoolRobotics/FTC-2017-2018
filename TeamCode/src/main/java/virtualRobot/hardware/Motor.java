package virtualRobot.hardware;

import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;

/*
The virtual Motor component
 */
public class Motor {

	public static final double MAX_POWER = 1;
	public static final double STATIONARY = 0;
	Sensor position;
	MotorConfigurationType motorType;

    private volatile double power;

    public Motor() {
        power = 0;
		position = new Sensor();
    }

    public synchronized double getPower () {
    	double retVal = 0;
    	synchronized (this) {
    		retVal = power;
    	}
        return retVal;
    }

    public synchronized void setPower(double newPower) {
		if (Double.isNaN(newPower)) {
			throw new IllegalArgumentException("Motor power cannot be NaN");
		}
		power = newPower;
		if (power > MAX_POWER) {
			power = 1;
		}

		if (power < -MAX_POWER) {
			power = -1;
		}
    }

	public synchronized Motor setMotorType(MotorConfigurationType type) { motorType = type; return this; }

	public synchronized MotorConfigurationType getMotorType() { return motorType; }

	public synchronized void setPosition(int position) {
		this.position.setRawValue(position);
	}

	public synchronized int getPosition() {
		return (int) position.getValue();
	}

	public synchronized int getRawValue() {
		return (int) position.getRawValue();
	}

	public synchronized void clearEncoder() {
		position.clearValue();
	}
}