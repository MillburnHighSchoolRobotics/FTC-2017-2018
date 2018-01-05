package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.JoystickTelemetryLogic;

/**
 * Created by david on 9/29/17.
 */

@TeleOp( name = "Testing: Joystick", group = "Testing")
public class JoystickUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        setLogicThread(JoystickTelemetryLogic.class);
    }
}
