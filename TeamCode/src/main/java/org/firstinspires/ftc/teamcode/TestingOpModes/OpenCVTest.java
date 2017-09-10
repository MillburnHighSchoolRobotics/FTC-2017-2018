package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.hardware.camera2.CameraDevice;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.UpdateThread;
import org.opencv.android.Camera2Renderer;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.exceptions.CameraException;

/**
 * Created by Ethan Mak on 8/31/2017.
 */

@Autonomous(name = "Testing: OpenCV Test", group = "Testing")
public class OpenCVTest extends OpMode {
    ElapsedTime time = new ElapsedTime();
    CameraDevice cap;
    VuforiaLocalizerImplSubclass vuforiaInstance;

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d("OpenCV", "OpenCV not loaded");
        } else {
            Log.d("OpenCV", "OpenCV loaded");
        }
    }

    @Override
    public void init() {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
        vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
    }

    @Override
    public void loop() {
        Mat img = new Mat();
        telemetry.addData("Data: ", img.cols());
    }

    @Override
    public void stop() {

    }
}
