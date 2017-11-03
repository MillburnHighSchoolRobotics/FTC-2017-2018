package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.Blue1AutoLogic;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

@Autonomous(name = "Autonomous: Blue 1 Full", group = "Competition")
public class BlueNearAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = Blue1AutoLogic.class;
    }
}
