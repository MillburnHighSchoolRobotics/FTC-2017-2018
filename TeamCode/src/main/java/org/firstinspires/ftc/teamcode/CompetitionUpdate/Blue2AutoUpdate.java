package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.Blue2AutoLogic;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Blue 2", group = "Competition")
public class Blue2AutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = Blue2AutoLogic.class;
    }
}
