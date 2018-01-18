package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.SallyJoeBot;
import virtualRobot.UpdateCRServo;
import virtualRobot.UpdateColorSensor;
import virtualRobot.UpdateMotor;
import virtualRobot.UpdateSensor;
import virtualRobot.UpdateServo;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.commands.Command;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;
import virtualRobot.hardware.ColorSensor;
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
import virtualRobot.utils.GlobalUtils;


/*
	In days of yore, forsaken souls were forced to make hundreds of thousands of line additions
	in UpdateThread to add a single motor.
	But then, a HERO emerged! Loyal subject of the house of Millburn FTC, ReflectionUpdateThread!
	Save us, RUT! You're our only hope! Wield your powerful blade of dynamic method invocation!
	Slay the evil spirits that cause such despair across the kingdom, verbosity and hardcoding!
	O ReflectionUpdateThread, prevail for the good of King Farrell, and humanity itself!
 */
public abstract class ReflectionUpdateThread extends OpMode {
	private SallyJoeBot robot;
	protected Class<? extends LogicThread> logicThread = null;
	private LogicThread t;
	private CreateVuforia cv;
	boolean tInstantiated= false;
	public static VuforiaLocalizerImplSubclass vuforiaInstance = null;
	private static ArrayList<Class<? extends LogicThread>> exceptions = new ArrayList<>();

	private ArrayList<Motor> vMotors = new ArrayList<>();
	private ArrayList<DcMotor> motors = new ArrayList<>();
	private ArrayList<virtualRobot.hardware.Servo> vServos = new ArrayList<>();
	private ArrayList<Servo> servos = new ArrayList<>();
	private ArrayList<Sensor> vSensors = new ArrayList<>();
	private ArrayList<HardwareDevice> sensors = new ArrayList<>();
	private ArrayList<DumbColorSensor> vDumbColorSensors = new ArrayList<>(); //This is so stupid lmao
	private ArrayList<com.qualcomm.robotcore.hardware.ColorSensor> dumbColorSensors = new ArrayList<>();
	private ArrayList<ContinuousRotationServo> vCRServos = new ArrayList<>();
	private ArrayList<CRServo> CRServos = new ArrayList<>();

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

//Now initiate the VIRTUAL componenents (from VirtualRobot!!), e.g. private Motor vDriveRightMotor, private virtualRobot.hardware.Servo ..., private Sensor vDriveRightMotorEncoder, private LocationSensor vLocationSensor

    private Sensor vVoltageSensor;
    private IMU vIMU;
    private StateSensor vStateSensor;
	private JoystickController vJoystickController1;
	private JoystickController vJoystickController2;

    private ElapsedTime runtime = new ElapsedTime();

	public ReflectionUpdateThread() {
		//Vuforia work-around
		super.msStuckDetectInit = Integer.MAX_VALUE;
	}

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

		//FETCH VIRTUAL ROBOT FROM COMMAND INTERFACE
		robot = Command.ROBOT;
		robot.initialBattery = getBatteryVoltage();

        //MOTOR SETUP (with physical componenents, e.g. leftBack = hardwareMap.dcMotor.get("leftBack")
		Class<SallyJoeBot> r = SallyJoeBot.class;
		Field[] fields = r.getDeclaredFields();
		for (Field f : fields) {
			String getter = "get" + f.getName().substring(0,1).toUpperCase() + f.getName().substring(1);
			if (f.getType().equals(Motor.class)) {
				UpdateMotor metadata = f.getAnnotation(UpdateMotor.class);
				if (metadata != null) {
					try {
						if (!metadata.enabled()) throw new Exception("Not enabled");
						Motor vMotor = (Motor) r.getDeclaredMethod(getter).invoke(robot);
						DcMotor motor = hardwareMap.dcMotor.get(metadata.name());
						if (metadata.zero() != DcMotor.ZeroPowerBehavior.UNKNOWN) motor.setZeroPowerBehavior(metadata.zero());
						motor.setMode(metadata.mode());
						motor.setDirection(metadata.direction());
						vMotor.setMotorType(motor.getMotorType());
						vMotors.add(vMotor);
						motors.add(motor);
						Log.d("Motor Load", "Successfully loaded motor " + f.getName());
					} catch (Exception e) {
						Log.d("Motor Load", "Failed to load motor " + f.getName() + ": " + e.getMessage());
					}

				} else {
					Log.d("Motor Load", "Motor " + f.getName() + " lacks annotation");
				}
			} else if (f.getType().equals(virtualRobot.hardware.Servo.class)) {
				UpdateServo metadata = f.getAnnotation(UpdateServo.class);
				if (metadata != null) {
					try {
						if (!metadata.enabled()) throw new Exception("Not enabled");
						virtualRobot.hardware.Servo vServo = (virtualRobot.hardware.Servo) r.getDeclaredMethod(getter).invoke(robot);
						Servo servo = hardwareMap.servo.get(metadata.name());
						servo.setPosition(metadata.initpos());
						vServos.add(vServo);
						servos.add(servo);
						Log.d("Servo Load", "Successfully loaded servo " + f.getName());
					} catch (Exception e) {
						Log.d("Servo Load", "Failed to load servo " + f.getName() + ": " + e.getMessage());
					}
				} else {
					Log.d("Servo Load", "Servo " + f.getName() + " lacks annotation");
				}
			} else if (f.getType().equals(ContinuousRotationServo.class)) {
				UpdateCRServo metadata = f.getAnnotation(UpdateCRServo.class);
				if (metadata != null) {
					try {
						if (metadata != null) throw new Exception("Not enabled");
						ContinuousRotationServo vCRServo = (ContinuousRotationServo) r.getDeclaredMethod(getter).invoke(robot);
						CRServo CRServo = hardwareMap.crservo.get(metadata.name());
						vCRServos.add(vCRServo);
						CRServos.add(CRServo);
						Log.d("CRServo Load", "Successfully loaded crservo " + f.getName());
					} catch (Exception e) {
						Log.d("CRServo Load", "Failed to load crservo " + f.getName() + ": " + e.getMessage());
					}
				} else {
					Log.d("CRServo Load", "CRServo " + f.getName() + "lacks annotation");
				}
			} else if (!f.getType().equals(DumbColorSensor.class) && f.getClass().isAssignableFrom(Sensor.class)) {
				UpdateSensor metadata = f.getAnnotation(UpdateSensor.class);
				if (metadata != null) {
					try {
						if (!metadata.enabled()) throw new Exception("Not enabled");
						Sensor vSensor = (Sensor) r.getDeclaredMethod(getter).invoke(robot);
						HardwareDevice sensor = hardwareMap.get(metadata.type(), metadata.name());
						vSensors.add(vSensor);
						sensors.add(sensor);
						Log.d("Sensor Load", "Successfully loaded sensor " + f.getName());
					} catch (Exception e) {
						Log.d("Sensor Load", "Failed to load sensor " + f.getName() + ": " + e.getMessage());
					}
				} else {
					Log.d("Sensor Load", "Sensor " + f.getName() + " lacks annotation");
				}
			} else if (f.getType().equals(DumbColorSensor.class)) {
				UpdateColorSensor metadata = f.getAnnotation(UpdateColorSensor.class);
				if (metadata != null) {
					try {
						if (!metadata.enabled()) throw new Exception("Not enabled");
						DumbColorSensor vDCS = (DumbColorSensor) r.getDeclaredMethod(getter).invoke(robot);
						com.qualcomm.robotcore.hardware.ColorSensor CS = hardwareMap.get(com.qualcomm.robotcore.hardware.ColorSensor.class, metadata.name());
						vDumbColorSensors.add(vDCS);
						dumbColorSensors.add(CS);
						Log.d("DCS Load", "Successfully loaded DCS " + f.getName());
					} catch (Exception e) {
						Log.d("DCS Load", "Failed to load DCS " + f.getName() + ": " + e.getMessage());
					}
				} else {
					Log.d("DCS Load", "DCS " + f.getName() + " lacks annotation");
				}
			}
		}

		//FETCH CONSTANT COMPONENTS OF VIRTUAL ROBOT (Do not touch)
        vJoystickController1 = robot.getJoystickController1();
        vJoystickController2 = robot.getJoystickController2();
        vVoltageSensor = robot.getVoltageSensor();
        vIMU = robot.getImu();
        vStateSensor = robot.getStateSensor();

        //Setup constant components (Do not touch)
		addPresets();
        setLogicThread();
//		cv = new CreateVuforia(LogicThread, vuforiaEverywhere, t);
//		new Thread (cv).start();
		if (!exceptions.contains(logicThread)){
			VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
			params.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
			params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
			ReflectionUpdateThread.vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
		}
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

        //Set virtual servo initial values
		for (int i = 0; i < servos.size(); i++) {
			vServos.get(i).setPosition(servos.get(i).getPosition());
		}

		//Set virtual voltage sensor value
        vVoltageSensor.setRawValue(getBatteryVoltage());

		//Copy to virtual generic sensors
		for (int i = 0; i < sensors.size(); i++) {
			vSensors.get(i).copyFrom(sensors.get(i));
		}

		//Copy to virtual color sensors
		for (int i = 0; i < dumbColorSensors.size(); i++) {
			DumbColorSensor dcs = vDumbColorSensors.get(i);
			com.qualcomm.robotcore.hardware.ColorSensor cs = dumbColorSensors.get(i);
			dcs.setRed(cs.red());
			dcs.setGreen(cs.green());
			dcs.setBlue(cs.blue());
		}

		//Copy to virtual motor encoders
		for (int i = 0; i < motors.size(); i++) {
			vMotors.get(i).setPosition(motors.get(i).getCurrentPosition());
		}

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

		//Copy voltage to virtual voltage sensor
        vVoltageSensor.setRawValue(getBatteryVoltage());

        try {
			telemetry.addData("Gamepad Stick Vals: ", gamepad1.left_stick_x + " " + gamepad1.left_stick_y);
            vJoystickController1.copyStates(gamepad1);
            vJoystickController2.copyStates(gamepad2);
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }

        //Copy to virtual motor encoders
        for (int i = 0; i < motors.size(); i++) {
			vMotors.get(i).setPosition(motors.get(i).getCurrentPosition());
		}

		//Copy to virtual sensors
		for (int i = 0; i < sensors.size(); i++) {
			vSensors.get(i).copyFrom(sensors.get(i));
		}

		//Copy to virtual color sensors
		for (int i = 0; i < dumbColorSensors.size(); i++) {
			DumbColorSensor dcs = vDumbColorSensors.get(i);
			com.qualcomm.robotcore.hardware.ColorSensor cs = dumbColorSensors.get(i);
			dcs.setRed(cs.red());
			dcs.setGreen(cs.green());
			dcs.setBlue(cs.blue());
		}

		//Set real servo positions
		for (int i = 0; i < servos.size(); i++) {
			servos.get(i).setPosition(vServos.get(i).getPosition());
		}

		//Set real motor speeds
		for (int i = 0; i < motors.size(); i++) {
			motors.get(i).setPower(vMotors.get(i).getPower());
		}

		//Set real CRServo speeds
		for (int i = 0; i < CRServos.size(); i++) {
			CRServos.get(i).setPower(vCRServos.get(i).getSpeed());
		}

		//Send real telemetry
		for (Map.Entry<String,Object> e: robot.getTelemetry().entrySet()) {
			telemetry.addData(e.getKey(),e.getValue());
		}
//		Log.d("Completed", "telemetry");

		//Send robot progress
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
		vuforiaInstance = null;
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
