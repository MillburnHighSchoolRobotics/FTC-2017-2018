package virtualRobot.logicThreads.testing;

import virtualRobot.commands.EthanClass;
import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by david on 11/15/17.
 */

public class RedIsLeftLogic extends AutonomousLogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        while (true) {
            runCommand(new EthanClass());
            robot.addToTelemetry("Red is Left", redIsLeft.get());
        }
    }
}
