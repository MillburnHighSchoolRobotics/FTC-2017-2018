package org.firstinspires.ftc.teamcode.TestingUpdate;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.TranslateAutoPIDLogic;

/**
 * Created by Ethan Mak on 8/30/2017.
 */

public class TranslateAutoPIDUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = TranslateAutoPIDLogic.class;
    }
}
