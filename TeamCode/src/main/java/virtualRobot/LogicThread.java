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
    protected List<Thread> children = new ArrayList<>(); //contains a list of threads created under this logic Thread using spawn new thread
    protected SallyJoeBot robot = Command.ROBOT;
    protected ConcurrentHashMap<String, Boolean> monitorData = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Condition, LogicThread> interrupts = new ConcurrentHashMap<>();
    protected volatile Command lastRunCommand = null;
    private List<MonitorThread> monitorThreads = Collections.synchronizedList(new ArrayList<MonitorThread>());
    private volatile long startTime, elapsedTime = 0;

    private enum ThreadState{
        NOT_RUNNING,
        RUNNING,
        PAUSE
    };
    private volatile ThreadState state;

    //The thread to check all monitorThreads and put data in HashMap
    private Thread interruptHandler = new Thread() {

        public LogicThread parent = null;

        public Thread setParent(LogicThread logicThread) {
            parent = logicThread;
            return this;
        }

        public void run() {
//            parent.startTime = System.currentTimeMillis();
            boolean isInterrupted = false;
            while (!isInterrupted) {
                for (MonitorThread m : parent.monitorThreads) {
                    parent.monitorData.put(m.getClass().getName(), !m.getStatus());
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
                            parent.lastRunCommand.stopCommand(); //stops currently running command
                        entry.getValue().state = ThreadState.RUNNING;
                        try {
                            entry.getValue().realRun();
                        } catch (InterruptedException e) {
                            isInterrupted = true;
                        }
                        entry.getValue().killChildren();
                        entry.getValue().state = ThreadState.NOT_RUNNING;
                        parent.resumeParent();
                        parent.notify();
                    }
                }
//                parent.elapsedTime = System.currentTimeMillis() - startTime;
                isInterrupted = Thread.currentThread().isInterrupted();
                if (isInterrupted)
                    break;

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    isInterrupted = true;
                    break;
                }
            }
        }
    }.setParent(this);

    @Override
    public void run(){
        state = ThreadState.RUNNING;
        interruptHandler.start();
        try {
            realRun();
        } catch (InterruptedException e) {
        }
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
    protected synchronized boolean runCommand(Command c) throws InterruptedException{
        boolean isInterrupted = false;
        boolean stopByIH = false;
        lastRunCommand = c;

        if (state == ThreadState.PAUSE){
            if (waitForNotify())
                return true;
        }

        if (c instanceof SpawnNewThread) { //Add all children thread to list to kill later
            children.addAll(((SpawnNewThread)c).getThreads());
        }

        try {
            isInterrupted  = c.changeRobotState(); //Actually run the command
        } catch (InterruptedException e) {
            isInterrupted = true;
        }

        if (state == ThreadState.PAUSE && !isInterrupted) {
            stopByIH = true;
            if (c instanceof Translate) {
                Translate t = (Translate) c;
                if (t.getDirection().getCode() % 4 == 0) { // Went forward or backward
                    t.setTarget(t.getTarget() - Math.abs(robot.getLFEncoder().getValue() + robot.getRFEncoder().getValue() + robot.getLBEncoder().getValue() + robot.getRBEncoder().getValue()) / 4);
                } else if (t.getDirection().getCode() % 2 == 0) { //Went left or right
                    t.setTarget(t.getTarget() - Math.abs(robot.getLFEncoder().getValue() + robot.getLBEncoder().getValue()) / 2);
                } else {
                    int count = 0;
                    double avg = 0;
                    for (int i = 0; i < 4; i++) { //add distance based off only the motors that moved
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
                    avg = Math.abs(avg) / count; //average the count
                    t.setTarget(avg / Math.sqrt(2)); //reduce it by multiplier
                }
            }
        }

        if (state == ThreadState.PAUSE){
            if (waitForNotify())
                return true;
        }

        if (stopByIH) //Resuming after stopped by Interrupt Handler, call command again and resume Translate where it left off
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

    private boolean waitForNotify() {
        synchronized (this) {
            while (state == ThreadState.PAUSE) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized void removeMonitor(Class<?> object) {
        List<MonitorThread> remove = new ArrayList<>();
        for (MonitorThread mt : monitorThreads) {
            if (object.isInstance(mt))
                remove.add(mt);
        }
        for (MonitorThread mt : remove) {
            monitorThreads.remove(mt);
        }
    }

    /**
     * Method with real commands and running commands
     */
    protected abstract void realRun() throws InterruptedException;


    /**
     *
     * @param condition
     * @param action
     */
    public void attachInterrupt(Condition condition, LogicThread action) {
        interrupts.put(condition, action);
    }

    /**
     * Kills all children spawned by SpawnNewThread
     */
    public synchronized void killChildren() {
        for (Thread x : children) //Kill any threads made using spawn new thread
            if (x.isAlive())
                x.interrupt();
    }

    /**
     * Binds monitorThread to LogicThread so that it can reference it in code
     *
     * @param LogicThread The logic thread to
     * @param monitorThread monitorThread to be bound to LogicThread
     */
    public static void delegateMonitor(LogicThread LogicThread, MonitorThread monitorThread) {
        LogicThread.monitorThreads.add(monitorThread);
        LogicThread.monitorData.put(monitorThread.getClass().getName(), false);
    }

    /**
     * Put this logic thread into a pause state and notify other threads
     */
    protected synchronized void pauseParent() {
        synchronized (this) {
            state = ThreadState.PAUSE;
            notifyAll();
        }
    }

    /**
     * Resumes this logic thread and notifies other threads
     */
    protected synchronized void resumeParent() {
        synchronized (this) {
            state = ThreadState.RUNNING;
            notifyAll();
        }
    }
}
