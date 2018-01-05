package org.firstinspires.ftc.teamcode

import android.app.Activity
import android.util.Log

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.exception.RobotCoreException
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.VoltageSensor
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.util.ElapsedTime

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.firstinspires.ftc.robotcore.external.navigation.Position
import org.firstinspires.ftc.robotcore.external.navigation.Velocity
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer

import java.util.ArrayList
import java.util.Date

import virtualRobot.JoystickController
import virtualRobot.LogicThread
import virtualRobot.SallyJoeBot
import virtualRobot.VuforiaLocalizerImplSubclass
import virtualRobot.commands.Command
import virtualRobot.commands.Rotate
import virtualRobot.commands.Translate
import virtualRobot.hardware.ContinuousRotationServo
import virtualRobot.hardware.DumbColorSensor
import virtualRobot.hardware.IMU
import virtualRobot.hardware.Motor
import virtualRobot.hardware.Sensor
import virtualRobot.hardware.StateSensor
import virtualRobot.logicThreads.competition.TeleOpCustomLogic
import virtualRobot.logicThreads.competition.TeleOpCustomLogicRewrite
import virtualRobot.logicThreads.competition.TeleOpLogic
import virtualRobot.logicThreads.testing.TestBackendLogic
import virtualRobot.utils.BetterLog
import virtualRobot.utils.GlobalUtils
import virtualRobot.utils.Vector3f


/*
Updates Virtual sensors etc to corresponds to their real hardware.
Updates Real hardware (e.g. motors) to correspond to the values of their virtual componennts
 */
abstract class UpdateThread : OpMode() {
    private var robot: SallyJoeBot? = null
    protected var logicThread: Class<out LogicThread>? = null
    private var t: LogicThread? = null
    private val cv: CreateVuforia? = null
    internal var tInstantiated = false

    //here we will initiate all of our PHYSICAL hardware. E.g: private DcMotor leftBack...
    //also initiate sensors. E.g. private AnalogInput sonar, private ColorSensor colorSensor, private DigitalChannel ...

    //	private BNO055 imu;
    private val imu: BNO055IMU? = null
    private var leftFront: DcMotor? = null
    private var leftBack: DcMotor? = null
    private var rightFront: DcMotor? = null
    private var rightBack: DcMotor? = null
    private var liftLeft: DcMotor? = null
    private var liftRight: DcMotor? = null
    private var boxLeft: CRServo? = null
    private var boxRight: CRServo? = null
    private var rollerLeft: DcMotor? = null
    private var rollerRight: CRServo? = null
    private var relicArmWinch: CRServo? = null
    private var jewelServo: Servo? = null
    private var relicArmWrist: Servo? = null
    private var relicArmClaw: Servo? = null
    private var colorSensor: ColorSensor? = null

    //Now initiate the VIRTUAL componenents (from VirtualRobot!!), e.g. private Motor vDriveRightMotor, private virtualRobot.hardware.Servo ..., private Sensor vDriveRightMotorEncoder, private LocationSensor vLocationSensor

    private var vVoltageSensor: Sensor? = null
    private var vIMU: IMU? = null
    private var vStateSensor: StateSensor? = null
    private var vJoystickController1: JoystickController? = null
    private var vJoystickController2: JoystickController? = null

    private var vLeftFront: Motor? = null
    private var vLeftBack: Motor? = null
    private var vRightFront: Motor? = null
    private var vRightBack: Motor? = null
    private var vLiftLeft: Motor? = null
    private var vLiftRight: Motor? = null
    private var vRollerLeft: Motor? = null
    private var vRollerRight: ContinuousRotationServo? = null
    private var vRelicArm: Motor? = null
    private var vRelicArmWinch: ContinuousRotationServo? = null
    private var vJewelServo: virtualRobot.hardware.Servo? = null
    private var vRelicArmWrist: virtualRobot.hardware.Servo? = null
    private var vRelicArmClaw: virtualRobot.hardware.Servo? = null


    private var vBoxLeft: ContinuousRotationServo? = null
    private var vBoxRight: ContinuousRotationServo? = null
    private var vColorSensor: DumbColorSensor? = null

    private val runtime = ElapsedTime()

    private val batteryVoltage: Double
        get() {
            var result = java.lang.Double.POSITIVE_INFINITY
            for (sensor in hardwareMap.voltageSensor) {
                val voltage = sensor.voltage
                if (voltage > 0) {
                    result = Math.min(result, voltage)
                }
            }
            return result
        }

    init {
        //Vuforia work-around
        super.msStuckDetectInit = Integer.MAX_VALUE
    }

    override fun init() {
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
        leftFront = hardwareMap.dcMotor.get("leftFront")
        leftBack = hardwareMap.dcMotor.get("leftBack")
        rightFront = hardwareMap.dcMotor.get("rightFront")
        rightBack = hardwareMap.dcMotor.get("rightBack")
        Log.d("Components ", "Motors Initialized")
        rollerLeft = hardwareMap.dcMotor.get("rollerLeft")
        rollerRight = hardwareMap.crservo.get("rollerRight")
        boxLeft = hardwareMap.crservo.get("boxLeft")
        boxRight = hardwareMap.crservo.get("boxRight")
        liftLeft = hardwareMap.dcMotor.get("liftLeft")
        liftRight = hardwareMap.dcMotor.get("liftRight")
        relicArmWinch = hardwareMap.crservo.get("relicArmWinch")
        //		relicArm = hardwareMap.dcMotor.get("relicArm");
        //
        //        //SERVO SETUP (with physical hardware, e.g. servo = hardwareMap....)
        //		relicArmWinch = hardwareMap.servo.get("relicArmWinch");
        jewelServo = hardwareMap.servo.get("jewelArm")
        relicArmWrist = hardwareMap.servo.get("relicArmWrist")
        relicArmClaw = hardwareMap.servo.get("relicArmClaw")

        //REVERSE ONE SIDE (If needed, e.g. rightFront.setDirection(DcMotor.Direction.REVERSE)
        rightFront!!.direction = DcMotorSimple.Direction.REVERSE
        rightBack!!.direction = DcMotorSimple.Direction.REVERSE
        liftLeft!!.direction = DcMotorSimple.Direction.REVERSE

        //SET MOTOR MODES
        //		leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //		leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //		rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //		rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftLeft!!.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        liftRight!!.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightBack!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rightFront!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        leftBack!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        leftFront!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        liftLeft!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        liftRight!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        //SENSOR SETUP e.g. colorSensor = hardwareMap.colorsensor.get("color"), sonar1 = hardwareMap.analogInput.get("sonar1"), liftEndStop1 = hardwareMap.digitalChannel.get("liftEndStop1")
        colorSensor = hardwareMap.get<ColorSensor>(ColorSensor::class.java!!, "colorSensor")

        //FETCH VIRTUAL ROBOT FROM COMMAND INTERFACE
        robot = Command.ROBOT
        robot!!.initialBattery = batteryVoltage
        jewelServo!!.position = 0.45
        relicArmWrist!!.position = 0.0 //TODO: Tune init value
        relicArmClaw!!.position = 0.0 //TODO Tune init value

        //FETCH CONSTANT COMPONENTS OF VIRTUAL ROBOT (Do not touch)
        vJoystickController1 = robot!!.joystickController1
        vJoystickController2 = robot!!.joystickController2
        vVoltageSensor = robot!!.voltageSensor
        vIMU = robot!!.imu
        vStateSensor = robot!!.stateSensor

        //FETCH VIRTUAL COMPONENTS OF VIRTUAL ROBOT from robot. E.g. vDriveLeftMotor = robot.getDriveLeftMotor();
        vLeftFront = robot!!.lfMotor
        vLeftBack = robot!!.lbMotor
        vRightFront = robot!!.rfMotor
        vRightBack = robot!!.rbMotor
        vBoxLeft = robot!!.boxLeft
        vBoxRight = robot!!.boxRight
        vJewelServo = robot!!.jewelServo
        vRelicArmWrist = robot!!.relicArmWrist
        vRelicArmClaw = robot!!.relicArmClaw
        vRelicArm = robot!!.relicArm
        vRelicArmWinch = robot!!.relicArmWinch
        vRollerLeft = robot!!.rollerLeft
        vRollerRight = robot!!.rollerRight
        vLiftLeft = robot!!.liftLeft
        vLiftRight = robot!!.liftRight

        //FETCH VIRTUAL SENSORS OF VIRTUAL ROBOT from robot. E.g. vColorSensor = robot.getColorSensor();
        vColorSensor = robot!!.colorSensor

        //Set Motor Types
        vLeftFront!!.motorType = leftFront!!.motorType
        vLeftBack!!.motorType = leftBack!!.motorType
        vRightFront!!.motorType = rightFront!!.motorType
        vRightBack!!.motorType = rightBack!!.motorType
        vRollerLeft!!.motorType = rollerLeft!!.motorType
        //		vRollerRight.setMotorType(rollerRight.getMotorType());
        //		vRelicArm.setMotorType(relicArm.getMotorType());

        //Setup constant components (Do not touch)
        addPresets()
        setLogicThread()
        //		cv = new CreateVuforia(LogicThread, vuforiaEverywhere, t);
        //		new Thread (cv).start();
        if (!exceptions.contains(logicThread)) {
            val params = VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId)
            params.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT
            params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0"
            UpdateThread.vuforiaInstance = VuforiaLocalizerImplSubclass(params)
        }
    }

    override fun init_loop() {
        telemetry.addData("Is Running Version: ", Translate.KPt.toString() + " 3.0")
        telemetry.addData("Init Loop Time", runtime.toString())
        telemetry.addData("Battery Voltage: ", batteryVoltage)
        //        telemetry.addData("IMU Status", imu.getSystemStatus().toShortString());
        //        telemetry.addData("IMU Calibration", imu.getCalibrationStatus().toString());
        telemetry.addData("Is Good for Testing: ", if (batteryVoltage < 13.5) "NO, BATTERY IS TOO LOW" else "YES")
    }

    override fun start() {
        //set constants (Do not touch)
        robot!!.initialBattery = vVoltageSensor!!.rawValue
        GlobalUtils.setCurrentActivity(hardwareMap.appContext as Activity)
        Rotate.setCurrentAngle(0.0)

        //Set initial servo positions
        //		relicArmWinch.setPosition(0.5);
        //		clawLeft.setPosition(0);
        //		clawRight.setPosition(0);

        //Copy positions to virtualRobot
        //		vRelicArmWinch.setSpeed(relicArmWinch.getPosition());
        vJewelServo!!.position = jewelServo!!.position
        vRelicArmWrist!!.position = relicArmWrist!!.position
        vRelicArmClaw!!.position = relicArmClaw!!.position
        //set sensors e.g. vDriveRightMotorEncoder.setRawValue(-rightFront.getCurrentPosition())
        vVoltageSensor!!.rawValue = batteryVoltage

        //		vColorSensor.setRed(colorSensor.red());
        //		vColorSensor.setBlue(colorSensor.blue());
        //		vColorSensor.setGreen(colorSensor.green());
        val r = colorSensor!!.red()
        val g = colorSensor!!.green()
        val b = colorSensor!!.blue()
        println(r + g + b)

        vLeftFront!!.position = leftFront!!.currentPosition
        vLeftBack!!.position = leftBack!!.currentPosition
        vRightFront!!.position = rightFront!!.currentPosition
        vRightBack!!.position = rightBack!!.currentPosition
        vRollerLeft!!.position = rollerLeft!!.currentPosition
        //		vRollerRight.setPosition(rollerRight.getCurrentPosition());
        //		vRelicArm.setPosition(relicArm.getCurrentPosition());

        try {
            t = logicThread!!.newInstance()
            tInstantiated = true
        } catch (e: InstantiationException) {
            tInstantiated = false
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            tInstantiated = false
        } catch (e: NullPointerException) {
            tInstantiated = false
        }

        if (tInstantiated)
            t!!.start()
        //		telemetry.addData("Started Logic ", t.isAlive());
        Thread.currentThread().priority = 10
        runtime.reset()
    }

    override fun loop() {
        telemetry.addData("Update timestamp: ", System.currentTimeMillis())
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

        vVoltageSensor!!.rawValue = batteryVoltage

        try {
            telemetry.addData("Gamepad Stick Vals: ", gamepad1.left_stick_x.toString() + " " + gamepad1.left_stick_y)
            vJoystickController1!!.copyStates(gamepad1)
            vJoystickController2!!.copyStates(gamepad2)
        } catch (e: RobotCoreException) {
            e.printStackTrace()
        }

        // Update Sensor Values E.g. vPitchSensor.setRawValue(imu.getIntegratedPitch()); vHeadingSensor, vRollSensor, vColorSensor...
        vLeftFront!!.position = leftFront!!.currentPosition
        vLeftBack!!.position = leftBack!!.currentPosition
        vRightFront!!.position = rightFront!!.currentPosition
        vRightBack!!.position = rightBack!!.currentPosition
        vLiftLeft!!.position = liftLeft!!.currentPosition
        vLiftRight!!.position = liftRight!!.currentPosition
        vRollerLeft!!.position = rollerLeft!!.currentPosition
        //		Log.d("Completed", "virtual encoders");

        vColorSensor!!.red = colorSensor!!.red()
        vColorSensor!!.blue = colorSensor!!.blue()
        vColorSensor!!.green = colorSensor!!.green()
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
        boxLeft!!.power = vBoxLeft!!.speed
        boxRight!!.power = vBoxRight!!.speed
        jewelServo!!.position = vJewelServo!!.position
        relicArmWrist!!.position = vRelicArmWrist!!.position
        relicArmClaw!!.position = vRelicArmClaw!!.position
        //		Log.d("Completed", "servos");

        // Capture Motor Powers,E.g. double leftPower = vDriveLeftMotore.getPower();
        val leftFrontPower = vLeftFront!!.power
        val leftBackPower = vLeftBack!!.power
        val rightFrontPower = vRightFront!!.power
        val rightBackPower = vRightBack!!.power
        val relicArmPower = vRelicArm!!.power
        val rollerLeftPower = vRollerLeft!!.power
        val rollerRightPower = vRollerRight!!.speed
        val relicArmWinchPower = vRelicArmWinch!!.speed
        val liftLeftPower = vLiftLeft!!.power
        val liftRightPower = vLiftRight!!.power
        //		Log.d("Completed", "powers");

        // Copy State of Motors and Servos E.g. leftFront.setPower(leftPower), Servo.setPosition(vServo.getPosition());
        leftFront!!.power = leftFrontPower
        leftBack!!.power = leftBackPower
        rightFront!!.power = rightFrontPower
        rightBack!!.power = rightBackPower
        rollerLeft!!.power = rollerLeftPower
        rollerRight!!.power = rollerRightPower
        liftLeft!!.power = liftLeftPower
        liftRight!!.power = liftRightPower
        relicArmWinch!!.power = relicArmWinchPower
        //		Log.d("Completed", "motor powers");
        //		relicArm.setPower(relicArmPower);
        //		rollerLeft.setPower(rollerLeftPower);
        //		rollerRight.setPower(rollerRightPower);

        for ((key, value) in robot!!.telemetry) {
            telemetry.addData(key, value)
        }
        //		Log.d("Completed", "telemetry");

        for (i in 0 until robot!!.progress.size) {
            telemetry.addData("robot progress " + i, robot!!.progress[i])
        }

        //		telemetry.addData("Gamepad"gamepad1.atRest());
        telemetry.addData("Logic is Alive: ", t!!.isAlive)
        telemetry.addData("All is Alive", t!!.allIsAlive())
        //telemetry.addData("Motor: ", robot.getLFMotor().toString());
        telemetry.addData("Loop Time: ", runtime.toString())
    }

    override fun stop() {
        //        imu.stopAccelerationIntegration();
        //		imu.close();
        vuforiaInstance = null
        if (tInstantiated)
            t!!.interrupt()
        System.gc()
    }

    abstract fun setLogicThread()

    fun addPresets() {}

    companion object {
        var vuforiaInstance: VuforiaLocalizerImplSubclass? = null
        private val exceptions = ArrayList<Class<out LogicThread>>()

        //Here we add all the exception logic threads that are exempt from vuforia initialization
        //This just helps to lessen the time needed to test
        init {
            exceptions.add(TestBackendLogic::class.java)
            exceptions.add(TeleOpCustomLogic::class.java)
            exceptions.add(TeleOpLogic::class.java)
            exceptions.add(TeleOpCustomLogicRewrite::class.java)
        }
    }
}
