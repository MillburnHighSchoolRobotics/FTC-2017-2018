package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.kauailabs.navx.ftc.MPU9250;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import virtualRobot.SallyJoeBot;
import virtualRobot.commands.Command;
import virtualRobot.hardware.AxisSensor;
import virtualRobot.hardware.Sensor;
import virtualRobot.hardware.StateSensor;
//import virtualRobot.LogicThreads.deprecated.FPSLogicThread;
import virtualRobot.utils.Vector3f;

/**
 * Created by ethachu19 on 12/21/2016.
 */

public class FPSTest extends OpMode {
    MPU9250 imu;
    SallyJoeBot robot = Command.ROBOT;
    StateSensor vStateSensor;
    Sensor vHeadingSensor, vPitchSensor, vRollSensor;
    AxisSensor vRawAccel;
    Thread LogicThread;
    @Override
    public void init() {
        imu = MPU9250.getInstance(hardwareMap.deviceInterfaceModule.get("dim"), 1);
        vStateSensor = robot.getStateSensor();
        vHeadingSensor = robot.getHeadingSensor();
        vPitchSensor = robot.getPitchSensor();
        vRollSensor = robot.getRollSensor();
        vRawAccel = robot.getRawAccel();
//        try {
//            LogicThread = new Thread(FPSLogicThread.class.newInstance());
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void loop() {
        vPitchSensor.setRawValue(imu.getIntegratedPitch());
        vHeadingSensor.setRawValue(imu.getIntegratedYaw());
        vRollSensor.setRawValue(imu.getIntegratedRoll());
        vRawAccel.setRawValue(new Vector3f(imu.getIntegratedAccelX(),imu.getIntegratedAccelY(),imu.getIntegratedAccelZ()));
        vStateSensor.update();
    }

    public void stop() {
        imu.close();
        LogicThread.interrupt();
    }
}
