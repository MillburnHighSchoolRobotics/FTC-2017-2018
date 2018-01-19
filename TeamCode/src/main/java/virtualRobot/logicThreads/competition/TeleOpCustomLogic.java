package virtualRobot.logicThreads.competition;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.commands.Translate;
import virtualRobot.utils.MathUtils;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

public class TeleOpCustomLogic extends LogicThread {
    @Override
    protected void addPresets() {
        shouldStartISR = false;
    }

    @Override
    protected void realRun() throws InterruptedException {
        robot.getJewelServo().setPosition(0.07);
        JoystickController controller1;
        JoystickController controller2;
        controller1 = robot.getJoystickController1();
        controller2 = robot.getJoystickController2();
        Translate.Direction direction = null;
        Translate.Direction lastDirection = null;
        double intakePos = 0;
        long lastIntakePosChange = 0;
        int translateAngle = 0;
        int lastTranslateAngle = 0;
        final int POWER_MATRIX[][] = { //for each of the directions
                //RF, RB, LF, LB
                {1, 1, 1, 1},
                {1, 0, 0, 1},
                {1, -1, -1, 1},
                {0, -1, -1, 0},
                {-1, -1, -1, -1},
                {-1, 0, 0, -1},
                {-1, 1, 1, -1},
                {0, 1, 1, 0}
        };
        double liftSpeed = 1;
        double relicArmSpeed = 0.5;
        double gearCoefficient = 0.666;
//        Translate headingMovement = null;
//        int lastAction = 0; //0 for stopped, 1 for translating, 2 for rotating
        boolean isInterrupted = false;
        while (!isInterrupted) {
            controller1.logicalRefresh();
            controller2.logicalRefresh();
            double translateTheta = Math.toDegrees(controller1.getValue(JoystickController.THETA_1));
            double translateMag = controller1.getValue(JoystickController.R_1);
            double rotateX = controller1.getValue(JoystickController.X_2);
            if (translateTheta < 0) translateTheta += 360;
            double scale;
            double RF = 0, RB = 0, LF = 0, LB = 0;
            robot.addToTelemetry("mag", translateMag);

            if (controller1.isDpadUp()) {
                gearCoefficient = 1;
            } else if (controller1.isDpadDown()) {
                gearCoefficient = 0.5;
            }
            if (!MathUtils.equals(rotateX, 0, 0.05)) {
                robot.getRFMotor().setPower(rotateX * gearCoefficient);
                robot.getRBMotor().setPower(rotateX * gearCoefficient);
                robot.getLFMotor().setPower(-rotateX * gearCoefficient);
                robot.getLBMotor().setPower(-rotateX * gearCoefficient);
//                robot.addToTelemetry("TeleOp if statement lvl", 0);
            } else if (!MathUtils.equals(translateMag, 0, 0.05)) {
                double translatePower = translateMag * 0.666;
                if (translateTheta >= 0 && translateTheta <= 90) { //quadrant 1
                    scale = MathUtils.sinDegrees(translateTheta - 45) / MathUtils.cosDegrees(translateTheta - 45);
                    LF = translatePower * POWER_MATRIX[0][0];
                    LB = translatePower * POWER_MATRIX[0][1] * scale;
                    RF = translatePower * POWER_MATRIX[0][2] * scale;
                    RB = translatePower * POWER_MATRIX[0][3];
                } else if (translateTheta > 90 && translateTheta <= 180) { //quadrant 2
                    translatePower *= -1;
                    scale = MathUtils.sinDegrees(translateTheta - 135) / MathUtils.cosDegrees(translateTheta - 135);
                    LF = (translatePower * POWER_MATRIX[2][0] * scale);
                    LB = (translatePower * POWER_MATRIX[2][1]);
                    RF = (translatePower * POWER_MATRIX[2][2]);
                    RB = (translatePower * POWER_MATRIX[2][3] * scale);
                } else if (translateTheta > 180 && translateTheta <= 270) { //quadrant 3
                    scale = MathUtils.sinDegrees(translateTheta - 225) / MathUtils.cosDegrees(translateTheta - 225);
                    LF = (translatePower * POWER_MATRIX[4][0]);
                    LB = (translatePower * POWER_MATRIX[4][1] * scale);
                    RF = (translatePower * POWER_MATRIX[4][2] * scale);
                    RB = (translatePower * POWER_MATRIX[4][3]);
//                Log.d("aaa", robot.getLFMotor().getPower() + " " + robot.getRFMotor().getPower() + " " + robot.getLBMotor().getPower() + " " + robot.getRBMotor().getPower());
                } else if (translateTheta > 270 && translateTheta < 360) { //quadrant 4
                    translatePower *= -1;
                    scale = MathUtils.sinDegrees(translateTheta - 315) / MathUtils.cosDegrees(translateTheta - 315);
                    LF = (translatePower * POWER_MATRIX[6][0] * scale);
                    LB = (translatePower * POWER_MATRIX[6][1]);
                    RF = (translatePower * POWER_MATRIX[6][2]);
                    RB = (translatePower * POWER_MATRIX[6][3] * scale);
                }
                robot.addToTelemetry("1", LF + "\t" + RF);
                robot.addToTelemetry("2", LB + "\t" + RB);
                LF *= -1;
                LB *= -1;
                RF *= -1;
                RB *= -1;
                robot.getLFMotor().setPower(LF * gearCoefficient);
                robot.getLBMotor().setPower(LB * gearCoefficient);
                robot.getRBMotor().setPower(RB * gearCoefficient);
                robot.getRFMotor().setPower(RF * gearCoefficient);

            } else {
//                robot.addToTelemetry("TeleOp if statement lvl", 2);
                robot.stopMotors();
            }

//            if (controller1.isPressed(JoystickController.BUTTON_LB)) {
//                //Grasp Relic
//                robot.getRelicArmClaw().setPosition(0);
//            } else if (controller1.isPressed(JoystickController.BUTTON_RB)) {
//                //Release Relic
//                robot.getRelicArmClaw().setPosition(1);
//            }

//            if (controller1.isDown(JoystickController.BUTTON_Y)) {
//                //Extend arm
//                robot.getRelicArmWinch().setPower(relicArmSpeed);
//            } else if (controller1.isDown(JoystickController.BUTTON_A)) {
//                //Retract arm
//                robot.getRelicArmWinch().setPower(-relicArmSpeed);
//            } else {
//                robot.getRelicArmWinch().setPower(0);
//            }

//            if (controller1.isDown(JoystickController.BUTTON_B)) {
//                //Raise wrist
//                robot.getRelicArmWrist().setPosition(0.5);
//            } else if (controller1.isDown(JoystickController.BUTTON_X)) {
//                //Lower wrist
//                robot.getRelicArmWrist().setPosition(0);
//            }

            if (controller1.isDpadUp()) {
                robot.getLift().setPower(liftSpeed);
            } else if (controller1.isDpadDown()) {
                robot.getLift().setPower(-liftSpeed);
            } else {
                robot.getLift().setPower(0);
            }

            if (controller1.isDown(JoystickController.BUTTON_A)) {
                robot.moveClaw(false);
            } else if (controller1.isDown(JoystickController.BUTTON_B)) {
                robot.moveClaw(true);
            }

            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
