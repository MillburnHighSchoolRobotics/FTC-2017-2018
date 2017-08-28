package org.firstinspires.ftc.teamcode;

import com.kauailabs.navx.ftc.MPU9250;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Map;

import virtualRobot.LogicThread;
import virtualRobot.SallyJoeBot;
import virtualRobot.commands.Command;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;
import virtualRobot.hardware.Motor;
//import virtualRobot.LogicThreads.TeleopLogic;


/*
Updates Virtual sensors etc to corresponds to their real hardware.
Updates Real hardware (e.g. motors) to correspond to the values of their virtual componennts
 */
public abstract class UpdateThread extends OpMode {
	private SallyJoeBot robot;
	protected Class<? extends LogicThread> logicThread;
	private Thread t;
	private CreateVuforia cv;
	boolean tInstantiated= false;
	public static boolean allDone = false;

	//here we will initiate all of our PHYSICAL hardware. E.g: private DcMotor leftBack...
	//also initiate sensors. E.g. private AnalogInput sonar, private ColorSensor colorSensor, private DigitalChannel ...

	private MPU9250 imu;
	private DcMotor leftFront;


//Now initiate the VIRTUAL componenents (from VirtualRobot!!), e.g. private Motor vDriveRightMotor, private virtualRobot.hardware.Servo ..., private Sensor vDriveRightMotorEncoder, private LocationSensor vLocationSensor

	private Motor vLeftFront;

    private ElapsedTime runtime = new ElapsedTime();

	private ArrayList<String> robotProgress;

	private long timePerIter = 1000, startTime, iterCount = 0;

	@Override
	public void init() {
		allDone = false;
        //MOTOR SETUP (with physical componenents, e.g. leftBack = hardwareMap.dcMotor.get("leftBack")
		leftFront = hardwareMap.dcMotor.get("leftFront");

        //SERVO SETUP (with physical hardware, e.g. servo = hardwareMap....)

        //REVERSE ONE SIDE (If needed, e.g. rightFront.setDirection(DcMotor.Direction.REVERSE)

        //SENSOR SETUP e.g. colorSensor = hardwareMap.colorsensor.get("color"), sonar1 = hardwareMap.analogInput.get("sonar1"), liftEndStop1 = hardwareMap.digitalChannel.get("liftEndStop1")

		//FETCH VIRTUAL ROBOT FROM COMMAND INTERFACE
		robot = Command.ROBOT;
		robot.initialBattery = getBatteryVoltage();

        //FETCH VIRTUAL COMPONENTS OF VIRTUAL ROBOT from robot. E.g. vDriveLeftMotor = robot.getDriveLeftMotor();
		vLeftFront = robot.getLFMotor();

		//Setup Physical Components

		addPresets();
        setLogicThread();
//		cv = new CreateVuforia(LogicThread, vuforiaEverywhere, t);
//		new Thread (cv).start();
	}

	public void init_loop () {
		telemetry.addData("Is Running Version: ", Translate.KPt + " 2.0");
        telemetry.addData("Init Loop Time", runtime.toString());
		telemetry.addData("Battery Voltage: ", getBatteryVoltage());
		telemetry.addData("Is Good for Testing: ", getBatteryVoltage() < 13.5 ? "NO, BATTERY IS TOO LOW" : "YES");

	}

	public void start() {
		//set encoders e.g. vDriveRightMotorEncoder.setRawValue(-rightFront.getCurrentPosition())

		Rotate.setCurrentAngle(0);
		telemetry.addData("Before thread", "");
		try {
			t = new Thread(logicThread.newInstance());
			tInstantiated = true;
		} catch (InstantiationException e) {
			tInstantiated = false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			tInstantiated = false;
		}
		telemetry.addData("Instantiated?: ", tInstantiated);
		if (tInstantiated)
			t.start();
//		telemetry.addData("Started Logic ", t.isAlive());
		Thread.currentThread().setPriority(10);
	}
	
	public void loop() {
		// Update Location. E.g.: double prevEcnoderValue=?, newEncoderValue=?,
		startTime = System.currentTimeMillis();
		//TODO: Calculate values for prev and newEncoderValues (Not top priority, locationSensor may not be used)

		// Update Sensor Values E.g. vPitchSensor.setRawValue(imu.getIntegratedPitch()); vHeadingSensor, vRollSensor, vColorSensor...

		// Capture Motor Powers,E.g. double leftPower = vDriveLeftMotore.getPower();
		double leftPower = vLeftFront.getPower();

		// Copy State of Motors and Servos E.g. leftFront.setPower(leftPower), Servo.setPosition(vServo.getPosition());
		leftFront.setPower(leftPower);

		for (Map.Entry<String,Object> e: robot.getTelemetry().entrySet()) {
			telemetry.addData(e.getKey(),e.getValue());
		}

		for (int i = 0; i < robot.getProgress().size(); i++) {
			telemetry.addData("robot progress " + i, robot.getProgress().get(i));
		}
		telemetry.addData("Logic is Alive: ", t.isAlive());
		telemetry.addData("Motor Power: ", leftPower);
		telemetry.addData("Motor: ", robot.getLFMotor().toString());
		telemetry.addData("Iterations: ", iterCount);
		iterCount++;
    }
	
	public void stop() {
//		imu.close();
		allDone = true;
		if (tInstantiated)
			t.interrupt();
	}

	public abstract void setLogicThread();

    public void addPresets(){}

	public synchronized double getBatteryVoltage() {
		double result = Double.POSITIVE_INFINITY;
		for (VoltageSensor sensor : hardwareMap.voltageSensor) {
			double voltage = sensor.getVoltage();
			if (voltage > 0) {
				result = Math.min(result, voltage);
			}
		}
		return result;
	}
}

