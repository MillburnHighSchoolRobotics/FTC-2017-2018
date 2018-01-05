package virtualRobot.logicThreads.competition

import virtualRobot.JoystickController
import virtualRobot.LogicThread
import virtualRobot.commands.Translate
import virtualRobot.utils.MathUtils

/**
 * Created by Ethan Mak on 8/29/2017.
 */

class TeleOpCustomLogic : LogicThread() {
    override fun addPresets() {
        shouldStartISR = false
    }

    @Throws(InterruptedException::class)
    override fun realRun() {
        robot.jewelServo.position = 0.50
        val controller1: JoystickController
        val controller2: JoystickController
        controller1 = robot.joystickController1
        controller2 = robot.joystickController2
        val direction: Translate.Direction? = null
        val lastDirection: Translate.Direction? = null
        val intakePos = 0.0
        val lastIntakePosChange: Long = 0
        val translateAngle = 0
        val lastTranslateAngle = 0
        val POWER_MATRIX = arrayOf(//for each of the directions
                //RF, RB, LF, LB
                intArrayOf(1, 1, 1, 1), intArrayOf(1, 0, 0, 1), intArrayOf(1, -1, -1, 1), intArrayOf(0, -1, -1, 0), intArrayOf(-1, -1, -1, -1), intArrayOf(-1, 0, 0, -1), intArrayOf(-1, 1, 1, -1), intArrayOf(0, 1, 1, 0))
        val intakeDirectionSensitivity = 1.0 //TODO: tune speed
        val liftSpeed = 1.0 //TODO: tune speed
        val relicArmSpeed = 1.0 //TODO: tune speed
        var gearCoefficient = 0.666
        //        Translate headingMovement = null;
        //        int lastAction = 0; //0 for stopped, 1 for translating, 2 for rotating
        val isInterrupted = false
        while (!isInterrupted) {
            controller1.logicalRefresh()
            controller2.logicalRefresh()
            var translateTheta = Math.toDegrees(controller1.getValue(JoystickController.THETA_1))
            val translateMag = controller1.getValue(JoystickController.R_1)
            val rotateX = controller1.getValue(JoystickController.X_2)
            if (translateTheta < 0) translateTheta += 360.0
            val scale: Double
            var RF = 0.0
            var RB = 0.0
            var LF = 0.0
            var LB = 0.0
            robot.addToTelemetry("mag", translateMag)

            if (controller1.isDpadUp) {
                gearCoefficient = 0.666
            } else if (controller1.isDpadDown) {
                gearCoefficient = 0.333
            }
            if (!MathUtils.equals(rotateX, 0.0, 0.05)) {
                robot.rfMotor.power = rotateX * gearCoefficient
                robot.rbMotor.power = rotateX * gearCoefficient
                robot.lfMotor.power = -rotateX * gearCoefficient
                robot.lbMotor.power = -rotateX * gearCoefficient
                //                robot.addToTelemetry("TeleOp if statement lvl", 0);
            } else if (!MathUtils.equals(translateMag, 0.0, 0.05)) {
                var translatePower = translateMag * 0.666
                if (translateTheta >= 0 && translateTheta <= 90) { //quadrant 1
                    scale = MathUtils.sinDegrees(translateTheta - 45) / MathUtils.cosDegrees(translateTheta - 45)
                    LF = translatePower * POWER_MATRIX[0][0]
                    LB = translatePower * POWER_MATRIX[0][1].toDouble() * scale
                    RF = translatePower * POWER_MATRIX[0][2].toDouble() * scale
                    RB = translatePower * POWER_MATRIX[0][3]
                } else if (translateTheta > 90 && translateTheta <= 180) { //quadrant 2
                    translatePower *= -1.0
                    scale = MathUtils.sinDegrees(translateTheta - 135) / MathUtils.cosDegrees(translateTheta - 135)
                    LF = translatePower * POWER_MATRIX[2][0].toDouble() * scale
                    LB = translatePower * POWER_MATRIX[2][1]
                    RF = translatePower * POWER_MATRIX[2][2]
                    RB = translatePower * POWER_MATRIX[2][3].toDouble() * scale
                } else if (translateTheta > 180 && translateTheta <= 270) { //quadrant 3
                    scale = MathUtils.sinDegrees(translateTheta - 225) / MathUtils.cosDegrees(translateTheta - 225)
                    LF = translatePower * POWER_MATRIX[4][0]
                    LB = translatePower * POWER_MATRIX[4][1].toDouble() * scale
                    RF = translatePower * POWER_MATRIX[4][2].toDouble() * scale
                    RB = translatePower * POWER_MATRIX[4][3]
                    //                Log.d("aaa", robot.getLFMotor().getPower() + " " + robot.getRFMotor().getPower() + " " + robot.getLBMotor().getPower() + " " + robot.getRBMotor().getPower());
                } else if (translateTheta > 270 && translateTheta < 360) { //quadrant 4
                    translatePower *= -1.0
                    scale = MathUtils.sinDegrees(translateTheta - 315) / MathUtils.cosDegrees(translateTheta - 315)
                    LF = translatePower * POWER_MATRIX[6][0].toDouble() * scale
                    LB = translatePower * POWER_MATRIX[6][1]
                    RF = translatePower * POWER_MATRIX[6][2]
                    RB = translatePower * POWER_MATRIX[6][3].toDouble() * scale
                }
                robot.addToTelemetry("1", LF.toString() + "\t" + RF)
                robot.addToTelemetry("2", LB.toString() + "\t" + RB)
                LF *= -1.0
                LB *= -1.0
                RF *= -1.0
                RB *= -1.0
                robot.lfMotor.power = LF * gearCoefficient
                robot.lbMotor.power = LB * gearCoefficient
                robot.rbMotor.power = RB * gearCoefficient
                robot.rfMotor.power = RF * gearCoefficient

            } else {
                //                robot.addToTelemetry("TeleOp if statement lvl", 2);
                robot.stopMotors()
            }


            //            if (controller1.isDpadUp()) {
            //                robot.getRelicArm().setPower(relicArmSpeed);
            //            } else if (controller1.isDpadDown()) {
            //                robot.getRelicArm().setPower(-relicArmSpeed);
            //            } else {
            //                robot.getRelicArm().setPower(0);
            //            }

            if (controller1.isPressed(JoystickController.BUTTON_LB)) {
                //Grasp Relic
            } else if (controller1.isPressed(JoystickController.BUTTON_RB)) {
                //Release Relic
            }

            //            if (controller2.isPressed(JoystickController.BUTTON_A)) robot.moveClaw(false);
            //            else if (controller2.isPressed(JoystickController.BUTTON_B)) robot.moveClaw(true);
            //
            //            double intakeDirectionY = controller2.getValue(JoystickController.Y_1);
            //            double intakeElevationY = controller2.getValue(JoystickController.Y_2);
            //
            //            if (!MathUtils.equals(intakeDirectionY, 0, 0.1)) {
            //                robot.getRollerLeft().setSpeed(intakeDirectionY * intakeDirectionSensitivity);
            //                robot.getRollerRight().setSpeed(-intakeDirectionY * intakeDirectionSensitivity);
            //            } else {
            //                robot.getRollerLeft().setSpeed(0);
            //                robot.getRollerRight().setSpeed(0);
            //            }
            //
            //            if (!MathUtils.equals(intakeElevationY, 0, 0.1)) {
            //                robot.getLiftLeft().setPower(intakeElevationY * intakeElevationSensitivity);
            //                robot.getLiftRight().setPower(intakeElevationY * intakeElevationSensitivity);
            //            } else {
            //                robot.getLiftLeft().setPower(0);
            //                robot.getLiftRight().setPower(0);
            //            }

            val boxDirection = controller2.getValue(JoystickController.Y_1)
            val intakeDirection = controller2.getValue(JoystickController.Y_2)
            val intakeRotation = controller2.getValue(JoystickController.X_2)

            if (!MathUtils.equals(intakeDirection, 0.0, 0.1)) {
                robot.rollerLeft.power = intakeDirection * intakeDirectionSensitivity
                robot.rollerRight.speed = -intakeDirection * intakeDirectionSensitivity
            } else {
                robot.rollerLeft.power = 0.0
                robot.rollerRight.speed = 0.0
            }

            //            if (!MathUtils.equals(intakeRotation, 0, 0.1)) {
            //                robot.getClawLeft().setPosition(intakeRotation);
            //                robot.getClawRight().setPosition(1 - intakeRotation);
            //            }

            if (!MathUtils.equals(boxDirection, 0.0, 0.1)) {
                robot.boxLeft.speed = boxDirection * intakeDirectionSensitivity
                robot.boxRight.speed = -boxDirection * intakeDirectionSensitivity
            } else {
                robot.boxLeft.speed = 0.0
                robot.boxRight.speed = 0.0
            }

            //            if (controller2.isDown(JoystickController.BUTTON_RB) && System.currentTimeMillis() - lastIntakePosChange > 50) {
            //                intakePos = MathUtils.clamp(intakePos + 0.05, 0, 1);
            //                lastIntakePosChange = System.currentTimeMillis();
            //            } else if (controller2.isDown(JoystickController.BUTTON_LB) && System.currentTimeMillis() - lastIntakePosChange > 50) {
            //                intakePos = MathUtils.clamp(intakePos - 0.05, 0, 1);
            //                lastIntakePosChange = System.currentTimeMillis();
            //            }
            //            robot.getClawLeft().setPosition(intakePos);

            if (controller2.isDpadUp) {
                robot.liftLeft.power = liftSpeed
                robot.liftRight.power = liftSpeed - 0.08
            } else if (controller2.isDpadDown) {
                robot.liftLeft.power = -liftSpeed
                robot.liftRight.power = -liftSpeed + 0.08
            } else {
                robot.liftLeft.power = 0.0
                robot.liftRight.power = 0.0
            }

            robot.addToTelemetry("liftEncoders", robot.liftLeft.position.toString() + ", " + robot.liftRight.position)

            if (Thread.currentThread().isInterrupted)
                throw InterruptedException()
            Thread.sleep(10)
        }
    }
}
