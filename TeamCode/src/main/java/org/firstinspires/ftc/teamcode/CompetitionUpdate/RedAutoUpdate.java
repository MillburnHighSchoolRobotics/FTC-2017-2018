package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.LogicThread;
import virtualRobot.logicThreads.competition.RedAutoLogic;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

@Autonomous(name = "Autonomous: Red Auton", group = "Competition")
public class RedAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = RedAutoLogic.class;
    }
}
