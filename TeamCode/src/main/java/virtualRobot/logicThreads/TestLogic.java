package virtualRobot.logicThreads;

import virtualRobot.LogicThread;
import virtualRobot.commands.Rotate;
import virtualRobot.monitorThreads.TimeMonitor;

/**
 * Created by ethachu19 on 3/31/2017.
 */

public class TestLogic extends LogicThread {
    @Override
    protected void realRun() {
        if (runCommand(new Rotate(90)))
            return;
    }
}
