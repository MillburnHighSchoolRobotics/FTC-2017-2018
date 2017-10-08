package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import virtualRobot.utils.MathUtils;

/**
 * Created by ethan on 10/5/17.
 */

@TeleOp(name="MainTeleop", group="LALALA")
public class TestLiftOpMode extends OpMode {
    DcMotor motor1;
    DcMotor motor2;
    DcMotor leftFront,rightFront,leftBack,rightBack;

    Servo servo1;
    Servo servo2;
    float threshold = 0.1f ;

    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("glyphLiftLeft");
        motor2 = hardwareMap.dcMotor.get("glyphLiftRight");
        servo1 = hardwareMap.servo.get("clawLeft");
        servo2 = hardwareMap.servo.get("clawRight");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        if (gamepad2.a)
            motor1.setPower(1);
        else if (gamepad2.b)
            motor1.setPower(-1);
        else
            motor1.setPower(0);

        if (gamepad2.dpad_up)
            motor2.setPower(1);
        else if (gamepad2.dpad_down)
            motor2.setPower(-1);
        else
            motor2.setPower(0);

        if (gamepad2.x) {
            servo1.setPosition(0);
            servo2.setPosition(1);
        }

        if (gamepad2.y) {
            servo1.setPosition(0.3);
            servo2.setPosition(0.7);
        }
        if(Math.abs(gamepad1.left_stick_y) > threshold || Math.abs(gamepad1.left_stick_x) > threshold)
        {
            float jx = gamepad1.left_stick_x;
            float jy = gamepad1.left_stick_y;
            rightFront.setPower(jx);
            rightBack.setPower(jx);

            leftBack.setPower(jx);
            leftFront.setPower(jx);
            if(jx<0.05){
                rightFront.setPower(jy);

                rightBack.setPower(jy/1.5);

                leftBack.setPower(-jy);
                leftFront.setPower(-jy);
            }


        }else{
          rightFront.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            leftBack.setPower(0);

        }
        if(Math.abs(gamepad1.right_stick_x) > threshold)
        {
            //rotate
            float jx = gamepad1.right_stick_x;
            float jy = gamepad1.right_stick_y;
            rightFront.setPower(-jx);
            leftFront.setPower(-jx);
            rightBack.setPower(jx);
            leftBack.setPower(jx);



        }else{
            rightFront.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            leftBack.setPower(0);

        }

    }
}
