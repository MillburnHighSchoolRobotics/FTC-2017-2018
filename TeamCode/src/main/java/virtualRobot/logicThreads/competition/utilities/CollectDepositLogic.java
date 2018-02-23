package virtualRobot.logicThreads.competition.utilities;

import virtualRobot.commands.DetermineColumn;
import virtualRobot.commands.RotateEncoder;
import virtualRobot.commands.Translate;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.logicThreads.competition.BlueNearBasicAutoLogic;
import virtualRobot.monitorThreads.TimeMonitor;

/**
 * Created by warre on 2/23/2018.
 */

public class CollectDepositLogic extends AutonomousLogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        Thread.sleep(2000);
        robot.setRollerPower(-1);
        runCommand(new Translate(1000, Translate.Direction.FORWARD,0,0.5f));
        Thread.sleep(3000);
        robot.setRollerPower(0);
        Thread.sleep(500);
        runCommand(new RotateEncoder(0,1));
        Thread.sleep(500);
        runCommand(new DetermineColumn());
    }


}
