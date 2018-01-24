package virtualRobot.logicThreads.competition;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.Command;
import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Servo;
import virtualRobot.logicThreads.AutonomousLogicThread;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.CENTER;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.RIGHT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.UNKNOWN;

/**
 * Created by ethan on 9/22/17.
 */

public class BlueNearBasicAutoLogic extends AutonomousLogicThread {
    private Motor leftFront, leftBack, rightFront, rightBack;
    private Servo jewelArm;
    private DumbColorSensor colorSensor;
    @Override
    protected void realRun() throws InterruptedException {
        double timeS = System.currentTimeMillis();
        Log.d("Begin", "Began");
        leftFront = robot.getLFMotor();
        leftBack = robot.getLBMotor();
        rightFront = robot.getRFMotor();
        rightBack = robot.getRBMotor();
        colorSensor = robot.getColorSensor();
        jewelArm = robot.getJewelServo();
        jewelArm.setPosition(0.67);
        Thread.sleep(2000);
        robot.addToTelemetry("Time Difference:", System.currentTimeMillis() - timeS);
        Log.d("Progress", "Time Difference:" + String.valueOf(System.currentTimeMillis() - timeS));
        int dist = 0;
        int travel = 200;
        double power = - 0.35;
        double turn = power*2;
        int startPosition;
//        Thread.sleep(1000);

//        runCommand(new GetVuMarkSide());
        int choice = (int)Math.floor(Math.random() * 3);
//        int choice = 1;
        currentVuMark = new RelicRecoveryVuMark[] {LEFT, CENTER, RIGHT}[choice]; //lmao why does this work
        robot.addToTelemetry("Current VuMark: ", currentVuMark);

        int red = colorSensor.getRed();
        int blue = colorSensor.getBlue();
        Log.d("Red Blue", red + " " + blue);
        robot.addToTelemetry("Red ", red);
        robot.addToTelemetry("Blue ", blue);
//        blue = Math.max(1, blue);
        if (red != 0 || blue != 0) {
            Log.d("Progress", "Jewel Beginning");
            blue = Math.max(1, blue);
            double rat = red/(double)blue;
            if (rat >= 1.2) {
                Log.d("Progress", "Jewel Red");
                startPosition = rightFront.getPosition();
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
                while (rightFront.getPosition() - startPosition > -travel) {
//                    Log.d("Ultimate Meme", Integer.toString(rightFront.getPosition() - startPosition));
                }
                jewelArm.setPosition(0.07);
                robot.stopMotors();
                Thread.sleep(5000);
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(power);
                rightBack.setPower(power);
                while (rightFront.getPosition() - startPosition < 0) {
                }
//                dist = travel;
            } else if (rat <= 0.8) {
                Log.d("Progress", "Jewel Blue");
                startPosition = rightFront.getPosition();
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(power);
                rightBack.setPower(power);
                while (rightFront.getPosition() - startPosition < travel) {
                }
                robot.stopMotors();
                jewelArm.setPosition(0.07);
                Thread.sleep(5000);
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
                while (rightFront.getPosition() - startPosition > 0) {
                }
//                dist = -travel;
            }
        }
        Log.d("Progress", "Jewel Completed");
        robot.stopMotors();
        jewelArm.setPosition(0.07);
        Thread.sleep(5000);

        dist = 0;
        if (currentVuMark == UNKNOWN)
            currentVuMark = LEFT;
        switch(currentVuMark){
            case LEFT:
                dist = 1600; //1926
                break;
            case CENTER:
                dist = 2000; //2126
                break;
            case RIGHT:
                dist = 2400; //2326
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

        while (leftFront.getPosition() - startPosition > -1350 && !Thread.interrupted()) {}

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
