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
//        Thread.sleep(1000);

        runCommand(new GetVuMarkSide());
        robot.addToTelemetry("Current Vumark: ", currentVuMark);

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
        jewelArm.setPosition(0.07);
//        Thread.sleep(2000);

        dist = 0;

        if (currentVuMark == RelicRecoveryVuMark.UNKNOWN)
            currentVuMark = RelicRecoveryVuMark.LEFT;
        switch(currentVuMark){
            case LEFT:
                dist = 1211;
                break;
            case CENTER:
                dist = 1311;
                break;
            case RIGHT:
                dist = 1411;
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

        while ((leftFront.getPosition() - startPosition) < 900 && !Thread.interrupted()) {}

        robot.stopMotors();

        Thread.sleep(1000);
        startPosition = leftFront.getPosition();
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while ((leftFront.getPosition() - startPosition) < 250 && !Thread.interrupted()) {}
        robot.stopMotors();

        startPosition = leftFront.getPosition();

//        Thread.sleep(1000);
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);

        while ((leftFront.getPosition() - startPosition) > -250 && !Thread.interrupted()) {}
        robot.stopMotors();
    }
}
