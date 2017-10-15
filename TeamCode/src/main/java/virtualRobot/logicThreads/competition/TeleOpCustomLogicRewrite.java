package virtualRobot.logicThreads.competition;

import com.qualcomm.robotcore.robot.Robot;

import java.util.Date;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.utils.MathUtils;

/**
 * Created by david on 9/29/17.
 */

public class TeleOpCustomLogicRewrite extends LogicThread {
    @Override
    protected void addPresets() {
        shouldStartISR = false;
    }

    @Override
    protected void realRun() throws InterruptedException {
        JoystickController controller1;
        JoystickController controller2;
        controller1 = robot.getJoystickController1();
        controller2 = robot.getJoystickController2();
        int leftMin = 800, leftMax = 8700, rightMin = 0, rightMax = 8200;
        final int POWER_MATRIX[][] = { //for each of the directions

                {1, 1, 1, 1},
                {1, 0, 0, 1},
                {1, -1, -1, 1},
                {0, -1, -1, 0},
                {-1, -1, -1, -1},
                {-1, 0, 0, -1},
                {-1, 1, 1, -1},
                {0, 1, 1, 0}
        };

        while (true) {
//            robot.addToTelemetry("TeleOp timestamp: ", System.currentTimeMillis());
            robot.addToTelemetry("EncoderL", robot.getGlyphLiftLeft().getPosition());
            robot.addToTelemetry("EncoderR", robot.getGlyphLiftRight().getPosition());
            controller1.logicalRefresh();
            controller2.logicalRefresh();
            double translateTheta = Math.toDegrees(controller1.getValue(JoystickController.THETA_1));
            double translateMag = controller1.getValue(JoystickController.R_1);
            double rotateX = controller1.getValue(JoystickController.X_2);
            if (translateTheta < 0) translateTheta += 360;
            double scale;
            double RF = 0, RB = 0, LF = 0, LB = 0;
            robot.addToTelemetry("mag", translateMag);
            if (!MathUtils.equals(rotateX, 0, 0.05)) {
                robot.getRFMotor().setPower(-rotateX);
                robot.getRBMotor().setPower(-rotateX);
                robot.getLFMotor().setPower(rotateX);
                robot.getLBMotor().setPower(rotateX);
//                robot.addToTelemetry("TeleOp if statement lvl", 0);
            } else if (!MathUtils.equals(translateMag, 0, 0.05)) {
                double translatePower = translateMag * 0.666;
                if (translateTheta >= 0 && translateTheta <= 90) { //quadrant 1
                    scale = MathUtils.sinDegrees(translateTheta - 45) / MathUtils.cosDegrees(translateTheta - 45);
                    RF = translatePower * POWER_MATRIX[0][0];
                    RB = translatePower * POWER_MATRIX[0][1] * scale;
                    LF = translatePower * POWER_MATRIX[0][2] * scale;
                    LB = translatePower * POWER_MATRIX[0][3];
                } else if (translateTheta > 90 && translateTheta <= 180) { //quadrant 2
                    translatePower *= -1;
                    scale = MathUtils.sinDegrees(translateTheta - 135) / MathUtils.cosDegrees(translateTheta - 135);
                    RF = (translatePower * POWER_MATRIX[2][0] * scale);
                    RB = (translatePower * POWER_MATRIX[2][1]);
                    LF = (translatePower * POWER_MATRIX[2][2]);
                    LB = (translatePower * POWER_MATRIX[2][3] * scale);
                } else if (translateTheta > 180 && translateTheta <= 270) { //quadrant 3
                    scale = MathUtils.sinDegrees(translateTheta - 225) / MathUtils.cosDegrees(translateTheta - 225);
                    RF = (translatePower * POWER_MATRIX[4][0]);
                    RB = (translatePower * POWER_MATRIX[4][1] * scale);
                    LF = (translatePower * POWER_MATRIX[4][2] * scale);
                    LB = (translatePower * POWER_MATRIX[4][3]);
//                Log.d("aaa", robot.getLFMotor().getPower() + " " + robot.getRFMotor().getPower() + " " + robot.getLBMotor().getPower() + " " + robot.getRBMotor().getPower());
                } else if (translateTheta > 270 && translateTheta < 360) { //quadrant 4
                    translatePower *= -1;
                    scale = MathUtils.sinDegrees(translateTheta - 315) / MathUtils.cosDegrees(translateTheta - 315);
                    RF = (translatePower * POWER_MATRIX[6][0] * scale);
                    RB = (translatePower * POWER_MATRIX[6][1]);
                    LF = (translatePower * POWER_MATRIX[6][2]);
                    LB = (translatePower * POWER_MATRIX[6][3] * scale);
                }

                robot.getLFMotor().setPower(LF);
                robot.getLBMotor().setPower(LB);
                robot.getRBMotor().setPower(RB);
                robot.getRFMotor().setPower(RF);

            } else {
//                robot.addToTelemetry("TeleOp if statement lvl", 2);
                robot.stopMotors();
            }

            if (controller1.isDown(JoystickController.BUTTON_LT) && robot.getGlyphLiftLeft().getPosition() > leftMin && robot.getGlyphLiftRight().getPosition() > rightMin) {
                robot.getGlyphLiftLeft().setPower(-1);
                robot.getGlyphLiftRight().setPower(-1);
            } else if (controller1.isDown(JoystickController.BUTTON_RT) && robot.getGlyphLiftLeft().getPosition() < leftMax && robot.getGlyphLiftRight().getPosition() < rightMax) {
                robot.getGlyphLiftLeft().setPower(1);
                robot.getGlyphLiftRight().setPower(1);
            } else if (controller1.isDown(JoystickController.BUTTON_A)) {
                robot.getGlyphLiftLeft().setPower(-1);
            } else if (controller1.isDown(JoystickController.BUTTON_X)) {
                robot.getGlyphLiftLeft().setPower(1);
            } else if (controller1.isDown(JoystickController.BUTTON_B)) {
                robot.getGlyphLiftRight().setPower(-1);
            } else if (controller1.isDown(JoystickController.BUTTON_Y)) {
                robot.getGlyphLiftRight().setPower(1);
            } else {
                robot.getGlyphLiftLeft().setPower(0);
                robot.getGlyphLiftRight().setPower(0);
            }

            if (controller1.isPressed(JoystickController.BUTTON_LB)) {
                robot.moveClaw(false);
            } else if (controller1.isPressed(JoystickController.BUTTON_RB)) {
                robot.moveClaw(true);
            }

            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
