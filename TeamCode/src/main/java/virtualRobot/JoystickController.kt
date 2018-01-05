package virtualRobot

import android.util.Log

import com.qualcomm.robotcore.exception.RobotCoreException
import com.qualcomm.robotcore.hardware.Gamepad

import java.util.ArrayList
import java.util.Collections
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReferenceArray

import virtualRobot.commands.Command

/**
 * Created by Yanjun on 11/12/2015.
 * Represents the joystick
 */
class JoystickController {
    internal var down: AtomicReferenceArray<Boolean>
    internal var pressed: AtomicReferenceArray<Boolean>
    internal var released: AtomicReferenceArray<Boolean>
    internal var stickValues: AtomicReferenceArray<Double>
    internal var dpad_up: AtomicBoolean
    internal var dpad_down: AtomicBoolean
    internal var dpad_left: AtomicBoolean
    internal var dpad_right: AtomicBoolean
    internal var eventQueue: List<JoystickEvent>

    internal var buffer: JoystickEvent? = null

    val isDpadUp: Boolean
        get() = dpad_up.get()

    val isDpadDown: Boolean
        get() = dpad_down.get()

    val isDpadLeft: Boolean
        get() = dpad_left.get()

    val isDpadRight: Boolean
        get() = dpad_right.get()

    init {
        down = AtomicReferenceArray(12)
        pressed = AtomicReferenceArray(12)
        released = AtomicReferenceArray(12)

        for (i in 0..11) {
            down.set(i, false)
            pressed.set(i, false)
            released.set(i, false)
        }

        stickValues = AtomicReferenceArray(10)

        for (i in 0..9) {
            stickValues.set(i, 0.0)
        }

        dpad_up = AtomicBoolean()
        dpad_down = AtomicBoolean()
        dpad_left = AtomicBoolean()
        dpad_right = AtomicBoolean()

        dpad_up.set(false)
        dpad_down.set(false)
        dpad_left.set(false)
        dpad_right.set(false)

        eventQueue = Collections.synchronizedList(ArrayList())

        buffer = null
    }

    @Synchronized
    @Throws(RobotCoreException::class)
    fun copyStates(gamepad: Gamepad) {
        buffer = JoystickEvent(gamepad)
        //        if (eventQueue.size() != 0 && newEvent.equals(eventQueue.get(eventQueue.size()-1))) {
        //            return;
        //        }

        //        Command.ROBOT.addToTelemetry("Gamepad Direct Values", gamepad.left_stick_x + " " + gamepad.left_stick_y);
        //        Command.ROBOT.addToTelemetry("Joystick Event Values", newEvent.stickValues[JoystickEvent.X_1] + " " + newEvent.stickValues[JoystickEvent.Y_1]);


        //        synchronized (this) {
        //        	eventQueue.add(newEvent);
        //        }

    }

    @Synchronized
    fun logicalRefresh() {
        if (buffer == null) return
        for (i in 0..11) {
            pressed.set(i, !down.get(i) && buffer!!.buttonStates[i])
            released.set(i, down.get(i) && !buffer!!.buttonStates[i])
            down.set(i, buffer!!.buttonStates[i])
        }

        for (i in 0 until stickValues.length()) {
            stickValues.set(i, buffer!!.stickValues[i])
        }

        dpad_up.set(buffer!!.dpad_up)
        dpad_down.set(buffer!!.dpad_down)
        dpad_left.set(buffer!!.dpad_left)
        dpad_right.set(buffer!!.dpad_right)
        //    	synchronized (this) {
        //
        //	        if (eventQueue.size() == 0) {
        //	            curEvent = prevEvent;
        //	        } else {
        //
        //	            prevEvent = curEvent;
        //	            curEvent = eventQueue.remove(0);
        //	        }
        //
        //	        if (prevEvent != null && curEvent != null) {
        ////                Log.d("JoystickController", "refreshed");
        //	            for (int i = 0; i < 12; i++) {
        //	                pressed.set(i, !prevEvent.buttonStates[i] && curEvent.buttonStates[i]);
        //	                released.set(i, prevEvent.buttonStates[i] && !curEvent.buttonStates[i]);
        //	                down.set(i, curEvent.buttonStates[i]);
        //	            }
        //
        //	            for (int i = 0; i < stickValues.length(); i++) {
        //	                stickValues.set(i, curEvent.stickValues[i]);
        //	            }
        //
        //	            dpad_up.set(curEvent.dpad_up);
        //	            dpad_down.set(curEvent.dpad_down);
        //	            dpad_left.set(curEvent.dpad_left);
        //	            dpad_right.set(curEvent.dpad_right);
        //	        }
        //    	}
    }

    fun isDown(buttonID: Int): Boolean {
        return down.get(buttonID)
    }

    fun isPressed(buttonID: Int): Boolean {
        return pressed.get(buttonID)
    }

    fun isReleased(buttonID: Int): Boolean {
        return released.get(buttonID)
    }

    fun getValue(ID: Int): Double {
        return stickValues.get(ID)
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
