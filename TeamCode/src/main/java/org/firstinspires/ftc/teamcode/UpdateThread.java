package org.firstinspires.ftc.teamcode;

import com.kauailabs.navx.ftc.MPU9250;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.util.ArrayList;
import java.util.Map;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.SallyJoeBot;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.commands.Command;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Sensor;
import virtualRobot.logicThreads.testing.TestBackendLogic;


/*
Updates Virtual sensors etc to corresponds to their real hardware.
Updates Real hardware (e.g. motors) to correspond to the values of their virtual componennts
 */
public abstract class UpdateThread extends OpMode {
	private SallyJoeBot robot;
	protected Class<? extends LogicThread> logicThread;
	private LogicThread t;
	private CreateVuforia cv;
	boolean tInstantiated= false;
	public static boolean allDone = false;
	public static VuforiaLocalizerImplSubclass vuforiaInstance = null;
	private static ArrayList<Class<? extends LogicThread>> exceptions = new ArrayList<>();

	//Here we add all the exception logic threads that are exempt from vuforia initialization
	//This just helps to lessen the time needed to test
	static {
		exceptions.add(TestBackendLogic.class);
	}

	//here we will initiate all of our PHYSICAL hardware. E.g: private DcMotor leftBack...
	//also initiate sensors. E.g. private AnalogInput sonar, private ColorSensor colorSensor, private DigitalChannel ...

	private MPU9250 imu;
	private DcMotor leftFront;


//Now initiate the VIRTUAL componenents (from VirtualRobot!!), e.g. private Motor vDriveRightMotor, private virtualRobot.hardware.Servo ..., private Sensor vDriveRightMotorEncoder, private LocationSensor vLocationSensor

    private Sensor vVoltageSensor;

	private Motor vLeftFront;


	private JoystickController vJoystickController1;
	private JoystickController vJoystickController2;

    private ElapsedTime runtime = new ElapsedTime();

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
		vJoystickController1 = robot.getJoystickController1();
		vJoystickController2 = robot.getJoystickController2();
        vVoltageSensor = robot.getVoltageSensor();

		//Setup Physical Components

		addPresets();
        setLogicThread();
//		cv = new CreateVuforia(LogicThread, vuforiaEverywhere, t);
//		new Thread (cv).start();
		if (!exceptions.contains(logicThread)){
			VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
			params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
			params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
			UpdateThread.vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
		}
	}

	public void init_loop () {
		telemetry.addData("Is Running Version: ", Translate.KPt + " 2.0");
        telemetry.addData("Init Loop Time", runtime.toString());
		telemetry.addData("Battery Voltage: ", getBatteryVoltage());
		telemetry.addData("Is Good for Testing: ", getBatteryVoltage() < 13.5 ? "NO, BATTERY IS TOO LOW" : "YES");

	}

	public void start() {
		//set encoders e.g. vDriveRightMotorEncoder.setRawValue(-rightFront.getCurrentPosition())
        vVoltageSensor.setRawValue(getBatteryVoltage());
        robot.initialBattery = vVoltageSensor.getRawValue();


		Rotate.setCurrentAngle(0);
//		telemetry.addData("Before thread", "");
		try {
			t = logicThread.newInstance();
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
		runtime.reset();
	}
	
	public void loop() {
		// Update Location. E.g.: double prevEcnoderValue=?, newEncoderValue=?,
		//TODO: Calculate values for prev and newEncoderValues (Not top priority, locationSensor may not be used)

		// Update Sensor Values E.g. vPitchSensor.setRawValue(imu.getIntegratedPitch()); vHeadingSensor, vRollSensor, vColorSensor...
        vVoltageSensor.setRawValue(getBatteryVoltage());

        vLeftFront.setPosition(leftFront.getCurrentPosition());

		try {
			vJoystickController1.copyStates(gamepad1);
			vJoystickController2.copyStates(gamepad2);
		} catch (RobotCoreException e) {
			e.printStackTrace();
		}

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
		telemetry.addData("All is Alive", t.allIsAlive());
		telemetry.addData("Motor Power: ", leftPower);
		//telemetry.addData("Motor: ", robot.getLFMotor().toString());
		telemetry.addData("Loop Time: ", runtime.toString());
    }
	
	public void stop() {
//		imu.close();
		vuforiaInstance = null;
		allDone = true;
		if (tInstantiated)
			t.interrupt();
		System.gc();
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

