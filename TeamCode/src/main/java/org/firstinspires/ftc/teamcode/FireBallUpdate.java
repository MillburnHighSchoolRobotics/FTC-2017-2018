package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import virtualRobot.godThreads.deprecated.FireBallsGodThread;

/**
 * Created by 17osullivand on 11/27/16.
 */

@Autonomous(name = "Testing: FIRE BALLS ONLY", group =  "Autonomous")
public class FireBallUpdate extends UpdateThread {
    @Override
    public void setGodThread() {
        godThread = FireBallsGodThread.class;
    }
}
