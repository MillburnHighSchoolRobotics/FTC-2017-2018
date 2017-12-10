package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import virtualRobot.utils.GlobalUtils;

/**
 * Created by Ethan Mak on 12/2/2017.
 */

public abstract class ReflectionUpdateThread extends OpMode {
    private SallyJoeBot robot;
    protected Class<? extends LogicThread> logicThread = null;
    private LogicThread t;
    private CreateVuforia cv;
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
    private DcMotor liftLeft, liftRight;
    private CRServo boxLeft, boxRight;
    private Servo clawLeft, clawRight;
    private CRServo rollerLeft, rollerRight;
    private Servo jewelServo;
    private ColorSensor colorSensor;

//Now initiate the VIRTUAL componenents (from VirtualRobot!!), e.g. private Motor vDriveRightMotor, private virtualRobot.hardware.Servo ..., private Sensor vDriveRightMotorEncoder, private LocationSensor vLocationSensor

    private Sensor vVoltageSensor;
    private IMU vIMU;
    private StateSensor vStateSensor;
    private JoystickController vJoystickController1;
    private JoystickController vJoystickController2;

    private Motor vLeftFront, vLeftBack, vRightFront, vRightBack;
    private Motor vLiftLeft, vLiftRight;
    private ContinuousRotationServo vRollerLeft, vRollerRight;
    private Motor vRelicArm;
    private ContinuousRotationServo vRelicArmWinch;
    private virtualRobot.hardware.Servo vJewelServo;
    private virtualRobot.hardware.Servo vClawLeft, vClawRight;


    private ContinuousRotationServo vBoxLeft, vBoxRight;
    private DumbColorSensor vColorSensor;

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

        //MOTOR SETUP (with physical componenents, e.g. leftBack = hardwareMap.dcMotor.get("leftBack")
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        Log.d("Components ", "Motors Initialized");
        rollerLeft = hardwareMap.crservo.get("rollerLeft");
        rollerRight = hardwareMap.crservo.get("rollerRight");
        boxLeft = hardwareMap.crservo.get("boxLeft");
        boxRight = hardwareMap.crservo.get("boxRight");
        liftLeft = hardwareMap.dcMotor.get("liftLeft");
        liftRight = hardwareMap.dcMotor.get("liftRight");
//		relicArm = hardwareMap.dcMotor.get("relicArm");
//
//        //SERVO SETUP (with physical hardware, e.g. servo = hardwareMap....)
//		relicArmWinch = hardwareMap.servo.get("relicArmWinch");
        jewelServo = hardwareMap.servo.get("jewelArm");
        clawLeft = hardwareMap.servo.get("clawLeft");
        clawRight = hardwareMap.servo.get("clawRight");

        //REVERSE ONE SIDE (If needed, e.g. rightFront.setDirection(DcMotor.Direction.REVERSE)
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        //SET MOTOR MODES
//		leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //SENSOR SETUP e.g. colorSensor = hardwareMap.colorsensor.get("color"), sonar1 = hardwareMap.analogInput.get("sonar1"), liftEndStop1 = hardwareMap.digitalChannel.get("liftEndStop1")
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        //FETCH VIRTUAL ROBOT FROM COMMAND INTERFACE
        robot = Command.ROBOT;
        robot.initialBattery = getBatteryVoltage();
        clawLeft.setPosition(0);
        clawRight.setPosition(1);
        jewelServo.setPosition(0);

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
        vBoxLeft = robot.getBoxLeft();
        vBoxRight = robot.getBoxRight();
        vJewelServo = robot.getJewelServo();
        vRelicArm = robot.getRelicArm();
        vRelicArmWinch = robot.getRelicArmWinch();
        vRollerLeft = robot.getRollerLeft();
        vRollerRight = robot.getRollerRight();
        vClawLeft = robot.getClawLeft();
        vClawRight = robot.getClawRight();
        vLiftLeft = robot.getLiftLeft();
        vLiftRight = robot.getLiftRight();

        //FETCH VIRTUAL SENSORS OF VIRTUAL ROBOT from robot. E.g. vColorSensor = robot.getColorSensor();
        vColorSensor = robot.getColorSensor();
        try {
            Class<?> c = this.getClass();
            Field[] fields = c.getDeclaredFields();
            String name;
            for (Field f : fields) {
                name = f.getName();
                if (name.charAt(0) == 'v' && Character.isUpperCase(name.charAt(1))) {
                    f.set(this, SallyJoeBot.class.getMethod("get" + name.substring(1)).invoke(null));
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

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
        if (!exceptions.contains(logicThread)){
            VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
            params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
            UpdateThread.vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
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

        //Set initial servo positions
//		relicArmWinch.setPosition(0.5);
//		clawLeft.setPosition(0);
//		clawRight.setPosition(0);

        //Copy positions to virtualRobot
//		vRelicArmWinch.setSpeed(relicArmWinch.getPosition());
        vClawLeft.setPosition(clawLeft.getPosition());
        vClawRight.setPosition(clawRight.getPosition());
        vJewelServo.setPosition(jewelServo.getPosition());

        //set sensors e.g. vDriveRightMotorEncoder.setRawValue(-rightFront.getCurrentPosition())
        vVoltageSensor.setRawValue(getBatteryVoltage());

//		vColorSensor.setRed(colorSensor.red());
//		vColorSensor.setBlue(colorSensor.blue());
//		vColorSensor.setGreen(colorSensor.green());
        int r = colorSensor.red();
        int g = colorSensor.green();
        int b = colorSensor.blue();
        System.out.println(r + g + b);

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
        vLiftLeft.setPosition(liftLeft.getCurrentPosition());
        vLiftRight.setPosition(liftRight.getCurrentPosition());
//		Log.d("Completed", "virtual encoders");

        vColorSensor.setRed(colorSensor.red());
        vColorSensor.setBlue(colorSensor.blue());
        vColorSensor.setGreen(colorSensor.green());
//		int r = colorSensor.red();
//		int g = colorSensor.green();
//		int b = colorSensor.blue();
//		System.out.println(r + g + b);
//		Log.d("Completed", "color sensor");

//		vRollerLeft.setPosition(rollerLeft.getCurrentPosition());
//		vRollerRight.setPosition(rollerRight.getCurrentPosition());
//		vRelicArm.setPosition(relicArm.getCurrentPosition());

        //Copy Servo Positions
//		relicArmWinch.setPosition(vRelicArmWinch.getSpeed());
        clawLeft.setPosition(vClawLeft.getPosition());
        clawRight.setPosition(vClawRight.getPosition());
        boxLeft.setPower(vBoxLeft.getSpeed());
        boxRight.setPower(vBoxRight.getSpeed());
        jewelServo.setPosition(vJewelServo.getPosition());
//		Log.d("Completed", "servos");

        // Capture Motor Powers,E.g. double leftPower = vDriveLeftMotore.getPower();
        double leftFrontPower = vLeftFront.getPower();
        double leftBackPower = vLeftBack.getPower();
        double rightFrontPower = vRightFront.getPower();
        double rightBackPower = vRightBack.getPower();
        double relicArmPower = vRelicArm.getPower();
        double rollerLeftPower = vRollerLeft.getSpeed();
        double rollerRightPower = vRollerRight.getSpeed();
        double liftLeftPower = vLiftLeft.getPower();
        double liftRightPower = vLiftRight.getPower();
//		Log.d("Completed", "powers");

        // Copy State of Motors and Servos E.g. leftFront.setPower(leftPower), Servo.setPosition(vServo.getPosition());
        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);
        rollerLeft.setPower(rollerLeftPower);
        rollerRight.setPower(rollerRightPower);
        liftLeft.setPower(liftLeftPower);
        liftRight.setPower(liftRightPower);
//		Log.d("Completed", "motor powers");
//		relicArm.setPower(relicArmPower);
//		rollerLeft.setPower(rollerLeftPower);
//		rollerRight.setPower(rollerRightPower);

        for (Map.Entry<String,Object> e: robot.getTelemetry().entrySet()) {
            telemetry.addData(e.getKey(),e.getValue());
        }
//		Log.d("Completed", "telemetry");

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
