package virtualRobot;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import virtualRobot.hardware.AxisSensor;
import virtualRobot.hardware.ColorSensor;
import virtualRobot.hardware.ContinuousRotationServo;
import virtualRobot.hardware.StateSensor;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Sensor;
import virtualRobot.hardware.Servo;
import virtualRobot.hardware.SyncedMotors;
import virtualRobot.hardware.UltrasonicSensor;

/**
 * Created by DOSullivan on 9/14/16.
 * All of our our virtual hardware and there getters are housed in SallyJoeBot
 */
public class SallyJoeBot {
    //Motors, sensors, servos referenced (e.g. private Motor...)
    private Sensor headingSensor, pitchSensor, rollSensor;
    private Sensor voltageSensor;
    private AxisSensor rawAccel, worldAccel;
    private JoystickController joystickController1, joystickController2;
    private StateSensor stateSensor;
    private ArrayList<String> robotProgress;
    private ConcurrentHashMap<String, Object> telemetry;
    private Motor LFMotor, LBMotor, RFMotor, RBMotor;
    private Servo ButtonServo;
    private Servo FlywheelStopper;
    public double initialBattery;
    public final double wheelDiameter = 5;
    public final double botWidth = 5;
    public final double botLength = 5;

    //Motors, sensors, servos instantiated (e.g Motor = new Motor(), some positions can also be set if desired
    public SallyJoeBot() {
        rawAccel = new AxisSensor();
        worldAccel = new AxisSensor();
        joystickController1 = new JoystickController();
        joystickController2 = new JoystickController();
        headingSensor = new Sensor();
        pitchSensor = new Sensor();
        rollSensor = new Sensor();
        voltageSensor = new Sensor();
        robotProgress = new ArrayList<String>();
        telemetry = new ConcurrentHashMap<>();
        stateSensor = new StateSensor();
        LFMotor = new Motor();
        LBMotor = new Motor();
        RFMotor = new Motor();
        RBMotor = new Motor();
        ButtonServo = new Servo();
        FlywheelStopper = new Servo();
        //capLift = new SyncedMotors(LiftLeftMotor, LiftRightMotor, LiftLeftEncoder, LiftRightEncoder, KP, KI, KD, SyncedMotors.SyncAlgo.POSITION);
        //capLift.setRatio(1);

    }
    //All of Autonomous and TeleopRobot's functions are created e.g. (public synchronized Motor getMotor() {return Motor;}

    public synchronized Sensor getHeadingSensor() {
        return headingSensor;
    }

    public synchronized Sensor getPitchSensor() {
        return pitchSensor;
    }

    public synchronized Sensor getRollSensor() {
        return rollSensor;
    }

    public Sensor getVoltageSensor() { return voltageSensor; }

    public AxisSensor getWorldAccel() { return worldAccel; }

    public AxisSensor getRawAccel() { return rawAccel; }

    public synchronized Motor getLFMotor() { return LFMotor; }

    public synchronized Motor getLBMotor() { return LBMotor; }

    public synchronized Motor getRFMotor() { return RFMotor; }

    public synchronized Motor getRBMotor() { return RBMotor; }

    public synchronized Servo getButtonServo() { return ButtonServo; }

    public Servo getFlywheelStopper() { return FlywheelStopper; }

    public synchronized StateSensor getStateSensor() { return stateSensor; }

    public synchronized void stopMotors() {LFMotor.setPower(0); RFMotor.setPower(0); LBMotor.setPower(0); RBMotor.setPower(0);}

    public synchronized JoystickController getJoystickController1() {
        return joystickController1;
    }

    public synchronized JoystickController getJoystickController2() {
        return joystickController2;
    }

    public synchronized void addToProgress (String s) {
        robotProgress.add(s);
    }

    public synchronized ArrayList<String> getProgress () {
        return robotProgress;
    }

    public synchronized void addToTelemetry(String s, Object arg) { telemetry.put(s,arg); }

    public synchronized ConcurrentHashMap<String, Object> getTelemetry () { return telemetry; }


}
