package virtualRobot.logicThreads.TestingAutonomouses;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.SallyJoeBot;
import virtualRobot.LogicThread;
import virtualRobot.commands.Command;
import virtualRobot.commands.CompensateColor;
import virtualRobot.commands.MoveMotorPID;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;
import virtualRobot.commands.WallTrace;

/**
 * Created by Yanjun on 11/28/2015.
 * used for tuning PID
 */
public class PIDTester extends LogicThread {
    public static boolean forward = true;
    @Override
    public void realRun() {
       // Translate.setGlobalMaxPower(1.0);
        //runCommand(new Translate(5000, Translate.Direction.FORWARD, 0));
        /*runCommand(new Pause(2000));
        runCommand(new Translate(3000, Translate.Direction.FORWARD, 0));
        runCommand(new Pause(2000));
        runCommand(new Translate(500, Translate.Direction.FORWARD, 0));*/

        //runCommand(new Translate(3000, Translate.Direction.RIGHT, 0));
        //runCommand(new Pause(3000));
        //runCommand(new Translate(7000, Translate.Direction.FORWARD, 0));
        //runCommand(new Translate(7000, Translate.Direction.FORWARD, 0));
        //runCommand(new Pause(3000));
        //runCommand(new Translate(3000, Translate.Direction.BACKWARD, 0));
        //runCommand(new Pause(3000));
        //runCommand(new Translate(5000, forward ? Translate.Direction.FORWARD : Translate.Direction.BACKWARD, 0));
//        runCommand(new Pause(3000));
//        runCommand(new Translate(7000, Translate.Direction.LEFT, 0));
//        runCommand(new Rotate(90));

        //HIGH: .008125; LOW: .007
        //LOW: .003; HIGH: .0035
       // Translate c = new Translate(.00325,5000,-1,new AtomicBoolean(), Translate.Direction.FORWARD);
       // runCommand(c);
        //Command.ROBOT.addToProgress("Translate KP:" + c.translateController.getKp());
//        runCommand(new Rotate(90, 1));

        //runCommand(new MoveMotorPID(50,robot.getFlywheel(),robot.getFlywheelEncoder()));
//        Rotate.setDefaultMode(Rotate.RunMode.WITH_ENCODER);
//        runCommand(new Rotate(55,.5,1000));
//        Rotate.setDefaultMode(Rotate.RunMode.WALL_ALIGN);
//        runCommand(new Pause(1000));
//        runCommand(new Rotate(0,0.5));
//        runCommand(new Pause(1000));
//        runCommand(new Rotate(0,0.5));

//        runCommand(new Command() {
//
//            @Override
//            public boolean changeRobotState() throws InterruptedException {
//                Log.d("PIDOUTPUTTICKS","" + robot.getLFMotor().getMotorType());
//                return false;
//            }
//        });
       // runCommand(new WallTrace(WallTrace.Direction.FORWARD));

        runCommand(new WallTrace(WallTrace.Direction.FORWARD,13,0.2,0.04,0.015));
//       runCommand(new CompensateColor());
//        runCommand(new Rotate(0.037,90,40000,new AtomicBoolean(false)));
    }
}
