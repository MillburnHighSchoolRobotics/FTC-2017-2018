package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by david on 10/26/17.
 */

@Autonomous( name="SoumyaClass", group="memes" )
public class SoumyaClass extends OpMode {
    Servo clawLeft, clawRight;
    double pos;
    long lastTime;
    public void init() {
        pos = 0;
        clawLeft = hardwareMap.servo.get("clawLeft");
        clawRight = hardwareMap.servo.get("clawRight");
        lastTime = System.currentTimeMillis();
    }
    public void loop() {
        if (System.currentTimeMillis() - lastTime > 1000) {
            pos = 1 - pos;
            lastTime = System.currentTimeMillis();
        }
    }
}
