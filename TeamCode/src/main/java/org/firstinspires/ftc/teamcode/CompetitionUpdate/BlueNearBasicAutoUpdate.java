package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.competition.BlueNearBasicAutoLogic;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Blue Near Basic", group = "Competition")
public class BlueNearBasicAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        setLogicThread(BlueNearBasicAutoLogic.class);
    }
}
