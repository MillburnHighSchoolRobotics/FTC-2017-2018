package virtualRobot.logicThreads.competition;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.GetVuMarkSide;
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
        jewelArm.setPosition(0.64);
        int dist = 0;
        int travel = 200;
        double power = 0.35;
        int startPosition;
        Thread.sleep(1000);

        runCommand(new GetVuMarkSide());

        int red = colorSensor.getRed();
        int blue = colorSensor.getBlue();
        robot.addToTelemetry("Red ", red);
        robot.addToTelemetry("Blue ", blue);
//        blue = Math.max(1, blue);
        if (red != 0 || blue != 0) {
            blue = Math.max(1, blue);
            double rat = red/(double)blue;
            if (rat >= 1.5) {
                startPosition = leftFront.getPosition();
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(power);
                rightBack.setPower(power);
                while (rightFront.getPosition() - startPosition < travel*0.5) {
                }
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
                while (rightFront.getPosition() - startPosition > -travel*0.5) {
                }
//                dist = travel;
            } else if (rat <= 0.5) {
                startPosition = leftFront.getPosition();
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
                while (rightFront.getPosition() - startPosition > -travel*0.5) {
                }
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(power);
                rightBack.setPower(power);
                while (rightFront.getPosition() - startPosition < travel*0.5) {
                }
//                dist = -travel;
            }
        }
        robot.stopMotors();
        jewelArm.setPosition(0);
        Thread.sleep(2000);

        dist = 0;

        robot.addToTelemetry("Current Vumark: ", currentVuMark.toString());
        switch(currentVuMark){
            case LEFT:
                dist = 45;
                break;
            case CENTER:
                dist = 65;
                break;
            case RIGHT:
                dist = 85;
                break;
        }

        startPosition = leftFront.getPosition();
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while ((leftFront.getPosition() - startPosition) < dist && !Thread.interrupted()) {}
        robot.stopMotors();

        Thread.sleep(1000);
        startPosition = leftFront.getPosition();
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);

        while ((leftFront.getPosition() - startPosition) < 550 && !Thread.interrupted()) {}

        robot.stopMotors();

        Thread.sleep(1000);
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while ((leftFront.getPosition() - startPosition) < 50 && !Thread.interrupted()) {}
        robot.stopMotors();

        Thread.sleep(1000);
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);

        while ((leftFront.getPosition() - startPosition) > -50 && !Thread.interrupted()) {}
        robot.stopMotors();
    }
}
