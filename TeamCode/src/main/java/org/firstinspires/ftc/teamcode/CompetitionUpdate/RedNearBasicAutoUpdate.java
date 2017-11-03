package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.Red1BasicAutoLogic;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Red 1 Basic", group = "Competition")
public class RedNearBasicAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = Red1BasicAutoLogic.class;
    }
}
