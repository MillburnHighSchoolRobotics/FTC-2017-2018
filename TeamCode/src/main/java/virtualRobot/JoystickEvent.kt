package virtualRobot

import android.util.Log

import com.qualcomm.robotcore.hardware.Gamepad

/**
 * Created by shant on 11/14/2015.
 * When anything happens on the joystick
 */
class JoystickEvent(gamepad: Gamepad) {

    var buttonStates = BooleanArray(12)
    var stickValues = DoubleArray(10)
    var dpad_up: Boolean = false
    var dpad_down: Boolean = false
    var dpad_left: Boolean = false
    var dpad_right: Boolean = false

    init {

        var x1: Double
        var y1: Double
        var x2: Double
        var y2: Double

        synchronized(gamepad) {
            buttonStates[BUTTON_X] = gamepad.x
            buttonStates[BUTTON_A] = gamepad.a
            buttonStates[BUTTON_Y] = gamepad.y
            buttonStates[BUTTON_B] = gamepad.b

            buttonStates[BUTTON_RB] = gamepad.right_bumper
            buttonStates[BUTTON_LB] = gamepad.left_bumper

            val rightTriggerValue = gamepad.right_trigger.toDouble()
            val leftTriggerValue = gamepad.left_trigger.toDouble()

            buttonStates[BUTTON_RT] = rightTriggerValue > 0.7
            buttonStates[BUTTON_LT] = leftTriggerValue > 0.7

            stickValues[RT_PRESSURE] = rightTriggerValue
            stickValues[LT_PRESSURE] = leftTriggerValue


            buttonStates[BUTTON_START] = gamepad.start
            buttonStates[BUTTON_BACK] = gamepad.back
            buttonStates[BUTTON_LEFT_STICK] = gamepad.left_stick_button
            buttonStates[BUTTON_RIGHT_STICK] = gamepad.right_stick_button


            x1 = gamepad.left_stick_x.toDouble()
            y1 = (-gamepad.left_stick_y).toDouble()
            x2 = gamepad.right_stick_x.toDouble()
            y2 = (-gamepad.right_stick_y).toDouble()

        }

        val SQRT_2 = Math.sqrt(2.0)
        var radius1 = Math.sqrt(x1 * x1 + y1 * y1)
        Log.d("gamepad", radius1.toString() + " " + x1 + " " + y1)
        var angle1 = Math.atan2(y1, x1)

        var radius2 = Math.sqrt(x2 * x2 + y2 * y2)
        var angle2 = Math.atan2(y2, x2)

        if (x1 == 1.0 || x1 == -1.0 || y1 == 1.0 || y1 == -1.0) {
            radius1 = SQRT_2

            if (x1 == 1.0) {
                angle1 = Math.asin(y1 / radius1)
            } else if (x1 == -1.0) {
                angle1 = Math.PI - Math.asin(y1 / radius1)
            } else if (y1 == 1.0) {
                angle1 = Math.acos(x1 / radius1)
            } else if (y1 == -1.0) {
                angle1 = 2 * Math.PI - Math.acos(x1 / radius1)
            }
        }

        x1 = radius1 * Math.cos(angle1) * SQRT_2 * 0.5
        y1 = radius1 * Math.sin(angle1) * SQRT_2 * 0.5

        if (x2 == 1.0 || x2 == -1.0 || y2 == 1.0 || y2 == -1.0) {
            radius2 = SQRT_2

            if (x2 == 1.0) {
                angle2 = Math.asin(y2 / radius2)
            } else if (x2 == -1.0) {
                angle2 = Math.PI - Math.asin(y2 / radius2)
            } else if (y2 == 1.0) {
                angle2 = Math.acos(x2 / radius2)
            } else if (y2 == -1.0) {
                angle2 = 2 * Math.PI - Math.acos(x2 / radius2)
            }
        }

        x2 = radius2 * Math.cos(angle2) * SQRT_2 * 0.5
        y2 = radius2 * Math.sin(angle2) * SQRT_2 * 0.5

        stickValues[X_1] = x1
        stickValues[Y_1] = y1
        stickValues[R_1] = radius1
        stickValues[THETA_1] = angle1

        stickValues[X_2] = x2
        stickValues[Y_2] = y2
        stickValues[R_2] = radius2
        stickValues[THETA_2] = angle2

        dpad_down = gamepad.dpad_down
        dpad_up = gamepad.dpad_up
        dpad_left = gamepad.dpad_left
        dpad_right = gamepad.dpad_right

    }

    fun equals(other: JoystickEvent): Boolean {
        for (i in buttonStates.indices) {
            if (buttonStates[i] != other.buttonStates[i]) {
                return false
            }
        }

        for (i in stickValues.indices) {
            if (stickValues[i] != other.stickValues[i]) {
                return false
            }
        }

        return if (dpad_up != other.dpad_up && dpad_down != other.dpad_down && dpad_left != other.dpad_left && dpad_right && other.dpad_left) {
            false
        } else true

    }

    companion object {

        var BUTTON_X = 0
        var BUTTON_A = 1
        var BUTTON_Y = 2
        var BUTTON_B = 3
        var BUTTON_LB = 4
        var BUTTON_RB = 5
        var BUTTON_LT = 6
        var BUTTON_RT = 7
        var BUTTON_BACK = 8
        var BUTTON_START = 9
        var BUTTON_LEFT_STICK = 10
        var BUTTON_RIGHT_STICK = 11

        var X_1 = 0
        var Y_1 = 1
        var R_1 = 2
        var THETA_1 = 3
        var X_2 = 4
        var Y_2 = 5
        var R_2 = 6
        var THETA_2 = 7
        var RT_PRESSURE = 8
        var LT_PRESSURE = 9
    }
}
