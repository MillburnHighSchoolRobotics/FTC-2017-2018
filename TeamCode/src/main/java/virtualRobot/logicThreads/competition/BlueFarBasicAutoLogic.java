package virtualRobot.logicThreads.competition;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Servo;
import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by ethan on 9/22/17.
 */

public class BlueFarBasicAutoLogic extends AutonomousLogicThread {
    private Motor leftFront, leftBack, rightFront, rightBack;
    private Servo jewelArm;
    private DumbColorSensor colorSensor;
    private static final double RIGHTPOS = -0.2, LEFTPOS = 1.2, CENTERPOS = 0.5;
    @Override
    protected void realRun() throws InterruptedException {
        double timeS = System.currentTimeMillis();
        Log.d("Progress", "Began");
//
        colorSensor = robot.getColorSensor();
        jewelArm = robot.getJewelServo();
//        jewelArm.setPosition(0.67);
//        Thread.sleep(2000);
        robot.addToTelemetry("Time Difference:", System.currentTimeMillis() - timeS);
        Log.d("Progress", "Time Difference:" + String.valueOf(System.currentTimeMillis() - timeS));
        int dist = 0;
        int travel = 200;
        double power = - 0.35;
        double turn = power*2;
        int startPosition;
//        Thread.sleep(1000);

        runCommand(new GetVuMarkSide(2000));
//        int choice = (int)Math.floor(Math.random() * 3);
//        int choice = 1;
//        currentVuMark = new RelicRecoveryVuMark[] {LEFT, CENTER, RIGHT}[choice]; //lmao why does this work
//        currentVuMark = GlobalUtils.forcedVumark;
        robot.addToTelemetry("Current VuMark: ", currentVuMark);
//        if (true) {
        robot.addToTelemetry("Hello", " there");
        Servo hitter = robot.getJewelHitter();
        jewelArm.setPosition(0.15);
        Thread.sleep(500);
        hitter.setPosition(CENTERPOS);
        Thread.sleep(200);
        jewelArm.setPosition(0.44);
        Thread.sleep(1000);
        int red = colorSensor.getRed();
        int blue = colorSensor.getBlue();
        if ((red != 0 || blue != 0)) {
            blue = Math.max(1, blue);
            double rat = red / (double) blue;
            robot.addToTelemetry("CS", red + " " + blue + " " + rat);
            if (rat >= 1.5) {
                hitter.setPosition(RIGHTPOS);
                Thread.sleep(1000);
                hitter.setPosition(CENTERPOS);
                Thread.sleep(500);
            } else if (rat <= 0.6) {
                hitter.setPosition(LEFTPOS);
                Thread.sleep(1000);
                hitter.setPosition(CENTERPOS);
                Thread.sleep(500);
            }
            robot.addToProgress("Complete Jewel Servo");
        }

        jewelArm.setPosition(0);
        Log.d("Progress", "Jewel Completed");
        Thread.sleep(1000 );
      /*  leftFront = robot.getLFMotor();
        leftBack = robot.getLBMotor();
        rightFront = robot.getRFMotor();
        rightBack = robot.getRBMotor();
        colorSensor = robot.getColorSensor();
        jewelArm = robot.getJewelServo();
        jewelArm.setPosition(0.67);
        Thread.sleep(500);
        int dist = 0;
        int travel = 400;
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
            if (rat >= 1.2) {
                startPosition = rightFront.getPosition();
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
//                dist = travel;
            } else if (rat <= 0.8) {
                startPosition = rightFront.getPosition();
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
//                dist = -travel;
            }
        }
        robot.stopMotors();
        jewelArm.setPosition(0.07);
        Thread.sleep(500);

        dist = 0;
        int rot = 0;
        if (currentVuMark == RelicRecoveryVuMark.UNKNOWN)
            currentVuMark = RelicRecoveryVuMark.LEFT;
        switch(currentVuMark){
            case LEFT:
                dist = 1926;
                rot = 200;
                break;
            case CENTER:
                dist = 2126;
                rot = 300;
                break;
            case RIGHT:
                dist = 2326;
                rot = 450;
                break;
        }

        startPosition = leftFront.getPosition();
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);
        while ((leftFront.getPosition() - startPosition) < rot && !Thread.interrupted()) {}
        robot.stopMotors();

        startPosition = leftFront.getPosition();
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while ((leftFront.getPosition() - startPosition) < dist && !Thread.interrupted()) {}
        robot.stopMotors();

        Thread.sleep(1500);
        robot.moveClaw(true);
        Thread.sleep(2000);

        startPosition = leftFront.getPosition();

//        Thread.sleep(1000);
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);

        while ((leftFront.getPosition() - startPosition) > -600 && !Thread.interrupted()) {}
        robot.stopMotors();*/
    }
}
