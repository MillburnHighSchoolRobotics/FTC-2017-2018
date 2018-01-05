package org.firstinspires.ftc.teamcode.TestingUpdate;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.PIDTesterLogic;

/**
 * Created by Ethan Mak on 8/30/2017.
 */

public class PIDTesterUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        setLogicThread(PIDTesterLogic.class);
    }
}
