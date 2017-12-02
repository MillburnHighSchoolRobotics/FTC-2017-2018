package virtualRobot.logicThreads.competition;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.EthanClass;
import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Servo;
import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by ethan on 9/22/17.
 */

public class RedNearBasicAutoLogic extends AutonomousLogicThread {
//    @Override
//    protected void realRun() throws InterruptedException {
//        runCommand(new Translate(50, Translate.Direction.FORWARD, 0));
//
//        runCommand(new EthanClass());
//
//        if(redIsLeft.get()) {
//            robot.getJewelServo().setPosition(1);
//            //runCommand(new Rotate(-90));
//        }
//        else{
//            robot.getJewelServo().setPosition(0);
//            //runCommand(new Translate(100, Translate.Direction.RIGHT, 0));
//            //runCommand(new Rotate(-90));
//        }
//
//        runCommand(new Translate(75, Translate.Direction.BACKWARD, 0));
//        runCommand(new Translate(100, Translate.Direction.LEFT, 0));
//
//        runCommand(new GetVuMarkSide());
//
//        runCommand(new Translate(200, Translate.Direction.LEFT, 0));
//
//        runCommand(new Rotate(-90,.5));
//
//        if(currentVuMark == RelicRecoveryVuMark.LEFT)
//            runCommand(new Translate(300, Translate.Direction.LEFT, 0));
//        else if(currentVuMark == RelicRecoveryVuMark.CENTER)
//            runCommand(new Translate(250, Translate.Direction.LEFT, 0));
//        else if(currentVuMark == RelicRecoveryVuMark.RIGHT)
//            runCommand(new Translate(200, Translate.Direction.LEFT, 0));
//
//        runCommand(new Translate(100, Translate.Direction.FORWARD, 0));
//
//        robot.moveClaw(true);
//
//    }
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
        jewelArm.setPosition(0.7);
        int dist = 0;
        int travel = 200;
        double power = 0.5;
        int startPosition;
        Thread.sleep(1000);
        int red = colorSensor.getRed();
        int blue = colorSensor.getBlue();
        robot.addToTelemetry("Red ", red);
        robot.addToTelemetry("Blue ", blue);

        if (red != 0 || blue != 0) {
            blue = Math.max(1, blue);
            double rat = red/(double)blue;
            if (rat >= 1.5) {
                startPosition = leftFront.getPosition();
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(power);
                rightBack.setPower(power);
                while (leftFront.getPosition() - startPosition < travel) {
                }
                dist = -travel;
            } else if (rat <= 0.5) {
                startPosition = leftFront.getPosition();
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
                while (leftFront.getPosition() - startPosition > -travel) {
                }
                dist = travel;
            }
        }
        jewelArm.setPosition(0);
        robot.stopMotors();
        Thread.sleep(2000);
        startPosition = leftFront.getPosition();
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);
        while ((leftFront.getPosition() - startPosition) + dist > -1440*0.85 && !Thread.interrupted()) {}
        robot.stopMotors();
        while(!Thread.interrupted()) {}
    }
}
