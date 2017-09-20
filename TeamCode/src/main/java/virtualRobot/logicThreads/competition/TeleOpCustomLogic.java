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
//        Translate headingMovement = null;
        while (true) {
            controller1.logicalRefresh();
            controller2.logicalRefresh();
            double movementTheta = Math.toDegrees(controller1.getValue(JoystickController.THETA_1)); //movement angle
            if (movementTheta < 0) movementTheta += 360;
            double movementMag = Math.toDegrees(controller1.getValue(JoystickController.R_1)); //movement magnitude
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
            double scale = 0;
            double LF;
            double RF;
            double LB;
            double RB;
            LF = 0;
            RF = 0;
            LB = 0;
            RB = 0;
//            int movementAngle = direction.getAngle();
            int movementAngle = (int)movementTheta;
            movementAngle = 180 - movementAngle; // transform to standard angle
            if (movementAngle < 0) movementAngle += 360;
            double power = 1; //set power later? Use magnitude?
            if (movementAngle >= 0 && movementAngle <= 90) { //quadrant 1
                scale = MathUtils.sinDegrees(movementAngle - 45) / MathUtils.cosDegrees(movementAngle - 45);
                LF = power * POWER_MATRIX[0][0];
                RF = power * POWER_MATRIX[0][1] * scale;
                LB = power * POWER_MATRIX[0][2] * scale;
                RB = power * POWER_MATRIX[0][3];
            } else if (movementAngle > 90 && movementAngle <= 180) { //quadrant 2
                power *= -1;
                scale = MathUtils.sinDegrees(movementAngle - 135) / MathUtils.cosDegrees(movementAngle - 135);
                LF = (power * POWER_MATRIX[2][0] * scale);
                RF = (power * POWER_MATRIX[2][1]);
                LB = (power * POWER_MATRIX[2][2]);
                RB = (power * POWER_MATRIX[2][3] * scale);
            } else if (movementAngle > 180 && movementAngle <= 270) { //quadrant 3
                scale = MathUtils.sinDegrees(movementAngle - 225) / MathUtils.cosDegrees(movementAngle - 225);
                LF = (power * POWER_MATRIX[4][0]);
                RF = (power * POWER_MATRIX[4][1] * scale);
                LB = (power * POWER_MATRIX[4][2] * scale);
                RB = (power * POWER_MATRIX[4][3]);
//                Log.d("aaa", robot.getLFMotor().getPower() + " " + robot.getRFMotor().getPower() + " " + robot.getLBMotor().getPower() + " " + robot.getRBMotor().getPower());
            } else if (movementAngle > 270 && movementAngle < 360) { //quadrant 4
                power *= -1;
                scale = MathUtils.sinDegrees(movementAngle - 315) / MathUtils.cosDegrees(movementAngle - 315);
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
            lastDirection = direction;
            //TODO: Use THETA_2 to rotate
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
