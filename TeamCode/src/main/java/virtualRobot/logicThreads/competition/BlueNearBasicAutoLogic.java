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
        jewelArm.setPosition(0.65);
        int dist = 0;
        int travel = 200;
        double power = 0.5;
        int startPosition;
        Thread.sleep(1000);

        GetVuMarkSide er = new GetVuMarkSide();
        runCommand(er);

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
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(power);
                rightBack.setPower(power);
                while (rightFront.getPosition() - startPosition < travel) {
                }
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
                while (rightFront.getPosition() - startPosition > -travel) {
                }
                dist = travel;
            } else if (rat <= 0.5) {
                startPosition = leftFront.getPosition();
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
                while (rightFront.getPosition() - startPosition > -travel) {
                }
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(power);
                rightBack.setPower(power);
                while (rightFront.getPosition() - startPosition < travel) {
                }
                dist = -travel;
            }
        }
        robot.stopMotors();
        jewelArm.setPosition(0);
        Thread.sleep(2000);
        startPosition = leftFront.getPosition();
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        double yes= 1440;

        switch(currentVuMark){
            case LEFT:
                yes =900;
                break;
            case CENTER:
                yes = 1100;
                break;
            case RIGHT:
                yes = 1300;
                break;
        }

        while ((leftFront.getPosition() - startPosition) + dist < yes*0.85 && !Thread.interrupted()) {}
        robot.stopMotors();

        Thread.sleep(1000);
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);

        while ((leftFront.getPosition() - startPosition) + dist < 1300*0.85 && !Thread.interrupted()) {}

        robot.stopMotors();

        Thread.sleep(1000);
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);

        while ((leftFront.getPosition() - startPosition) + dist < 100*0.85 && !Thread.interrupted()) {}
        robot.stopMotors();

        Thread.sleep(1000);
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while ((leftFront.getPosition() - startPosition) + dist < 100*0.85 && !Thread.interrupted()) {}
        robot.stopMotors();


        while(!Thread.interrupted()) {

        }
    }
}
