package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.logicThreads.ExampleLogic;

/**
 * Created by Ethan Mak on 8/28/2017.
 *
 * An example of what subclasses of UpdateThread should look like
 */

@Autonomous(name="Example: Test", group="Example")
public class ExampleUpdate extends UpdateThread {
    @Override
    public void setLogicThread() {
        setLogicThread(ExampleLogic.class);
    }
}
