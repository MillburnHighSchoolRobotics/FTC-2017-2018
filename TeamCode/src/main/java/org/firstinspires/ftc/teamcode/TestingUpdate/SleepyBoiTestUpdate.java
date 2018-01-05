package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.SleepyBoiTestLogic;

/**
 * Created by david on 12/2/17.
 */

@Autonomous( name="Sleepy Boi Test", group = "Testing" )
public class SleepyBoiTestUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = SleepyBoiTestLogic.class;
    }
}
