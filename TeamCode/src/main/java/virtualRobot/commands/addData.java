package virtualRobot.commands;

import virtualRobot.LogicThread;

/**
 * Created by 17osullivand on 11/3/16.
 */

public class addData extends Command {
    private LogicThread logicThread;
   private Object[] myData;
    public addData(LogicThread logicThread, Object... data) {
        this.logicThread = logicThread;
        this.myData = data;
    }

    @Override
    protected int activate(String s) {
        return 0;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        return false;
    }
    public LogicThread getLogicThread() {
        return logicThread;
    }
    public Object[] getMyData() {
        return myData;
    }
}
