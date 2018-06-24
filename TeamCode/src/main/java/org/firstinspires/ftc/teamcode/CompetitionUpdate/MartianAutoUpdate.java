package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.competition.MartianAutoLogic;

@Autonomous(group = "Competition", name = "Z U C C on those martians")
public class MartianAutoUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        this.logicThread = MartianAutoLogic.class;
    }
}
