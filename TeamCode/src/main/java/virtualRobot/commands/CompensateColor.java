package virtualRobot.commands;

import android.util.Log;

import virtualRobot.SallyJoeBot;
import virtualRobot.Condition;
import virtualRobot.PIDController;

/**
 * Created by ethachu19 on 11/22/16.
 */

public class CompensateColor extends Command {
    SallyJoeBot robot = Command.ROBOT;
    PIDController lateral = new PIDController(1.22,0,0,0,0);
//    Direction direction = Direction.FORWARD;
    double referenceAngle;
    double timeLimit;
    double multiplier;
    private static final double whiteTape = 13;

    public CompensateColor() {
        referenceAngle = 0; timeLimit = 10000; multiplier=2.5;
    }
    public CompensateColor(double timeLimit) {
        this();
        this.timeLimit = timeLimit;
    }

    public CompensateColor(double timeLimit, double multiplier) {
        this(timeLimit);
        this.multiplier = multiplier;
    }

    public CompensateColor(double timeLimit, double multiplier, double referenceAngle) {
        this(timeLimit, multiplier);
        this.referenceAngle = referenceAngle;
    }

    @Override
    protected int activate(String s) {
        switch(s) {
            case "BREAK":
                return BREAK;
            case "END":
                return END;
        }
        return NO_CHANGE;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        boolean isInterrupted = false;
        PIDController rotation = new PIDController(0.008,0,0,0,referenceAngle);
        double lateralPower = 0, rotationPower = 0;
        double curr;
        double startTime = System.currentTimeMillis();
        boolean colorTriggered = false;
        MainLoop: while (!colorTriggered && !isInterrupted && System.currentTimeMillis() - startTime < timeLimit) {
            switch (checkConditionals()) {
                case BREAK:
                    break MainLoop;
                case END:
                    return isInterrupted;
            }

            curr = robot.getLightSensor1().getValue()*multiplier + robot.getLightSensor2().getValue() - robot.getLightSensor3().getValue() - robot.getLightSensor4().getValue()*(2.55);
            lateralPower = lateral.getPIDOutput(curr)*-1;// - pidController1.getPIDOutput(robot.getHeadingSensor().getValue());
            Log.d("CompensateColor", curr + " " + lateralPower);
            robot.addToTelemetry("CompensateColor: ", curr + " " + lateralPower);
            //rotationPower = rotation.getPIDOutput(robot.getHeadingSensor().getValue());
            rotationPower =0 ;
            robot.getLFMotor().setPower(lateralPower + rotationPower);
            robot.getLBMotor().setPower(lateralPower + rotationPower);
            robot.getRFMotor().setPower(lateralPower - rotationPower);
            robot.getRBMotor().setPower(lateralPower - rotationPower);

            if (Thread.currentThread().isInterrupted()) {
                isInterrupted = true;
                break;
            }
            if ((robot.getColorSensor().getRed() >= whiteTape && robot.getColorSensor().getBlue() >= whiteTape && robot.getColorSensor().getGreen() >= whiteTape && robot.getColorSensor().getBlue() < 255)) {
                colorTriggered = true;
                robot.addToProgress("Color Triggered while compensating");
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                isInterrupted = true;
                break;
            }
        }
        robot.stopMotors();
        return isInterrupted;
    }

//    public enum Direction {
//        FORWARD(1),
//        BACKWARD(-1);
//
//        private int i;
//        private Direction(int i) {
//            this.i = i;
//        }
//        public int getNum() {
//            return i;
//        }
//    }
}
