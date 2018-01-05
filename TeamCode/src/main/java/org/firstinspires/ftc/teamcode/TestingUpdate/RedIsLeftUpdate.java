package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.RedIsLeftLogic;

/**
 * Created by david on 11/15/17.
 */
@Autonomous( name="RedIsLeftTester", group="Testing" )
public class RedIsLeftUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = RedIsLeftLogic.class;
    }
}
