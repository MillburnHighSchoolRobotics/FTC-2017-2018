package virtualRobot.commands;

import virtualRobot.commands.Command;

/**
 * Created by Yanjun on 11/12/2015.
 * Pauses the Robot
 */
public class Pause extends Command {

    private int nMillis;

    public Pause(int time) {
        nMillis = time;
    }

    @Override
    public int activate(String s) {
        return 0;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        boolean isInterrupted = false;

        try {
            Thread.currentThread().sleep(nMillis);
        } catch (InterruptedException e) {
            isInterrupted = true;
        }

        return isInterrupted;
    }
}
