package virtualRobot.commands;

import virtualRobot.Condition;
import virtualRobot.PIDController;
import virtualRobot.components.Motor;
import virtualRobot.components.Sensor;

/**
 * Created by shant on 11/5/2015.
 * Moves a Motor
 */
public class MoveMotor extends Command {
	private Motor motor;
	private Sensor encoder;
	private boolean clearEncoders;
	private double power;
	private PIDController pidController;
	private Translate.RunMode runMode;
	private double tolerance;
	private double timeLimit = Double.MAX_VALUE;

	public MoveMotor() {

		motor = null;
		encoder = null;
		power = 1;
		runMode = Translate.RunMode.CUSTOM;
		tolerance = 20;
		clearEncoders = true;

		pidController = new PIDController();
	}

	public MoveMotor(Motor motor) {
		this();
		this.motor = motor;
	}

	public MoveMotor(Motor motor, double power) {
		this(motor);
		this.power = power;
	}

	public MoveMotor(Motor motor, double power, double timeLimit) {
		this(motor, power);
		this.timeLimit = timeLimit;
	}

	public MoveMotor(Motor motor, double power, Sensor encoder, double target, Translate.RunMode runMode, boolean clearEncoders) {
		this(motor, power);

		this.runMode = runMode;
		this.encoder = encoder;
		this.clearEncoders = clearEncoders;

		this.pidController.setTarget(target);
	}

	public MoveMotor(Motor motor, double power, Sensor encoder, double target, Translate.RunMode runMode, boolean clearEncoders, double kP, double kI, double kD, double threshold, double tolerance) {
		this(motor, power, encoder, target, runMode, clearEncoders);

		this.pidController.setKP(kP);
		this.pidController.setKI(kI);
		this.pidController.setKD(kD);
		this.pidController.setThreshold(threshold);

		this.tolerance = tolerance;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	public void setEncoder(Sensor encoder) {
		this.encoder = encoder;
	}

	public void setTarget(double target) {
		this.pidController.setTarget(target);
	}

	public void setRunMode(Translate.RunMode runMode) {
		this.runMode = runMode;
	}

	public void setPIDValues(double kP, double kI, double kD, double threshold, double tolerance) {
		this.pidController.setKP(kP);
		this.pidController.setKI(kI);
		this.pidController.setKD(kD);
		this.pidController.setThreshold(threshold);

		this.tolerance = tolerance;
	}


	@Override
	protected int activate(String s) {
		switch(s) {
			case "BREAK":
				return BREAK;
			case "END":
				return END;
		}
		return NO_CHANGE;
	}

	@Override
	public boolean changeRobotState() throws InterruptedException {

		boolean isInterrupted = false;

		switch (runMode) {
		case CUSTOM:
			motor.setPower(power);
			long start = System.currentTimeMillis();

			MainLoop: while (!isInterrupted && (System.currentTimeMillis() - start < timeLimit)) {
				switch (checkConditionals()) {
					case BREAK:
						break MainLoop;
					case END:
						return isInterrupted;
				}

				if (Thread.currentThread().isInterrupted()) {
					isInterrupted = true;
					break;
				}

				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException e) {
					isInterrupted = true;
					break;
				}
			}

			break;
		case WITH_ENCODERS:
			if (clearEncoders) encoder.clearValue();
			motor.setPower(power);

			if (power > 0) {
				MainLoop: while (!isInterrupted && encoder.getValue() < pidController.getTarget()) {
					switch (checkConditionals()) {
						case BREAK:
							break MainLoop;
						case END:
							return isInterrupted;
					}

					if (Thread.currentThread().isInterrupted()) {
						isInterrupted = true;
						break;
					}

					try {
						Thread.currentThread().sleep(25);
					} catch (InterruptedException e) {
						isInterrupted = true;
						break;
					}
				}
			} else {
				MainLoop: while (!isInterrupted && encoder.getValue() > pidController.getTarget()) {
					switch (checkConditionals()) {
						case BREAK:
							break MainLoop;
						case END:
							return isInterrupted;
					}

					if (Thread.currentThread().isInterrupted()) {
						isInterrupted = true;
						break;
					}

					try {
						Thread.currentThread().sleep(10);
					} catch (InterruptedException e) {
						isInterrupted = true;
						break;
					}
				}
			}

			break;

		case WITH_PID:
			if (clearEncoders) encoder.clearValue();

			MainLoop: while (!isInterrupted && Math.abs(encoder.getValue() - pidController.getTarget()) > tolerance) {
				switch (checkConditionals()) {
					case BREAK:
						break MainLoop;
					case END:
						return isInterrupted;
				}

				double pidOutput = pidController.getPIDOutput(Math.abs(encoder.getValue()));

				motor.setPower(pidOutput * power);

				if (Thread.currentThread().isInterrupted()) {
					isInterrupted = true;
					break;
				}

				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException e) {
					isInterrupted = true;
					break;
				}

			}
		default:
			break;
		}

		motor.setPower(0);

		return isInterrupted;
	}
}
