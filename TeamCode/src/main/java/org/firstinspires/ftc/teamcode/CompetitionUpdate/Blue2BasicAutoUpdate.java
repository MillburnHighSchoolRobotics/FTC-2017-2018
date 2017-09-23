package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.Blue2BasicAutoLogic;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Blue 2 Basic", group = "Competition")
public class Blue2BasicAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = Blue2BasicAutoLogic.class;
    }
}
