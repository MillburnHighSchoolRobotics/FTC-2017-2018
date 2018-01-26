package virtualRobot.logicThreads.competition;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.Command;
import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.HitJewel;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Servo;
import virtualRobot.logicThreads.AutonomousLogicThread;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.CENTER;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.RIGHT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.UNKNOWN;
import static virtualRobot.SallyJoeBot.Team.BLUE;

/**
 * Created by ethan on 9/22/17.
 */

public class BlueNearBasicAutoLogic extends AutonomousLogicThread {
    private Motor leftFront, leftBack, rightFront, rightBack;
    private Servo jewelArm;
    private DumbColorSensor colorSensor;
    private static final double RIGHTPOS = 0, LEFTPOS = 1, CENTERPOS = 0.5;

    @Override
    protected void realRun() throws InterruptedException {
        double timeS = System.currentTimeMillis();
        Log.d("Begin", "Began");
        robot.getJewelHitter().setPosition(RIGHTPOS);
        leftFront = robot.getLFMotor();
        leftBack = robot.getLBMotor();
        rightFront = robot.getRFMotor();
        rightBack = robot.getRBMotor();
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

        runCommand(new GetVuMarkSide());
//        int choice = (int)Math.floor(Math.random() * 3);
//        int choice = 1;
//        currentVuMark = new RelicRecoveryVuMark[] {LEFT, CENTER, RIGHT}[choice]; //lmao why does this work
        robot.addToTelemetry("Current VuMark: ", currentVuMark);
        if (true) {
            Servo arm = robot.getJewelServo();
            Servo hitter = robot.getJewelHitter();
            hitter.setPosition(CENTERPOS);
            arm.setPosition(0.67);
            Thread.sleep(200);
            Thread.sleep(1000);
            DumbColorSensor cs = robot.getColorSensor();
            int red = cs.getRed();
            int blue = cs.getBlue();
            if ((red != 0 || blue != 0)) {
                blue = Math.max(1, blue);
                double rat = red / (double) blue;
                robot.addToTelemetry("CS", red + " " + blue + " " + rat);
                if (rat >= 1.5) {
                    hitter.setPosition(RIGHTPOS);
                    Thread.sleep(500);
                    hitter.setPosition(CENTERPOS);
                    Thread.sleep(500);
                } else if (rat <= 0.6) {
                    hitter.setPosition(LEFTPOS);
                    Thread.sleep(500);
                    hitter.setPosition(CENTERPOS);
                    Thread.sleep(500);
                }
                robot.addToProgress("Complete Jewel Servo");
            }

            arm.setPosition(0.07);
            Log.d("Progress", "Jewel Completed");
            Thread.sleep(5000);
        }

        dist = 0;
        int rot = 950;
        if (currentVuMark == UNKNOWN)
            currentVuMark = LEFT;
        switch(currentVuMark){
            case LEFT:
                dist = 1400; //1926
                rot += 100; //rotate more
                break;
            case CENTER:
                dist = 1700; //2126
                rot -= 100;
                break;
            case RIGHT:
                dist = 2000; //2326
                break;
        }

        Log.d("Progress", "Vumark " + currentVuMark.name());

        startPosition = leftFront.getPosition();
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while (leftFront.getPosition() - startPosition < dist && !Thread.interrupted()) {}
        robot.stopMotors();

        Thread.sleep(1000);
        startPosition = leftFront.getPosition();
        leftFront.setPower(-power*1.5);
        leftBack.setPower(-power*1.5);
        rightFront.setPower(power*1.5);
        rightBack.setPower(power*1.5);

        while (leftFront.getPosition() - startPosition > -rot && !Thread.interrupted()) {}

        robot.stopMotors();

        Thread.sleep(1000);
        startPosition = leftFront.getPosition();
        leftFront.setPower(power*1.5);
        leftBack.setPower(power*1.5);
        rightFront.setPower(power*1.5);
        rightBack.setPower(power*1.5);

        while (leftFront.getPosition() - startPosition < 3000 && !Thread.interrupted()) {}
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

        while (leftFront.getPosition() - startPosition > -600 && !Thread.interrupted()) {}
        robot.stopMotors();
    }
}
