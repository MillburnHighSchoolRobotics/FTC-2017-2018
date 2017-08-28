package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import virtualRobot.godThreads.deprecated.TakePictureTestGod;

/**
 * Created by mehme_000 on 10/15/2016.
 * Update Thread used for testing the camera
 */
@Autonomous(name ="Sensor: CameraOurBackend", group="Sensor")
public class TakePictureTestUpdate extends UpdateThread {

    @Override
    public void setGodThread() {
        godThread = TakePictureTestGod.class;

    }


    @Override
    public void addPresets() {

    }
}
