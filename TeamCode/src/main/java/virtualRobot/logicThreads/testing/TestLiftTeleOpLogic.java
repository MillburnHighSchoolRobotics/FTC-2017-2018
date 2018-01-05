package virtualRobot.logicThreads.testing;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;

/**
 * Created by ethan on 10/5/17.
 */

public class TestLiftTeleOpLogic extends LogicThread {
    @Override
    protected void addPresets() {
        setShouldStartISR(false);
    }

    @Override
    protected void realRun() throws InterruptedException {
        boolean isInterrupted = false;
        JoystickController controller1 = getRobot().getJoystickController1();
        JoystickController controller2 = getRobot().getJoystickController2();

        while (!isInterrupted) {
            controller1.logicalRefresh();
            controller2.logicalRefresh();

            if (controller1.isDown(JoystickController.Companion.getBUTTON_A())) {

            }

            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
