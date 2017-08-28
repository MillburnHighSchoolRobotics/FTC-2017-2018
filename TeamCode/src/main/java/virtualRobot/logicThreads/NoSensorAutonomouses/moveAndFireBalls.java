package virtualRobot.logicThreads.NoSensorAutonomouses;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import virtualRobot.SallyJoeBot;
import virtualRobot.LogicThread;
import virtualRobot.commands.Command;
import virtualRobot.commands.MoveMotor;
import virtualRobot.commands.MoveMotorPID;
import virtualRobot.commands.MoveServo;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.SpawnNewThread;
import virtualRobot.commands.Translate;
import virtualRobot.commands.killChildren;
import virtualRobot.components.Motor;
import virtualRobot.components.Servo;

/**
 * Created by 17osullivand on 11/27/16.
 */

public class moveAndFireBalls extends LogicThread {
    private AtomicBoolean reapStart;
    private AtomicBoolean onlyFireBalls;


    LogicThread forward = new LogicThread() {
        @Override
        public void realRun() {
            runCommand(new MoveServo(new Servo[]{robot.getFlywheelStopper()}, new double[]{0})); //move button pusher

            runCommand(new Translate(1600, Translate.Direction.LEFT, 0));
            runCommand(new Pause(500));
            runCommand(new Rotate(0, .5, 500));
        }
    };
    LogicThread spinFlywheel = new LogicThread() {
        @Override
        public void realRun() {
            runCommand(new MoveMotorPID(92,robot.getFlywheel(),robot.getFlywheelEncoder()));
            runCommand(new Pause(1000));

        }
    };
    LogicThread moveReaper = new LogicThread() {
        @Override
        public void realRun() {
            runCommand(new Pause(2000));
            runCommand(new MoveMotor(robot.getReaperMotor(), .21));

        }
    };

    public moveAndFireBalls() {
        this.onlyFireBalls = new AtomicBoolean(false);
    }

    public moveAndFireBalls(AtomicBoolean onlyFireBalls) {
        this.onlyFireBalls = onlyFireBalls;
    }



    @Override
    public void realRun (){

        List<LogicThread> threads = new ArrayList<LogicThread>();
        threads.add(forward);
        threads.add(spinFlywheel);
        threads.add(moveReaper);

        SpawnNewThread fly = new SpawnNewThread((threads));

        runCommand(fly);
        runCommand(new Pause(5000));
        runCommand(new MoveServo(new Servo[]{robot.getFlywheelStopper()}, new double[]{.6})); //move button pusher

        runCommand(new killChildren(this));

        if (onlyFireBalls.get()) {
            runCommand(new Translate(4000, Translate.Direction.LEFT, 0));
            runCommand(new Rotate(-90));
        }
    }
}
