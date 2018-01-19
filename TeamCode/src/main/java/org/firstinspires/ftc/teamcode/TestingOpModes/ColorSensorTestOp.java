package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Ethan Mak on 1/18/2018.
 */

@Autonomous(name="Sensor: Color Sensor Test No Backend", group = "Sensor")
public class ColorSensorTestOp extends OpMode {
    ColorSensor sensor;
    @Override
    public void init() {
        sensor = (ColorSensor) hardwareMap.get("colorSensor");
    }

    @Override
    public void loop() {
        telemetry.addData("RED: ", sensor.red());
        telemetry.addData("GREEN: ", sensor.green());
        telemetry.addData("BLUE: ", sensor.blue());
        telemetry.addData("ALPHA: ", sensor.alpha());
    }

    @Override
    public void stop() {
        sensor.close();
    }
}
