package virtualRobot.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.SallyJoeBot;
import virtualRobot.Condition;

/**
 * Created by ethachu19 on 3/31/17
 *
 * Command is the part that accesses the robot itself
 */
public abstract class Command  {

    final int BREAK = 2;
    final int END = 1;
    final int NO_CHANGE = 0;
    private HashMap<Condition, String> conditionals = new HashMap<>();
    private AtomicBoolean stopCommand = new AtomicBoolean(false);

    /**
     * To evaluate the action of the string and act accordingly
     *
     * @param s Action string
     * @return An int associated with the action needed in the method changeRobotState
     */
    protected abstract int activate(String s);

    /**
     * Adds a condition to HashMap of condiiton and associates it with an action
     *
     * @param condition
     * @param action
     */
    public void addCondition(Condition condition, String action) {
        conditionals.put(condition, action);
    }

    /**
     * Changes the SallyJoeBot itself
     * Calls to checkCondition will check all conditionals and return based on predfined action
     *
     * @return If it ended due to interrupt
     * @throws InterruptedException
     */
    public abstract boolean changeRobotState() throws InterruptedException;

    /**
     * Checks each conditional in HashMap and activates corresponding action
     *
     * @return Action to do in changeRobotState
     */
    protected final int checkConditionals() {
        for (Map.Entry<Condition, String> entry : conditionals.entrySet()) {
            if (entry.getKey().isConditionMet())
                return activate(entry.getValue());
        }
        return NO_CHANGE;
    }

    public synchronized void stopCommand() {
        stopCommand.set(true);
    }

    private synchronized boolean ifStopCommand() {
        return stopCommand.get();
    }

    public final static SallyJoeBot ROBOT = new SallyJoeBot();
}
