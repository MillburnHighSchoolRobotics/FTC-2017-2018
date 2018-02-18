package virtualRobot.logicThreads.testing;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.LogicThread;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.RotateEncoder;
import virtualRobot.commands.Translate;

/**
 * Created by Yanjun on 11/28/2015.
 * used for tuning PID
 */
public class PIDTesterLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
//        runCommand(new Translate(1000, Translate.Direction.R,0,1));
//        runCommand(new Rotate(0.0122,90,40000, new AtomicBoolean(false)));
//        runCommand(new Rotate(90));
        runCommand(new RotateEncoder(90));
        runCommand(new Pause(2000));
        runCommand(new RotateEncoder(0));
        runCommand(new Pause(2000));
        runCommand(new RotateEncoder(45));
    }
}
