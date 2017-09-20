package virtualRobot.monitorThreads;

import virtualRobot.utils.BetterLog;

import virtualRobot.SallyJoeBot;
import virtualRobot.MonitorThread;

/**
 * Created by shant on 1/10/2016.
 * Automatically stops robot if it's tilted at an angle (it's caught on debris)
 */
public class DebrisMonitor extends MonitorThread {


    @Override
    public boolean setStatus() {
        double totalAngle = Math.sqrt(Math.pow(robot.getRollSensor().getValue(), 2) + Math.pow(robot.getPitchSensor().getValue(), 2));
        if (totalAngle > 2.5) {
            BetterLog.d("RoboAngle", robot.getRollSensor().getValue() + " " + robot.getPitchSensor().getValue() + " " + totalAngle);
            BetterLog.d("RoboAngle", "Robot died in debris thread");
            return false;
        }
        BetterLog.d("RoboAngle", "we still in here " + robot.getRollSensor().getValue() + " " + robot.getPitchSensor().getValue());
        return true;
    }

}
