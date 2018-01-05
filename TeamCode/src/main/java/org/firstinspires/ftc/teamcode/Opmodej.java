package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ethan on 9/25/17.
 */
@TeleOp(name = "Concept: navX Collisionds Detection", group = "Concept")
public class Opmodej extends OpMode{

    Servo vex = (Servo) hardwareMap.get("servov");
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        vex.setPosition(0.4);
    }
}
