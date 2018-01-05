package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.TeleOpCustomLogic;
import virtualRobot.logicThreads.competition.TeleOpLogic;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

@TeleOp(name = "TeleOp: Run TeleOp", group = "Competition")
public class TeleOpUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        setLogicThread(TeleOpCustomLogic.class);
    }
}
