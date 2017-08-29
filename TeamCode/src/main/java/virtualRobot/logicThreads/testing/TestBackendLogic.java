package virtualRobot.logicThreads.testing;

import virtualRobot.LogicThread;
import virtualRobot.commands.Command;
import virtualRobot.commands.MoveMotor;

/**
 * Created by Ethan Mak on 8/28/2017.
 */

public class TestBackendLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException{
        runCommand(new MoveMotor(robot.getLFMotor(),1,5000));
    }
}
