package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.Red2AutoLogic;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Red 2 Full", group = "Competition")
public class RedFarAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = Red2AutoLogic.class;
    }
}
