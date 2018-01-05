package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.RedNearAutoLogic;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

@Autonomous(name = "Autonomous: Red Near Full", group = "Competition")
public class RedNearAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        setLogicThread(RedNearAutoLogic.class);
    }
}
