package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.TeleOpCustomLogic;
import virtualRobot.logicThreads.competition.TeleOpCustomLogicRewrite;

/**
 * Created by david on 9/29/17.
 */
@TeleOp(name = "TeleOp: Run TeleOp Rewrite", group = "Competition")
public class TeleOpRewriteUpdate extends UpdateThread {
    public void setLogicThread() {
        logicThread = TeleOpCustomLogic.class;
    }
}
