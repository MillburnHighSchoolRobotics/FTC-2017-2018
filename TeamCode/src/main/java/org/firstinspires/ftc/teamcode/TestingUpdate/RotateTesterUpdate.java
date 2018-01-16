package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.RotateTesterLogic;

/**
 * Created by david on 1/16/18.
 */
@Autonomous(name="Rotate Tester Loop", group="Testing")
public class RotateTesterUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        this.logicThread = RotateTesterLogic.class;
    }
}
