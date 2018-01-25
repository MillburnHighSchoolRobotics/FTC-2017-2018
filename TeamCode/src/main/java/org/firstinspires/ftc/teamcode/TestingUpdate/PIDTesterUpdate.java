package org.firstinspires.ftc.teamcode.TestingUpdate;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.PIDTesterLogic;

/**
 * Created by Ethan Mak on 8/30/2017.
 */

public class PIDTesterUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = PIDTesterLogic.class;
    }
}
