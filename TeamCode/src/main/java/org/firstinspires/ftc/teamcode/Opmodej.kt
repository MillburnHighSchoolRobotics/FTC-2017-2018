package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

/**
 * Created by ethan on 9/25/17.
 */
@TeleOp(name = "Concept: navX Collisionds Detection", group = "Concept")
class Opmodej : OpMode() {

    internal var vex = hardwareMap.get("servov") as Servo
    override fun init() {

    }

    override fun loop() {
        vex.position = 0.4
    }
}
