package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.RedFarBasicAutoLogic;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Red 2 Basic", group = "Competition")
public class RedFarBasicAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = RedFarBasicAutoLogic.class;
    }
}
