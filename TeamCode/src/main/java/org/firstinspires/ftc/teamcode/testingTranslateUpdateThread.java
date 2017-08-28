package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import virtualRobot.godThreads.deprecated.testingTranslateGodThread;

/**
 * Created by 17osullivand on 11/4/16.
 */
@Autonomous(name = "Testing: Translate", group = "Testing")
public class testingTranslateUpdateThread extends UpdateThread {
    @Override
    public void setGodThread() {
        godThread = testingTranslateGodThread.class;
    }

    @Override
    public void addPresets() {

    }
}
