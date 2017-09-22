package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.Red2AutoLogic;

/**
 * Created by ethan on 9/22/17.
 */

public class Red2AutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = Red2AutoLogic.class;
    }
}
