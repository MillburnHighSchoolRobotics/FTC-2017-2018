package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.testing.LogicThreadSanityCheckAutoLogic;

/**
 * Created by david on 10/6/17.
 */
@Autonomous(name = "Logic Thread Sanity Check Auto Test Thing", group = "Testing")
public class LogicThreadSanityCheckAutoUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = LogicThreadSanityCheckAutoLogic.class;
    }
}
