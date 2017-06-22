package virtualRobot;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import virtualRobot.commands.Command;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.SpawnNewThread;
import virtualRobot.commands.Translate;

/**
 * Created by ethachu19 on 3/31/17
 *
 * LogicThread is the main working thread that runs procedurally
 * In realRun use runCommand to run commands
 * Do not use Command.changeRobotState
 *
 * HAHA TAKE THAT SUCKERS
 */
public abstract class LogicThread implements Runnable {
    protected List<Thread> children; //contains a list of threads created under this logic Thread using spawn new thread
    protected SallyJoeBot robot;
    protected ConcurrentHashMap<String, Boolean> monitorData = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Condition, LogicThread> interrupts = new ConcurrentHashMap<>();
    protected Command lastRunCommand = null;
    private List<MonitorThread> monitorThreads = Collections.synchronizedList(new ArrayList<MonitorThread>());
    private long startTime, elapsedTime;

    private enum ThreadState{
        NOT_RUNNING,
        RUNNING,
        PAUSE
    };
    private volatile ThreadState state;

    //The thread to check all monitorThreads and put data in HashMap
    private Thread interruptHandler = new Thread(this) {

        public LogicThread parent;

        public void Thread(LogicThread thread) {
            parent = thread;
        }

        public void run() {
            startTime = System.currentTimeMillis();
            boolean isInterrupted = false;
            while (!isInterrupted) {
                for (MonitorThread m : parent.monitorThreads) {
                    if (!m.getStatus()) {
                        parent.monitorData.put(m.getClass().getName(), true);
                    }
                }

                for (Map.Entry<Condition, LogicThread> entry : parent.interrupts.entrySet()) {
                    if (entry.getKey().isConditionMet()) {
                        parent.pauseParent();
                        try {
                            parent.wait();
                        } catch (InterruptedException e) {
                            isInterrupted = true;
                            return;
                        }
                        if (parent.lastRunCommand != null)
                            parent.lastRunCommand.stopCommand();
                        entry.getValue().state = ThreadState.RUNNING;
                        entry.getValue().realRun();
                        entry.getValue().killChildren();
                        entry.getValue().state = ThreadState.NOT_RUNNING;
                        parent.resumeParent();
                    }
                }
                parent.elapsedTime = System.currentTimeMillis() - startTime;
                isInterrupted = Thread.currentThread().isInterrupted();

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    isInterrupted = true;
                }
            }
        }
    };

    @Override
    public void run(){
        state = ThreadState.RUNNING;
        elapsedTime = 0;
        interruptHandler.start();
        realRun();
        killChildren();
        interruptHandler.interrupt();
        state = ThreadState.NOT_RUNNING;
    }

    /**
     * Runs command with certain conditions to check type of command
     *
     * @param c Command to run
     * @return Whether command stopped by interrupt
     */
    protected boolean runCommand(Command c) {
        boolean isInterrupted = false;
        boolean stopByIH = false;
        lastRunCommand = c;
        while (state == ThreadState.PAUSE) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return true;
            }
        }
        if (c instanceof Rotate) {
            if (((Rotate)c).getName() != null) robot.addToProgress(((Rotate)c).getName());
        }
        if (c instanceof Translate) {
            if (((Translate)c).getName() != null) robot.addToProgress(((Translate)c).getName());
        }
        if (c instanceof SpawnNewThread) {
            children.addAll(((SpawnNewThread)c).getThreads());
        }
        try {
            isInterrupted  = c.changeRobotState();
        } catch (InterruptedException e) {
            isInterrupted = true;
        }
        if (state == ThreadState.PAUSE && !isInterrupted) {
            stopByIH = true;
            if (c instanceof Translate) {
                Translate t = (Translate) c;
                if (t.getDirection().getCode() % 4 == 0) {
                    t.setTarget(t.getTarget() - Math.abs(robot.getLFEncoder().getValue() + robot.getRFEncoder().getValue() + robot.getLBEncoder().getValue() + robot.getRBEncoder().getValue()) / 4);
                } else if (t.getDirection().getCode() % 2 == 0) {
                    t.setTarget(t.getTarget() - Math.abs(robot.getLFEncoder().getValue() + robot.getLBEncoder().getValue()) / 2);
                } else {
                    int count = 0;
                    double avg = 0;
                    for (int i = 0; i < 4; i++) {
                        if (t.getMultiplier()[i] != 0) {
                            count++;
                            switch (i) {
                                case 0:
                                    avg += robot.getLFEncoder().getValue();
                                    break;
                                case 1:
                                    avg += robot.getRFEncoder().getValue();
                                    break;
                                case 2:
                                    avg += robot.getLBEncoder().getValue();
                                    break;
                                case 3:
                                    avg += robot.getRBEncoder().getValue();
                                    break;
                            }
                        }
                    }
                    avg = Math.abs(avg) / count;
                    t.setTarget(avg / Math.sqrt(2));
                }
            }
            notify();
        }
        while (state == ThreadState.PAUSE && !isInterrupted) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return true;
            }
        }
        if (stopByIH)
            try {
                if (c instanceof Translate) {
                    Translate t = (Translate) c;
                    isInterrupted = t.changeRobotState();
                } else {
                    isInterrupted = c.changeRobotState();
                }
            } catch (InterruptedException e) {
                isInterrupted = true;
            }
        return isInterrupted;
    }

    protected void resetMonitor(Class<?> monitorClass) {
        monitorData.put(monitorClass.getName(),false);
    }

    /**
     * Method with real commands and running commands
     */
    protected abstract void realRun();

    protected LogicThread() {
        state = ThreadState.NOT_RUNNING;
        robot = Command.ROBOT;
        children = new ArrayList<Thread>();
    }

    public void attachInterrupt(Condition condition, LogicThread action) {
        interrupts.put(condition, action);
    }

    /**
     * Kills all children spawned by SpawnNewThread
     */
    public void killChildren() {
        for (Thread x : children) //Kill any threads made using spawn new thread
            if (x.isAlive())
                x.interrupt();
    }

    /**
     * Binds monitorThread to logicThread so that it can reference it in code
     *
     * @param logicThread The logic thread to
     * @param monitorThread monitorThread to be bound to LogicThread
     */
    public static void delegateMonitor(LogicThread logicThread, MonitorThread monitorThread) {
        logicThread.monitorThreads.add(monitorThread);
        logicThread.monitorData.put(monitorThread.getClass().getName(), false);
    }

    protected synchronized void pauseParent() {
        synchronized (this) {
            state = ThreadState.PAUSE;
            notify();
        }
    }

    protected synchronized void resumeParent() {
        synchronized (this) {
            state = ThreadState.RUNNING;
            notify();
        }
    }
}
