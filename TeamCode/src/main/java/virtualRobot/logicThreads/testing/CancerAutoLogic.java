package virtualRobot.logicThreads.testing;

import virtualRobot.Condition;
import virtualRobot.LogicThread;
import virtualRobot.commands.MoveMotor;
import virtualRobot.utils.MathUtils;

/**
 * Created by david on 10/6/17.
 */

public class CancerAutoLogic extends LogicThread {
    @Override
    protected void addPresets() {
        shouldStartISR = false;
    }

    @Override
    protected void realRun() throws InterruptedException {
        double pow = 1;
        int n = 0;
        while (!Thread.currentThread().isInterrupted()) {
            runCommand(new MoveMotor(robot.getLBMotor(), pow, 1000));
            robot.addToTelemetry("Cancer Level: ", n++);
            if (MathUtils.equals(pow % 1, 0)) {
                pow *= 0.5;
            } else {
                pow *= -2;
            }
        }
    }
}
