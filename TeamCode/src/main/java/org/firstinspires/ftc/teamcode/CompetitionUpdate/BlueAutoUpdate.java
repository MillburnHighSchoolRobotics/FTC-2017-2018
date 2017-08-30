package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.BlueAutoLogic;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

@Autonomous(name = "Autonomous: Blue Auton", group = "Competition")
public class BlueAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = BlueAutoLogic.class;
    }
}
