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
    protected void realRun() throws InterruptedException {
        JoystickController controller1;
        JoystickController controller2;
        controller1 = robot.getJoystickController1();
        controller2 = robot.getJoystickController2();
        Translate.Direction direction = null;
        Translate.Direction lastDirection = null;
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
        double intakeDirectionSensitivity = 1; //TODO: tune speed
        double intakeElevationSensitivity = 1; //TODO: tune speed
        double relicArmSpeed = 1; //TODO: tune speed
//        Translate headingMovement = null;
//        int lastAction = 0; //0 for stopped, 1 for translating, 2 for rotating
        while (true) {
            controller1.logicalRefresh();
            controller2.logicalRefresh();
            double translateTheta = Math.toDegrees(controller1.getValue(JoystickController.THETA_1)); //movement angle
            double rotateX = Math.toDegrees(controller1.getValue(JoystickController.X_2)); //rotation angle
            if (translateTheta < 0) translateTheta += 360;
//            if (rotateTheta < 0) rotateTheta += 360;
            double translateMag = Math.toDegrees(controller1.getValue(JoystickController.R_1)); //movement magnitude
            double rotateMag = Math.toDegrees(controller1.getValue(JoystickController.R_2)); //rotation magnitude
            //calc direction
//            if (movementMag < 0.1) {
//                direction = null;
//            } else if (movementTheta >= 67.5 && movementTheta < 112.5) {
//                direction = Translate.Direction.FORWARD;
//            } else if (movementTheta >= 112.5 && movementTheta < 157.5) {
//                direction = Translate.Direction.FORWARD_RIGHT;
//            } else if (movementTheta >= 157.5 && movementTheta < 202.5) {
//                direction = Translate.Direction.RIGHT;
//            } else if (movementTheta >= 202.5 && movementTheta < 247.5) {
//                direction = Translate.Direction.BACKWARD_RIGHT;
//            } else if (movementTheta >= 247.5 && movementTheta < 292.5) {
//                direction = Translate.Direction.BACKWARD;
//            } else if (movementTheta >= 292.5 && movementTheta < 337.5) {
//                direction = Translate.Direction.BACKWARD_LEFT;
//            } else if (movementTheta >= 337.5 || movementTheta < 22.5) {
//                direction = Translate.Direction.LEFT;
//            } else if (movementTheta >= 22.5 && movementTheta < 67.5) {
//                direction = Translate.Direction.FORWARD_LEFT;
//            }
            double scale;
            double LF;
            double RF;
            double LB;
            double RB;
            LF = 0;
            RF = 0;
            LB = 0;
            RB = 0;
//            int movementAngle = direction.getAngle();
            translateAngle = (int)translateTheta;
            translateAngle = 180 - translateAngle; // transform to standard angle
            if (translateAngle < 0) translateAngle += 360;
            double power = 1; //set power later? Use magnitude?
            if (!MathUtils.equals(translateMag, 0, 0.1)) {
                if (translateAngle >= 0 && translateAngle <= 90) { //quadrant 1
                    scale = MathUtils.sinDegrees(translateAngle - 45) / MathUtils.cosDegrees(translateAngle - 45);
                    LF = power * POWER_MATRIX[0][0];
                    RF = power * POWER_MATRIX[0][1] * scale;
                    LB = power * POWER_MATRIX[0][2] * scale;
                    RB = power * POWER_MATRIX[0][3];
                } else if (translateAngle > 90 && translateAngle <= 180) { //quadrant 2
                    power *= -1;
                    scale = MathUtils.sinDegrees(translateAngle - 135) / MathUtils.cosDegrees(translateAngle - 135);
                    LF = (power * POWER_MATRIX[2][0] * scale);
                    RF = (power * POWER_MATRIX[2][1]);
                    LB = (power * POWER_MATRIX[2][2]);
                    RB = (power * POWER_MATRIX[2][3] * scale);
                } else if (translateAngle > 180 && translateAngle <= 270) { //quadrant 3
                    scale = MathUtils.sinDegrees(translateAngle - 225) / MathUtils.cosDegrees(translateAngle - 225);
                    LF = (power * POWER_MATRIX[4][0]);
                    RF = (power * POWER_MATRIX[4][1] * scale);
                    LB = (power * POWER_MATRIX[4][2] * scale);
                    RB = (power * POWER_MATRIX[4][3]);
//                Log.d("aaa", robot.getLFMotor().getPower() + " " + robot.getRFMotor().getPower() + " " + robot.getLBMotor().getPower() + " " + robot.getRBMotor().getPower());
                } else if (translateAngle > 270 && translateAngle < 360) { //quadrant 4
                    power *= -1;
                    scale = MathUtils.sinDegrees(translateAngle - 315) / MathUtils.cosDegrees(translateAngle - 315);
                    LF = (power * POWER_MATRIX[6][0] * scale);
                    RF = (power * POWER_MATRIX[6][1]);
                    LB = (power * POWER_MATRIX[6][2]);
                    RB = (power * POWER_MATRIX[6][3] * scale);
                }

                robot.getLFMotor().setPower(LF);
                robot.getLBMotor().setPower(LB);
                robot.getRBMotor().setPower(RB);
                robot.getRFMotor().setPower(RF);


//            if (direction != null && headingMovement == null) {
//                headingMovement = new Translate(Translate.RunMode.HEADING_ONLY, direction, 0, 1000);
//                runCommand((headingMovement));
//            } else if (direction == null && headingMovement != null) {
//                headingMovement.stopCommand();
//                headingMovement = null;
//            } else if (direction != null && lastDirection != null && direction != lastDirection) {
//                headingMovement.setDirection(direction);
//            }
                lastTranslateAngle = translateAngle;
                lastDirection = direction;
            } else if (!MathUtils.equals(rotateMag, 0, 0.1)) {
                int rotationDirection = (int)Math.signum(rotateX);
                int rotationPower = 1; //set using rotationMag
                //Double check that these turn in the right direction
                robot.getLFMotor().setPower(rotationDirection * rotationPower);
                robot.getLBMotor().setPower(rotationDirection * rotationPower);
                robot.getRFMotor().setPower(-rotationDirection * rotationPower);
                robot.getRBMotor().setPower(-rotationDirection * rotationPower);
            } else {
                robot.stopMotors();
            }


            if (controller1.isDpadUp()) {
                robot.getRelicArm().setPower(relicArmSpeed);
            } else if (controller1.isDpadDown()) {
                robot.getRelicArm().setPower(-relicArmSpeed);
            } else {
                robot.getRelicArm().setPower(0);
            }

            if (controller1.isPressed(JoystickController.BUTTON_LB)) {
                //Grasp Relic
            } else if (controller1.isPressed(JoystickController.BUTTON_RB)) {
                //Release Relic
            }

            if (controller2.isPressed(JoystickController.BUTTON_A)) robot.moveClaw(false);
            else if (controller2.isPressed(JoystickController.BUTTON_B)) robot.moveClaw(true);

            double intakeDirectionY = controller2.getValue(JoystickController.Y_1);
            double intakeElevationY = controller2.getValue(JoystickController.Y_2);

            if (!MathUtils.equals(intakeDirectionY, 0, 0.1)) {
                robot.getRollerLeft().setPower(intakeDirectionY * intakeDirectionSensitivity);
                robot.getRollerRight().setPower(-intakeDirectionY * intakeDirectionSensitivity);
            } else {
                robot.getRollerLeft().setPower(0);
                robot.getRollerRight().setPower(0);
            }

            if (!MathUtils.equals(intakeElevationY, 0, 0.1)) {
                robot.getGlyphLiftLeft().setPower(intakeElevationY * intakeElevationSensitivity);
                robot.getGlyphLiftRight().setPower(intakeElevationY * intakeElevationSensitivity);
            } else {
                robot.getGlyphLiftLeft().setPower(0);
                robot.getGlyphLiftRight().setPower(0);
            }



            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
