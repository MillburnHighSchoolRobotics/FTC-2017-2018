package org.firstinspires.ftc.teamcode.UpdateOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.TestBackendLogic;

/**
 * Created by Ethan Mak on 8/28/2017.
 */

@Autonomous(name="TestBackend", group="Testing")
public class TestBackendUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = TestBackendLogic.class;
    }
}
