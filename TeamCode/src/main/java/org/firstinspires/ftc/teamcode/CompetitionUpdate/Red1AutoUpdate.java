package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.Red1AutoLogic;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

@Autonomous(name = "Autonomous: Red 1 Full", group = "Competition")
public class Red1AutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = Red1AutoLogic.class;
    }
}
