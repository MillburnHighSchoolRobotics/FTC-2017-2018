package virtualRobot.logicThreads.testing;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;

/**
 * Created by david on 9/29/17.
 */

public class JoystickTelemetryLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        JoystickController controller1 = getRobot().getJoystickController1();
        while (true) {
            controller1.logicalRefresh();
            getRobot().addToTelemetry("X: ", controller1.getValue(JoystickController.Companion.getX_1()));
            getRobot().addToTelemetry("Y: ", controller1.getValue(JoystickController.Companion.getY_1()));
            getRobot().addToTelemetry("THETA: ", controller1.getValue(JoystickController.Companion.getTHETA_1()));
            double thetaDeg = Math.toDegrees(controller1.getValue(JoystickController.Companion.getTHETA_1()));
            getRobot().addToTelemetry("THETADEG: ", thetaDeg);
            if (thetaDeg < 0) thetaDeg += 360;
            getRobot().addToTelemetry("ADJTHETADEG: ", thetaDeg);
            getRobot().addToTelemetry("R: ", controller1.getValue(JoystickController.Companion.getR_1()));
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
