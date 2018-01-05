package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.ColorSensorTestLogic;

/**
 * Created by david on 11/3/17.
 */

@Autonomous( name="Color Sensor Test", group="Testing" )
public class ColorSensorTestUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = ColorSensorTestLogic.class;
    }
}
