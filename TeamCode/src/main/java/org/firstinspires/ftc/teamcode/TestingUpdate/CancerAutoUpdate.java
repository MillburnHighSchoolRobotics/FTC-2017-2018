package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.CancerAutoLogic;

/**
 * Created by david on 10/6/17.
 */
@Autonomous(name = "Cancer Auto Test Thing", group = "Autistomous")
public class CancerAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = CancerAutoLogic.class;
    }
}
