package virtualRobot.hardware;

import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;

import virtualRobot.SallyJoeBot;
import virtualRobot.commands.Command;

/*
The virtual Motor component
 */
public class Motor {
	protected SallyJoeBot robot = Command.ROBOT;
	public static final double MAX_POWER = 1;
	public static final double STATIONARY = 0;
	Sensor position;
	boolean positionReverse;
	MotorConfigurationType motorType;



    private volatile double power;

    public Motor() {
        power = 0;
		position = new Sensor();
		positionReverse = false;
    }

    public synchronized double getPower () {
//		if (robot != null) {
//			robot.addToTelemetry("motorTime: ", System.currentTimeMillis());
//		}
        return power;
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
		this.position.setRawValue((positionReverse ? -1 : 1) * position);
	}

	public synchronized void setPositionReversed(boolean isRev) {
    	positionReverse = isRev;
	}

	public synchronized boolean isPositionReversed() {
    	return positionReverse;
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