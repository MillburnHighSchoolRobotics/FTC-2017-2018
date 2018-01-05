package virtualRobot.logicThreads.competition

import virtualRobot.JoystickController
import virtualRobot.LogicThread
import virtualRobot.commands.Command
import virtualRobot.commands.Translate

/**
 * Created by Ethan Mak on 8/29/2017.
 */

class TeleOpLogic : LogicThread() {
    override fun addPresets() {
        shouldStartISR = false
    }

    @Throws(InterruptedException::class)
    override fun realRun() {
        val controller1: JoystickController
        val controller2: JoystickController
        controller1 = robot.joystickController1
        controller2 = robot.joystickController2
        var direction: Translate.Direction? = null
        var lastDirection: Translate.Direction? = null
        var headingMovement: Translate? = null
        while (true) {
            controller1.logicalRefresh()
            controller2.logicalRefresh()
            var movementTheta = Math.toDegrees(controller1.getValue(JoystickController.THETA_1)) //movement angle
            if (movementTheta < 0) movementTheta += 360.0
            val movementMag = Math.toDegrees(controller1.getValue(JoystickController.R_1)) //movement magnitude
            //calc direction
            if (movementMag < 0.1) {
                direction = null
            } else if (movementTheta >= 67.5 && movementTheta < 112.5) {
                direction = Translate.Direction.FORWARD
            } else if (movementTheta >= 112.5 && movementTheta < 157.5) {
                direction = Translate.Direction.FORWARD_RIGHT
            } else if (movementTheta >= 157.5 && movementTheta < 202.5) {
                direction = Translate.Direction.RIGHT
            } else if (movementTheta >= 202.5 && movementTheta < 247.5) {
                direction = Translate.Direction.BACKWARD_RIGHT
            } else if (movementTheta >= 247.5 && movementTheta < 292.5) {
                direction = Translate.Direction.BACKWARD
            } else if (movementTheta >= 292.5 && movementTheta < 337.5) {
                direction = Translate.Direction.BACKWARD_LEFT
            } else if (movementTheta >= 337.5 || movementTheta < 22.5) {
                direction = Translate.Direction.LEFT
            } else if (movementTheta >= 22.5 && movementTheta < 67.5) {
                direction = Translate.Direction.FORWARD_LEFT
            }
            if (direction != null && headingMovement == null) {
                headingMovement = Translate(Translate.RunMode.HEADING_ONLY, direction, 0.0, 1000.0)
                runCommand(headingMovement)
            } else if (direction == null && headingMovement != null) {
                headingMovement.stopCommand()
                headingMovement = null
            } else if (direction != null && lastDirection != null && direction != lastDirection) {
                headingMovement!!.direction = direction
            }
            lastDirection = direction
            //TODO: Use THETA_2 to rotate
            if (Thread.currentThread().isInterrupted)
                throw InterruptedException()
            Thread.sleep(10)
        }
    }
}
