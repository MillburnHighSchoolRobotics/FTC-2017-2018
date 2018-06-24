package virtualRobot.logicThreads.competition;

import virtualRobot.LogicThread;
import virtualRobot.commands.RotateEncoder;
import virtualRobot.commands.Translate;

public class MartianAutoLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        runCommand(new Translate(1000, Translate.Direction.BACKWARD, 0, 0.8));
        runCommand(new RotateEncoder(90, 0.8));
        Thread.sleep(1000);
        runCommand(new Translate(2000, Translate.Direction.BACKWARD, 0, 0.8));
        robot.moveFlipper(true);
        Thread.sleep(3000);
        runCommand(new Translate(600, Translate.Direction.FORWARD, 0, 0.8));
    }
}
