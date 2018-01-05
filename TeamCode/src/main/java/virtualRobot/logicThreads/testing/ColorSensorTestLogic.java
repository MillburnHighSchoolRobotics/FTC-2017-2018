package virtualRobot.logicThreads.testing;

import virtualRobot.LogicThread;

/**
 * Created by david on 11/3/17.
 */

public class ColorSensorTestLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        while (true) {
            getRobot().addToTelemetry("RGB: ", "(" + getRobot().getColorSensor().getRed() + ", " + getRobot().getColorSensor().getGreen() + ", " + getRobot().getColorSensor().getBlue() + ")");
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
