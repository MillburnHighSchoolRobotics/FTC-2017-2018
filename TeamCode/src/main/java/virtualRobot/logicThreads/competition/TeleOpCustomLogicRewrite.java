package virtualRobot.logicThreads.competition;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.utils.MathUtils;

/**
 * Created by david on 9/29/17.
 */

public class TeleOpCustomLogicRewrite extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        JoystickController controller1;
        JoystickController controller2;
        controller1 = robot.getJoystickController1();
        controller2 = robot.getJoystickController2();
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
            controller1.logicalRefresh();
            controller2.logicalRefresh();
            double translateTheta = Math.toDegrees(controller1.getValue(JoystickController.THETA_1));
            double translateMag = controller1.getValue(JoystickController.R_1);
            double rotateX = controller1.getValue(JoystickController.X_2);
            if (translateTheta < 0) translateTheta += 360;
            double scale;
            double RF = 0, RB = 0, LF = 0, LB = 0;
            if (!MathUtils.equals(rotateX, 0, 0.0001)) {
                robot.getRFMotor().setPower(-rotateX);
                robot.getRBMotor().setPower(-rotateX);
                robot.getLFMotor().setPower(rotateX);
                robot.getLBMotor().setPower(rotateX);
            } else if (!MathUtils.equals(translateMag, 0, 0.0001)) {
                double translatePower = translateMag * 0.66; //set later
                if (MathUtils.equals(translateTheta, 90)) {
                    //Forward
                    RF = translatePower * POWER_MATRIX[0][0];
                    RB = translatePower * POWER_MATRIX[0][1];
                    LF = translatePower * POWER_MATRIX[0][2];
                    LB = translatePower * POWER_MATRIX[0][3];
                } else if (MathUtils.equals(translateTheta, 180)) {
                    //Left
                    RF = translatePower * POWER_MATRIX[2][0];
                    RB = translatePower * POWER_MATRIX[2][1];
                    LF = -translatePower * POWER_MATRIX[2][2];
                    LB = -translatePower * POWER_MATRIX[2][3];
                } else if (MathUtils.equals(translateTheta, 270)) {
                    //Backward
                    RF = translatePower * POWER_MATRIX[4][0];
                    RB = translatePower * POWER_MATRIX[4][1];
                    LF = translatePower * POWER_MATRIX[4][2];
                    LB = translatePower * POWER_MATRIX[4][3];
                } else if (MathUtils.equals(translateTheta, 0)) {
                    //Right
                    RF = translatePower * POWER_MATRIX[6][0];
                    RB = translatePower * POWER_MATRIX[6][1];
                    LF = -translatePower * POWER_MATRIX[6][2];
                    LB = -translatePower * POWER_MATRIX[6][3];
                }
                robot.addToTelemetry("0", LF + " " + RF);
                robot.addToTelemetry("1", LB + " " + RB);
                robot.getRFMotor().setPower(RF);
                robot.getRBMotor().setPower(RB);
                robot.getLFMotor().setPower(LF);
                robot.getLBMotor().setPower(LB);
            } else {
                robot.stopMotors();
            }

            if (controller1.isPressed(JoystickController.BUTTON_LT)) {
                robot.getGlyphLift().setPower(-0.5);
            } else if (controller1.isPressed(JoystickController.BUTTON_RT)) {
                robot.getGlyphLift().setPower(0.5);
            } else {
                robot.getGlyphLift().setPower(0);
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
