package virtualRobot.logicThreads.competition;

import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Servo;
import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by ethan on 9/22/17.
 */

public class BlueNearBasicAutoLogic extends AutonomousLogicThread {
    private Motor leftFront, leftBack, rightFront, rightBack;
    private Servo jewelArm;
    private DumbColorSensor colorSensor;
    @Override
    protected void realRun() throws InterruptedException {
        leftFront = robot.getLFMotor();
        leftBack = robot.getLBMotor();
        rightFront = robot.getRFMotor();
        rightBack = robot.getRBMotor();
        colorSensor = robot.getColorSensor();
        jewelArm = robot.getJewelServo();
        jewelArm.setPosition(0.5);
        int dist = 0;
        int travel = 200;
        double power = 0.5;
        int startPosition;
        Thread.sleep(1000);
        int red = colorSensor.getRed();
        int blue = colorSensor.getBlue();
        robot.addToTelemetry("Red ", red);
        robot.addToTelemetry("Blue ", blue);
//        blue = Math.max(1, blue);
        double rat = red/(double)blue;
        if (red != 0 || blue != 0) {
            blue = Math.max(1, blue);
            if (rat >= 1.5) {
                startPosition = leftFront.getPosition();
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(power);
                rightBack.setPower(power);
                while (leftFront.getPosition() - startPosition < travel) {
                }
                dist = travel;
            } else if (rat <= 0.5) {
                startPosition = leftFront.getPosition();
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
                while (leftFront.getPosition() - startPosition > -travel) {
                }
                dist = -travel;
            }
        }
        jewelArm.setPosition(0);
        robot.stopMotors();
        Thread.sleep(2000);
        startPosition = leftFront.getPosition();
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);
        while ((leftFront.getPosition() - startPosition) + dist < 1440*0.85 && !Thread.interrupted()) {}
        robot.stopMotors();
        while(!Thread.interrupted()) {}
    }
}
