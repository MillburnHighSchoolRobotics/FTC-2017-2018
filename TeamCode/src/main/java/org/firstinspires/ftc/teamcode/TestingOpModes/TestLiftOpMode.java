package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ethan on 10/5/17.
 */

@TeleOp(name="TestFuck2", group="LALALA")
public class TestLiftOpMode extends OpMode {
    DcMotor motor1;
    DcMotor motor2;

    Servo servo1;
    Servo servo2;

    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("glyphLiftLeft");
        motor2 = hardwareMap.dcMotor.get("glyphLiftRight");
        servo1 = hardwareMap.servo.get("clawLeft");
        servo2 = hardwareMap.servo.get("clawRight");
    }

    @Override
    public void loop() {
        if (gamepad1.a)
            motor1.setPower(1);
        else if (gamepad1.b)
            motor1.setPower(-1);
        else
            motor1.setPower(0);

        if (gamepad1.dpad_up)
            motor2.setPower(1);
        else if (gamepad1.dpad_down)
            motor2.setPower(-1);
        else
            motor2.setPower(0);

        if (gamepad1.x) {
            servo1.setPosition(0);
            servo2.setPosition(1);
        }

        if (gamepad1.y) {
            servo1.setPosition(0.3);
            servo2.setPosition(0.7);
        }
    }
}
