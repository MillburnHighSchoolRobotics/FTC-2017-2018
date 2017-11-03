package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.Blue2BasicAutoLogic;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Blue Far Basic", group = "Competition")
public class BlueFarBasicAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = Blue2BasicAutoLogic.class;
    }
}
