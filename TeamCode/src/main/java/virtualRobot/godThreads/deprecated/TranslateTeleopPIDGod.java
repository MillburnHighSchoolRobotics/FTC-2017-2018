package virtualRobot.godThreads.deprecated;

import android.util.Log;

import virtualRobot.GodThread;
import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.MonitorThread;
import virtualRobot.SallyJoeBot;
import virtualRobot.commands.Command;
import virtualRobot.logicThreads.TestingAutonomouses.KPModifier;
import virtualRobot.logicThreads.TestingAutonomouses.TranslatePIDTest;

/**
 * Created by ethachu19 on 1/15/2017.
 */

public class TranslateTeleopPIDGod extends GodThread {
    SallyJoeBot robot;
    JoystickController controller;
    long iter = 1;
    Thread pid;
    LogicThread PIDTest;

    public TranslateTeleopPIDGod() {
        controller = robot.getJoystickController1();
    }

    @Override
    public void realRun() throws InterruptedException {
        robot = Command.ROBOT;
        while (!Thread.currentThread().isInterrupted()) {
            PIDTest = new KPModifier();
            pid = new Thread(PIDTest);
            pid.start();
            children.add(pid);
            delegateMonitor(pid, new MonitorThread[]{});
            Log.d("PIDOUTTRANSLATE","\n-----------------------------------------------------------------\n");
            PIDTest = new TranslatePIDTest(iter);
            pid = new Thread(PIDTest);
            pid.start();
            children.add(pid);
            delegateMonitor(pid, new MonitorThread[]{new MonitorThread(){
                JoystickController controller;
                @Override
                public boolean setStatus() {
                    controller = Command.ROBOT.getJoystickController1();
                    controller.logicalRefresh();
                    if (controller.isPressed(JoystickController.BUTTON_B)) {
                        pid.interrupt();
                    }
                    return true;
                }
            }});
            iter++;
        }
    }
}
