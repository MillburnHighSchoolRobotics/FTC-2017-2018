package virtualRobot;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Retrofit;
import virtualRobot.hardware.ContinuousRotationServo;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.IMU;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Sensor;
import virtualRobot.hardware.Servo;
import virtualRobot.hardware.StateSensor;
import virtualRobot.telemetry.CTelemetry;
import virtualRobot.telemetry.MatConverterFactory;

/**
 * Created by DOSullivan on 9/14/16.
 * All of our our virtual hardware and there getters are housed in SallyJoeBot
 */
public class SallyJoeBot {
    //Robot Constants
    public volatile double initialBattery;
    public final double wheelDiameter = 5;
    public final double botWidth = 5;
    public final double botLength = 5;

    //Data to pass to UpdateThread
    private ArrayList<String> robotProgress;
    private ConcurrentHashMap<String, Object> telemetry;

    //Motors and Servos
    @UpdateMotor(name = "leftFront")
    private Motor LFMotor;
    @UpdateMotor(name = "leftBack")
    private Motor LBMotor;
    @UpdateMotor(name = "rightFront", direction = DcMotorSimple.Direction.REVERSE)
    private Motor RFMotor;
    @UpdateMotor(name = "rightBack", direction = DcMotorSimple.Direction.REVERSE)
    private Motor RBMotor;
    @UpdateMotor(name = "rollerLeft", enabled = true)
    private Motor rollerLeft;
    @UpdateMotor(name = "rollerRight", direction = DcMotorSimple.Direction.REVERSE, enabled = true)
    private Motor rollerRight;
    @UpdateMotor(name = "lift", enabled = true)
    private Motor lift;
    @UpdateServo(name = "flipper", enabled = true)
    private Servo flipper;
    @UpdateServo(name = "rollerLiftLeft", enabled = false)
    private Servo rollerLiftLeft;
    @UpdateServo(name = "rollerLiftRight", enabled = false)
    private Servo rollerLiftRight;
    @UpdateMotor(name = "relicArmWinch", direction = DcMotorSimple.Direction.REVERSE, enabled = false)
    private Motor relicArmWinch;
    @UpdateCRServo(name = "relicArmWrist", enabled = false)
    private ContinuousRotationServo relicArmWrist;
    @UpdateServo(name = "relicArmClaw", enabled = false)
    private Servo relicArmClaw;
    @UpdateColorSensor(name = "jewelColorSensor", enabled = false)
    private DumbColorSensor colorSensor;
    @UpdateServo(name = "jewelArm", enabled = false)
    private Servo jewelServo;
    @UpdateServo(name = "jewelHitter", initpos = 1.2, enabled = false)
    private Servo jewelHitter;
    @UpdateServo(name = "relicArmRotater", initpos = 0.5, enabled = false)
    private Servo relicArmRotater;
    @UpdateServo(name = "phoneServo", enabled = false)
    private Servo phoneServo;
    //Sensors
    private IMU imu;
    private Sensor voltageSensor;
    private JoystickController joystickController1, joystickController2;
    private StateSensor stateSensor;

    //CTelemetry
    private CTelemetry ctel;
    private final String ipaddr = "http://172.20.95.158:8080/";

    //Motors, sensors, servos instantiated (e.g Motor = new Motor(), some positions can also be set if desired
    public SallyJoeBot() {
        joystickController1 = new JoystickController();
        joystickController2 = new JoystickController();
        voltageSensor = new Sensor();
        robotProgress = new ArrayList<String>();
        telemetry = new ConcurrentHashMap<>();
        stateSensor = new StateSensor();
        imu = new IMU();

        LFMotor = new Motor();
        LBMotor = new Motor();
        RFMotor = new Motor();
        RBMotor = new Motor();

        lift = new Motor();
        flipper = new Servo();
        rollerLiftLeft = new Servo();
        rollerLiftRight = new Servo();

        rollerLeft = new Motor();
        rollerRight = new Motor();

        relicArmWinch = new Motor();
        relicArmWrist = new ContinuousRotationServo();
        relicArmClaw = new Servo();
        relicArmRotater = new Servo();

        jewelServo = new Servo();
        jewelHitter = new Servo();
        colorSensor = new DumbColorSensor();
        //capLift = new SyncedMotors(LiftLeftMotor, LiftRightMotor, LiftLeftEncoder, LiftRightEncoder, KP, KI, KD, SyncedMotors.SyncAlgo.POSITION);
        //capLift.setRatio(1);

    }
    //All of Autonomous and TeleopRobot's functions are created e.g. (public synchronized Motor getMotor() {return Motor;}

    public synchronized DumbColorSensor getColorSensor() {
        return colorSensor;
    }

    public synchronized Sensor getVoltageSensor() {
        return voltageSensor;
    }

    public synchronized IMU getImu() {
        return imu;
    }

    public synchronized Motor getLFMotor() {
        return LFMotor;
    }

    public synchronized Motor getLBMotor() {
        return LBMotor;
    }

    public synchronized Motor getRFMotor() {
        return RFMotor;
    }

    public synchronized Motor getRBMotor() {
        return RBMotor;
    }

    public synchronized Motor getRelicArmWinch() {
        return relicArmWinch;
    }

    public synchronized ContinuousRotationServo getRelicArmWrist() {
        return relicArmWrist;
    }

    public synchronized Servo getRelicArmClaw() {
        return relicArmClaw;
    }

    public synchronized Servo getJewelServo() {
        return jewelServo;
    }

    public synchronized StateSensor getStateSensor() {
        return stateSensor;
    }

    public synchronized void stopMotors() {
        LFMotor.setPower(0);
        RFMotor.setPower(0);
        LBMotor.setPower(0);
        RBMotor.setPower(0);
    }

    public synchronized void moveRollerLifts(boolean isOpen) {
        rollerLiftLeft.setPosition(isOpen ? 0 : 1);
        rollerLiftRight.setPosition(isOpen ? 1 : 0);
    }

    public synchronized JoystickController getJoystickController1() {
        return joystickController1;
    }

    public synchronized JoystickController getJoystickController2() {
        return joystickController2;
    }

    public synchronized void addToProgress(String s) {
        robotProgress.add(s);
    }

    public synchronized ArrayList<String> getProgress() {
        return robotProgress;
    }

    public synchronized void setRollerPower(double power) {
        rollerLeft.setPower(power);
        rollerRight.setPower(power);
    }

    public synchronized void addToTelemetry(String s, Object arg) {
        telemetry.put(s, arg);
    }

    public synchronized ConcurrentHashMap<String, Object> getTelemetry() {
        return telemetry;
    }

    public synchronized void initCTelemetry() {
        ctel = new Retrofit.Builder()
                .baseUrl(ipaddr)
                .addConverterFactory(MatConverterFactory.create())
                .build()
                .create(CTelemetry.class);
    }

    public synchronized CTelemetry getCTelemetry() {
        return ctel;
    }

    public Servo getJewelHitter() {
        return jewelHitter;
    }

    public Servo getRelicArmRotater() {
        return relicArmRotater;
    }

    public Motor getRollerRight() {
        return rollerRight;
    }

    public Motor getRollerLeft() {
        return rollerLeft;
    }

    public Motor getLift() {
        return lift;
    }

    public Servo getFlipper() {
        return flipper;
    }

    public Servo getRollerLiftLeft() {
        return rollerLiftLeft;
    }

    public Servo getRollerLiftRight() {
        return rollerLiftRight;
    }

    public Servo getPhoneServo() {
        return phoneServo;
    }

    public enum Team {
        BLUE, RED
    }
}
