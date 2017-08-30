package virtualRobot.commands;

import virtualRobot.Condition;
import virtualRobot.PIDController;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Sensor;
import virtualRobot.utils.MathUtils;

/**
 * Created by ethachu19 on 2/4/17.
 */
public class MoveMotorPID extends Command {
    public final static double MAX_SPEED = 100;

    private double speed;
    Motor motor;
    Sensor encoder;
    private double lastTime;
    private double lastSpeed;
    private double lastEncoder;
    private double currPower = 0;
    private double KP, KI, KD;
    private double PPC, MSC;
   private double maxAS = .06;

    Condition condition = new Condition() {
        @Override
        public boolean isConditionMet() {
            return false;
        }
    };

    //0.0000000000005
    //37.5, 50

    //KU = 48.5
    //TU =
    PIDController speedController;

    //PIDController speedController = new PIDController(29.1,.05197,4073.191,0);
    public MoveMotorPID(double speed, Motor motor, Sensor encoder) {
        maxAS = motor.getMotorType().getMaxActSpeed();
        this.speed = getMappedSpeed(MathUtils.clamp(speed, 0, 100));
        this.motor = motor;
        this.encoder = encoder;
        lastTime = System.currentTimeMillis();
        lastSpeed = 0;
        lastEncoder = encoder.getRawValue();
        KP = motor.getMotorType().getKP();
        KI = motor.getMotorType().getKI();
        KD = motor.getMotorType().getKD();
        PPC = motor.getMotorType().getPPC();
        MSC = motor.getMotorType().getMSC();
        speedController = new PIDController(KP,KI,KD,0);
        speedController.setTarget(this.speed);

    }

    public void addCondition(Condition condition) {
        this.condition = condition;
    }

    //@Deprecated :P
    private double getMotorSpeed() {
        double timeDiff = System.currentTimeMillis() - lastTime;
        double encoderDiff = encoder.getRawValue() - lastEncoder;
        if (timeDiff > 5 && encoderDiff > 20) {
            lastSpeed = encoderDiff / timeDiff * 1000;
            lastEncoder = encoder.getRawValue();
            lastTime = System.currentTimeMillis();
        }
        return lastSpeed;
    }
    private double getSpeedOfRev() {
        double a = encoder.getRawValue()-lastEncoder;
        //Command.ROBOT.addToTelemetry("DIF: ", a);

        if (System.currentTimeMillis() - lastTime > MSC ) { //1780 RPM = 333 milliseconds/cycle
            //if (!UpdateThread.allDone)
            //Log.d("MotorPID", String.valueOf(currPower));
            //Command.ROBOT.addToTelemetry("PENS: ", "Lock");

            double time = lastTime;
            lastEncoder = encoder.getRawValue();
            lastTime = System.currentTimeMillis();
            return (a/PPC)/(System.currentTimeMillis()-time); //25.9 pulses per cycle
        }
       // Command.ROBOT.addToTelemetry("PENS: ", "Ban");

        return lastSpeed;
    }

    //shuyd
    @Override
    public boolean changeRobotState() throws InterruptedException {
        double motorSpeed;
        boolean isInterrupted = false;
        MainLoop: while (!isInterrupted) {
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
           lastSpeed = getSpeedOfRev();
            //Command.ROBOT.addToTelemetry("Speed: ", lastSpeed);


//            if (Double.isNaN(motorSpeed))
//                continue;
            double oldPower = currPower;
            currPower = speedController.getPIDOutput(lastSpeed);
            currPower = MathUtils.clamp(currPower, -1, 1);
            //Command.ROBOT.addToTelemetry("MotorPID: ", currPower);

            //Log.d("MotorPID", speed + " " + currPower + " " + lastSpeed + " " + encoder.getRawValue());
            motor.setPower(currPower == 0 ? oldPower : currPower);

        }
        motor.setPower(0);

        return isInterrupted;
    }
    private double getMappedSpeed(double speed) {
        return (maxAS/100)*speed;
    }
}
