package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.VuforiaVuMarkTestLogic;

/**
 * Created by david on 1/24/18.
 */

@Autonomous(name = "Vuforia Tester", group = "Testing")
public class VuforiaVuMarkTestUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        this.logicThread = VuforiaVuMarkTestLogic.class;
    }
}
