package virtualRobot.logicThreads.competition;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.EthanClass;
import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Servo;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.GlobalUtils;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.UNKNOWN;

/**
 * Created by ethan on 9/22/17.
 */

public class RedNearBasicAutoLogic extends AutonomousLogicThread {
    private Motor leftFront, leftBack, rightFront, rightBack, encoder;
    private Servo jewelArm;
    private DumbColorSensor colorSensor;
    private static final double RIGHTPOS = -0.2, LEFTPOS = 1.2, CENTERPOS = 0.5;

    @Override
    protected void realRun() throws InterruptedException {
        double timeS = System.currentTimeMillis();
        Log.d("Progress", "Began");
//        robot.getJewelHitter().setPosition(CENTERPOS);

        leftFront = robot.getLFMotor();
        leftBack = robot.getLBMotor();
        rightFront = robot.getRFMotor();
        rightBack = robot.getRBMotor();

        encoder = leftFront;
        colorSensor = robot.getColorSensor();
        jewelArm = robot.getJewelServo();
//        jewelArm.setPosition(0.67);
//        Thread.sleep(2000);
        robot.addToTelemetry("Time Difference:", System.currentTimeMillis() - timeS);
        Log.d("Progress", "Time Difference:" + String.valueOf(System.currentTimeMillis() - timeS));
        int dist = 0;
        int travel = 200;
        double power = -0.35;
        double turn = power * 2;
        int startPosition;
//        Thread.sleep(1000);

        runCommand(new GetVuMarkSide(4000));
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
        robot.addToTelemetry("Red Blue: ", red + " " + blue);
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
        Thread.sleep(500);
//        }

        dist = 0;
        int rot = 1450;
        if (currentVuMark == UNKNOWN)
            currentVuMark = LEFT;
        switch (currentVuMark) {
            case LEFT:
                dist = 1500; //1926
                rot += 100; //rotate more
                break;
            case CENTER:
                dist = 2150; //2126
                rot -= 100;
                break;
            case RIGHT:
                dist = 2350; //2326
                break;
        }

        Log.d("Progress", "Vumark " + currentVuMark.name());

        startPosition = encoder.getPosition();
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);

        while (encoder.getPosition() - startPosition > -dist && !Thread.interrupted()) {
        }
        robot.stopMotors();

        robot.addToProgress("Ended Front");

        Thread.sleep(1000);
        startPosition = encoder.getPosition();
        leftFront.setPower(-power * 1.5);
        leftBack.setPower(-power * 1.5);
        rightFront.setPower(power * 1.5);
        rightBack.setPower(power * 1.5);

        while (encoder.getPosition() - startPosition > -rot && !Thread.interrupted()) {
        }

        robot.stopMotors();

        robot.addToProgress("Ended Rot");
        Thread.sleep(1000);
        startPosition = encoder.getPosition();
        leftFront.setPower(power * 1.5);
        leftBack.setPower(power * 1.5);
        rightFront.setPower(power * 1.5);
        rightBack.setPower(power * 1.5);

//        while (leftBack.getPosition() - startPosition < 1500 && !Thread.interrupted()) {
//        }
        Thread.sleep(2000);
        robot.stopMotors();


        robot.addToProgress("Ended Deposit");
        Thread.sleep(1500);
        robot.moveClaw(true);
        Thread.sleep(2000);

        startPosition = encoder.getPosition();

//        Thread.sleep(1000);
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);

        while (encoder.getPosition() - startPosition > -500 && !Thread.interrupted()) {
        }
        robot.stopMotors();

        robot.addToProgress("Ended Backup");
    }
}
