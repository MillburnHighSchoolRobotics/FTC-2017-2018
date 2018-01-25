package virtualRobot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.opencv.core.Mat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
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
    @UpdateMotor(name = "leftFront", direction = DcMotorSimple.Direction.REVERSE, encoderReversed = true)
    private Motor LFMotor;
    @UpdateMotor(name = "leftBack", direction = DcMotorSimple.Direction.REVERSE, encoderReversed = true)
    private Motor LBMotor;
    @UpdateMotor(name = "rightFront", encoderReversed = true)
    private Motor RFMotor;
    @UpdateMotor(name = "rightBack", encoderReversed = true)
    private Motor RBMotor;
    @UpdateMotor(name = "lift")
    private Motor lift;
    @UpdateServo(name = "clawLeft", initpos = 1)
    private Servo clawLeft;
    @UpdateServo(name = "clawRight")
    private Servo clawRight;
    @UpdateMotor(name = "relicArmWinch", direction = DcMotorSimple.Direction.REVERSE)
    private Motor relicArmWinch;
    @UpdateServo(name = "relicArmWrist")
    private Servo relicArmWrist;
    @UpdateServo(name = "relicArmClaw")
    private Servo relicArmClaw;
    @UpdateColorSensor(name = "jewelColorSensor")
    private DumbColorSensor colorSensor;
    @UpdateServo(name = "jewelArm")
    private Servo jewelServo;
    @UpdateCRServo(name = "jewelHitter")
    private ContinuousRotationServo jewelHitter;
    //Sensors
    private IMU imu;
    private Sensor voltageSensor;
    private JoystickController joystickController1, joystickController2;
    private StateSensor stateSensor;

    //CTelemetry
    private CTelemetry ctel;
    private final String ipaddr = "http://172.20.95.207:8080/";

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
        clawLeft = new Servo();
        clawRight = new Servo();

        relicArmWinch = new Motor();
        relicArmWrist = new Servo();
        relicArmClaw = new Servo();

        jewelServo = new Servo();
        jewelHitter = new ContinuousRotationServo();
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

    public synchronized Motor getLift() {
        return lift;
    }

    public synchronized Servo getClawLeft() {
        return clawLeft;
    }

    public synchronized Servo getClawRight() {
        return clawRight;
    }

    public synchronized Motor getRelicArmWinch() {
        return relicArmWinch;
    }

    public synchronized Servo getRelicArmWrist() {
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

    public synchronized void moveClaw(boolean isOpen) {
        clawLeft.setPosition(isOpen ? 0.6 : 1);
        clawRight.setPosition(isOpen ? 0.4 : 0);
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

    public ContinuousRotationServo getJewelHitter() {
        return jewelHitter;
    }

    public enum Team {
        BLUE, RED
    }
}
