package virtualRobot.commands;

import virtualRobot.LogicThread;

/**
 * Created by 17osullivand on 1/31/17.
 * KIlls all the babies of the thread (aka threads spanwed with spawn new thread)
 */

public class killChildren extends Command {
    private LogicThread logicThread;
    public killChildren(LogicThread logicThread) {
        this.logicThread = logicThread;
    }

    @Override
    protected int activate(String s) {
        return NO_CHANGE;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        logicThread.killChildren();
        return false;
    }
}
