package virtualRobot.logicThreads;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import virtualRobot.LogicThread;
import virtualRobot.commands.DetermineColumn;
import virtualRobot.commands.Rotate;
import virtualRobot.monitorThreads.TimeMonitor;

/**
 * Created by ethachu19 on 3/31/2017.
 *
 *
 */

public class ExampleLogic extends AutonomousLogicThread {
    @Override
    protected void addPresets() {

    }

    @Override
    protected void realRun() throws InterruptedException {
//        runCommand(new Rotate(90))
        while (true) runCommand(new DetermineColumn());
    }
}
