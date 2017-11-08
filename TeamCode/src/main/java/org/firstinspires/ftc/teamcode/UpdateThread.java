package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.SallyJoeBot;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.commands.Command;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;
import virtualRobot.hardware.ContinuousRotationServo;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.IMU;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Sensor;
import virtualRobot.hardware.StateSensor;
import virtualRobot.logicThreads.competition.TeleOpCustomLogic;
import virtualRobot.logicThreads.competition.TeleOpCustomLogicRewrite;
import virtualRobot.logicThreads.competition.TeleOpLogic;
import virtualRobot.logicThreads.testing.TestBackendLogic;
import virtualRobot.utils.BetterLog;
import virtualRobot.utils.GlobalUtils;
import virtualRobot.utils.Vector3f;


/*
Updates Virtual sensors etc to corresponds to their real hardware.
Updates Real hardware (e.g. motors) to correspond to the values of their virtual componennts
 */
public abstract class UpdateThread extends OpMode {
	private SallyJoeBot robot;
	protected Class<? extends LogicThread> logicThread = null;
	private LogicThread t;
//	private CreateVuforia cv;
	boolean tInstantiated= false;
	public static VuforiaLocalizerImplSubclass vuforiaInstance = null;
	private static ArrayList<Class<? extends LogicThread>> exceptions = new ArrayList<>();

	//Here we add all the exception logic threads that are exempt from vuforia initialization
	//This just helps to lessen the time needed to test
	static {
		exceptions.add(TestBackendLogic.class);
		exceptions.add(TeleOpCustomLogic.class);
		exceptions.add(TeleOpLogic.class);
		exceptions.add(TeleOpCustomLogicRewrite.class);
	}

	//here we will initiate all of our PHYSICAL hardware. E.g: private DcMotor leftBack...
	//also initiate sensors. E.g. private AnalogInput sonar, private ColorSensor colorSensor, private DigitalChannel ...

//	private BNO055 imu;
    private BNO055IMU imu;
	private DcMotor leftFront, leftBack, rightFront, rightBack;
	private Servo glyphLiftRight, glyphLiftLeft;
	private Servo clawLeft, clawRight;
	private Servo jewelServo;
	private ColorSensor colorSensor;

//Now initiate the VIRTUAL componenents (from VirtualRobot!!), e.g. private Motor vDriveRightMotor, private virtualRobot.hardware.Servo ..., private Sensor vDriveRightMotorEncoder, private LocationSensor vLocationSensor

    private Sensor vVoltageSensor;
    private IMU vIMU;
    private StateSensor vStateSensor;
	private JoystickController vJoystickController1;
	private JoystickController vJoystickController2;

	private Motor vLeftFront, vLeftBack, vRightFront, vRightBack;
	private Motor vRollerLeft, vRollerRight;
	private Motor vRelicArm;
	private ContinuousRotationServo vRelicArmWinch;
	private virtualRobot.hardware.Servo vJewelServo;
	private virtualRobot.hardware.Servo vClawLeft, vClawRight;
	private virtualRobot.hardware.Servo vGlyphLiftLeft, vGlyphLiftRight;
	private DumbColorSensor vColorSensor;

    private ElapsedTime runtime = new ElapsedTime();


	@Override
	public void init() {
        //IMU SETUP (Do not touch)
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
//        parameters.loggingEnabled      = false;
//        parameters.loggingTag          = "IMU";
//        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
//
//        imu = hardwareMap.get(BNO055IMU.class, "imu");
//        imu.initialize(parameters);
//        imu.startAccelerationIntegration(new Position(), new Velocity(),1);

        //MOTOR SETUP (with physical componenents, e.g. leftBack = hardwareMap.dcMotor.get("leftBack")
		leftFront = hardwareMap.dcMotor.get("leftFront");
		leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		leftBack = hardwareMap.dcMotor.get("leftBack");
		leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightFront = hardwareMap.dcMotor.get("rightFront");
		rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightBack = hardwareMap.dcMotor.get("rightBack");
		rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		Log.d("Components ", "Motors Initialized");
//		rollerLeft = hardwareMap.dcMotor.get("rollerLeft");
//		rollerRight = hardwareMap.dcMotor.get("rollerRight");
		glyphLiftRight = hardwareMap.servo.get("glyphLiftRight");
		glyphLiftLeft = hardwareMap.servo.get("glyphLiftLeft");
//		relicArm = hardwareMap.dcMotor.get("relicArm");
//
//        //SERVO SETUP (with physical hardware, e.g. servo = hardwareMap....)
//		relicArmWinch = hardwareMap.servo.get("relicArmWinch");
		jewelServo = hardwareMap.servo.get("jewelArm");
		clawLeft = hardwareMap.servo.get("clawLeft");
		clawRight = hardwareMap.servo.get("clawRight");

        //REVERSE ONE SIDE (If needed, e.g. rightFront.setDirection(DcMotor.Direction.REVERSE)
		leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
		leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

		//SET MOTOR MODES
//		leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //SENSOR SETUP e.g. colorSensor = hardwareMap.colorsensor.get("color"), sonar1 = hardwareMap.analogInput.get("sonar1"), liftEndStop1 = hardwareMap.digitalChannel.get("liftEndStop1")
//		colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

		//FETCH VIRTUAL ROBOT FROM COMMAND INTERFACE
		robot = Command.ROBOT;
		robot.initialBattery = getBatteryVoltage();


		clawLeft.setPosition(1);
		clawRight.setPosition(0);
		jewelServo.setPosition(0);
		glyphLiftRight.setPosition(0);
		glyphLiftLeft.setPosition(1);

		//FETCH CONSTANT COMPONENTS OF VIRTUAL ROBOT (Do not touch)
        vJoystickController1 = robot.getJoystickController1();
        vJoystickController2 = robot.getJoystickController2();
        vVoltageSensor = robot.getVoltageSensor();
        vIMU = robot.getImu();
        vStateSensor = robot.getStateSensor();

        //FETCH VIRTUAL COMPONENTS OF VIRTUAL ROBOT from robot. E.g. vDriveLeftMotor = robot.getDriveLeftMotor();
		vLeftFront = robot.getLFMotor();
		vLeftBack = robot.getLBMotor();
		vRightFront = robot.getRFMotor();
		vRightBack = robot.getRBMotor();
		vGlyphLiftLeft = robot.getGlyphLiftLeft();
		vGlyphLiftRight = robot.getGlyphLiftRight();
		vJewelServo = robot.getJewelServo();
		vRelicArm = robot.getRelicArm();
		vRelicArmWinch = robot.getRelicArmWinch();
		vRollerLeft = robot.getRollerLeft();
		vRollerRight = robot.getRollerRight();
		vClawLeft = robot.getClawLeft();
		vClawRight = robot.getClawRight();

		//FETCH VIRTUAL SENSORS OF VIRTUAL ROBOT from robot. E.g. vColorSensor = robot.getColorSensor();
		vColorSensor = robot.getColorSensor();

		//Set Motor Types
		vLeftFront.setMotorType(leftFront.getMotorType());
		vLeftBack.setMotorType(leftBack.getMotorType());
		vRightFront.setMotorType(rightFront.getMotorType());
		vRightBack.setMotorType(rightBack.getMotorType());
//		vRollerLeft.setMotorType(rollerLeft.getMotorType());
//		vRollerRight.setMotorType(rollerRight.getMotorType());
//		vRelicArm.setMotorType(relicArm.getMotorType());

        //Setup constant components (Do not touch)
		addPresets();
        setLogicThread();
//		cv = new CreateVuforia(LogicThread, vuforiaEverywhere, t);
//		new Thread (cv).start();
//		if (!exceptions.contains(logicThread)){
//			VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
//			params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
//			params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
//			UpdateThread.vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
//		}
	}

	public void init_loop () {
		telemetry.addData("Is Running Version: ", Translate.KPt + " 3.0");
        telemetry.addData("Init Loop Time", runtime.toString());
		telemetry.addData("Battery Voltage: ", getBatteryVoltage());
//        telemetry.addData("IMU Status", imu.getSystemStatus().toShortString());
//        telemetry.addData("IMU Calibration", imu.getCalibrationStatus().toString());
		telemetry.addData("Is Good for Testing: ", getBatteryVoltage() < 13.5 ? "NO, BATTERY IS TOO LOW" : "YES");
	}

	public void start() {
		//set constants (Do not touch)
		robot.initialBattery = vVoltageSensor.getRawValue();
		GlobalUtils.setCurrentActivity((Activity) hardwareMap.appContext);
        Rotate.setCurrentAngle(0);

		//Set initial servo positions
//		relicArmWinch.setPosition(0.5);
//		clawLeft.setPosition(0);
//		clawRight.setPosition(0);

		//Copy positions to virtualRobot
//		vRelicArmWinch.setSpeed(relicArmWinch.getPosition());
		vClawLeft.setPosition(clawLeft.getPosition());
		vClawRight.setPosition(clawRight.getPosition());
		vGlyphLiftLeft.setPosition(glyphLiftLeft.getPosition());
		vGlyphLiftRight.setPosition(glyphLiftRight.getPosition());
		vJewelServo.setPosition(jewelServo.getPosition());

		//set sensors e.g. vDriveRightMotorEncoder.setRawValue(-rightFront.getCurrentPosition())
        vVoltageSensor.setRawValue(getBatteryVoltage());

//		vColorSensor.setRed(colorSensor.red());
//		vColorSensor.setBlue(colorSensor.blue());
//		vColorSensor.setGreen(colorSensor.green());


		vLeftFront.setPosition(leftFront.getCurrentPosition());
		vLeftBack.setPosition(leftBack.getCurrentPosition());
		vRightFront.setPosition(rightFront.getCurrentPosition());
		vRightBack.setPosition(rightBack.getCurrentPosition());
//		vRollerLeft.setPosition(rollerLeft.getCurrentPosition());
//		vRollerRight.setPosition(rollerRight.getCurrentPosition());
//		vRelicArm.setPosition(relicArm.getCurrentPosition());

		try {
			t = logicThread.newInstance();
			tInstantiated = true;
		} catch (InstantiationException e) {
			tInstantiated = false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			tInstantiated = false;
		} catch (NullPointerException e) {
			tInstantiated = false;
		}
		if (tInstantiated)
			t.start();
//		telemetry.addData("Started Logic ", t.isAlive());
		Thread.currentThread().setPriority(10);
		runtime.reset();
	}

	public void loop() {
		telemetry.addData("Update timestamp: ", System.currentTimeMillis());
		// Update Location. E.g.: double prevEcnoderValue=?, newEncoderValue=?,
		//TODO: Calculate values for prev and newEncoderValues (Not top priority, locationSensor may not be used)
//		Position position = imu.getPosition();
//		Velocity velocity = imu.getVelocity();

//		vStateSensor.setPosition(new Vector3f(position.x, position.y, position.z));
//		vStateSensor.setVelocity(new Vector3f(velocity.xVeloc, velocity.yVeloc, velocity.zVeloc));

        //Update the sensors that stay constant (Do not touch)
//        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//        Acceleration accel = imu.getLinearAcceleration();
//        Acceleration total = imu.getOverallAcceleration();
//        AngularVelocity angularVelocity = imu.getAngularVelocity();

//        vIMU.setLinearAccel(new Vector3f(accel.xAccel, accel.yAccel, accel.zAccel));
//        vIMU.setTotalAccel(new Vector3f(total.xAccel, total.yAccel, total.zAccel));
//        vIMU.linearAcquisition = accel.acquisitionTime;
//        vIMU.totalAcquisition = total.acquisitionTime;

//        vIMU.setYaw(angles.firstAngle);
//        vIMU.setRoll(angles.secondAngle);
//        vIMU.setPitch(angles.thirdAngle);
//        vIMU.angleAcquisition = angles.acquisitionTime;

//        vIMU.setAngularVelocity(new Vector3f(angularVelocity.xRotationRate, angularVelocity.yRotationRate, angularVelocity.zRotationRate));

        vVoltageSensor.setRawValue(getBatteryVoltage());

        try {
			telemetry.addData("Gamepad Stick Vals: ", gamepad1.left_stick_x + " " + gamepad1.left_stick_y);
            vJoystickController1.copyStates(gamepad1);
            vJoystickController2.copyStates(gamepad2);
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }

        // Update Sensor Values E.g. vPitchSensor.setRawValue(imu.getIntegratedPitch()); vHeadingSensor, vRollSensor, vColorSensor...
		vLeftFront.setPosition(leftFront.getCurrentPosition());
		vLeftBack.setPosition(leftBack.getCurrentPosition());
		vRightFront.setPosition(rightFront.getCurrentPosition());
		vRightBack.setPosition(rightBack.getCurrentPosition());
		Log.d("Completed", "virtual encoders");

		vColorSensor.setRed(colorSensor.red());
//		vColorSensor.setBlue(colorSensor.blue());
//		vColorSensor.setGreen(colorSensor.green());
		Log.d("Completed", "color sensor");

//		vRollerLeft.setPosition(rollerLeft.getCurrentPosition());
//		vRollerRight.setPosition(rollerRight.getCurrentPosition());
//		vRelicArm.setPosition(relicArm.getCurrentPosition());

		//Copy Servo Positions
//		relicArmWinch.setPosition(vRelicArmWinch.getSpeed());
		clawLeft.setPosition(vClawLeft.getPosition());
		clawRight.setPosition(vClawRight.getPosition());
		glyphLiftLeft.setPosition(vGlyphLiftLeft.getPosition());
		glyphLiftRight.setPosition(vGlyphLiftRight.getPosition());
		jewelServo.setPosition(vJewelServo.getPosition());
		Log.d("Completed", "servos");

		// Capture Motor Powers,E.g. double leftPower = vDriveLeftMotore.getPower();
		double leftFrontPower = vLeftFront.getPower();
        double leftBackPower = vLeftBack.getPower();
        double rightFrontPower = vRightFront.getPower();
        double rightBackPower = vRightBack.getPower();
        double relicArmPower = vRelicArm.getPower();
		double rollerLeftPower = vRollerLeft.getPower();
		double rollerRightPower = vRollerRight.getPower();
		Log.d("Completed", "powers");

		// Copy State of Motors and Servos E.g. leftFront.setPower(leftPower), Servo.setPosition(vServo.getPosition());
		leftFront.setPower(leftFrontPower);
		leftBack.setPower(leftBackPower);
		rightFront.setPower(rightFrontPower);
		rightBack.setPower(rightBackPower);
		Log.d("Completed", "motor powers");
//		relicArm.setPower(relicArmPower);
//		rollerLeft.setPower(rollerLeftPower);
//		rollerRight.setPower(rollerRightPower);

		for (Map.Entry<String,Object> e: robot.getTelemetry().entrySet()) {
			telemetry.addData(e.getKey(),e.getValue());
		}
		Log.d("Completed", "telemetry");

		for (int i = 0; i < robot.getProgress().size(); i++) {
			telemetry.addData("robot progress " + i, robot.getProgress().get(i));
		}

//		telemetry.addData("Gamepad"gamepad1.atRest());
		telemetry.addData("Logic is Alive: ", t.isAlive());
		telemetry.addData("All is Alive", t.allIsAlive());
		//telemetry.addData("Motor: ", robot.getLFMotor().toString());
		telemetry.addData("Loop Time: ", runtime.toString());
    }

	public void stop() {
//        imu.stopAccelerationIntegration();
//		imu.close();
//		vuforiaInstance = null;
		if (tInstantiated)
			t.interrupt();
		System.gc();
	}

	public abstract void setLogicThread();

    public void addPresets(){}

	private double getBatteryVoltage() {
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
