package virtualRobot.logicThreads.competition;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.commands.Command;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

public class TeleOpLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        runCommand(new Command() {
            JoystickController controller1;
            JoystickController controller2;
            @Override
            public boolean changeRobotState() throws InterruptedException {
                controller1 = robot.getJoystickController1();
                controller2 = robot.getJoystickController2();
                boolean isInterrupted = false;
                while (!isInterrupted) {
                    controller1.logicalRefresh();
                    controller2.logicalRefresh();
                }
                return false;
            }
        });
    }
}
