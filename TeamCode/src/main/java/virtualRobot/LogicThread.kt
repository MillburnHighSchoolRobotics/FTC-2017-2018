package virtualRobot


import java.util.ArrayList
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

import virtualRobot.commands.Command
import virtualRobot.commands.SpawnNewThread
import virtualRobot.commands.Translate
import virtualRobot.utils.BetterLog

/**
 * Created by ethachu19 on 3/31/17
 *
 * LogicThread is the main working thread that runs procedurally
 * In realRun use runCommand to run commands
 * Do not use Command.changeRobotState
 *
 * HAHA TAKE THAT SUCKERS
 */
abstract class LogicThread : Thread() {
    protected var children: MutableList<Thread> = ArrayList() //contains a list of threads created under this logic Thread using spawn new thread
    protected var robot = Command.ROBOT
    private val monitorData = ConcurrentHashMap<String, Boolean>()
    private val interrupts = ConcurrentHashMap<Condition, LogicThread>()
    @Volatile protected var lastRunCommand: Command? = null
    private val monitorThreads = Collections.synchronizedList(ArrayList<MonitorThread>())
    @Volatile private var startTime: Long = 0
    @Volatile private var elapsedTime: Long = 0

    protected var isPaused = AtomicBoolean(false)
    protected var shouldStartISR = true

    //The thread to check all monitorThreads and put data in HashMap and check for interrupts
    private val interruptHandler = object : Thread() {

        var parent: LogicThread? = null

        fun setParent(logicThread: LogicThread): Thread {
            parent = logicThread
            return this
        }

        override fun run() {
            parent!!.startTime = System.currentTimeMillis()
            var isInterrupted = false
            while (!isInterrupted) {
                for (m in parent!!.monitorThreads) {
                    parent!!.monitorData.put(m.javaClass.getName(), !m.status || parent!!.monitorData[m.javaClass.getName()])
                }

                for ((key, value) in parent!!.interrupts) {
                    if (key.isConditionMet) {
                        parent!!.isPaused.set(true)
                        if (parent!!.lastRunCommand != null)
                            parent!!.lastRunCommand!!.stopCommand() //stops currently running command
                        robot.stopMotors()
                        try {
                            value.realRun()
                        } catch (e: InterruptedException) {
                            isInterrupted = true
                        }

                        value.killChildren()
                        parent!!.lastRunCommand!!.resetStopCommand()
                        parent!!.isPaused.set(false)
                    }
                }
                parent!!.elapsedTime = System.currentTimeMillis() - startTime
                isInterrupted = Thread.currentThread().isInterrupted || isInterrupted
                if (isInterrupted)
                    break

                try {
                    Thread.sleep(1)
                } catch (ex: InterruptedException) {
                    isInterrupted = true
                    break
                }

            }
        }
    }.setParent(this)

    override fun run() {
        isPaused.set(false)
        addPresets()
        if (shouldStartISR) {
            interruptHandler.start()
        }
        try {
            realRun()
        } catch (e: InterruptedException) {
            BetterLog.d("INTERRUPTS", this.javaClass.getName() + " was interrupted")
        } finally {
            killChildren()
            killMonitorThreads()
            if (shouldStartISR)
                interruptHandler.interrupt()
            isPaused.set(false)
        }
    }

    /**
     * Runs command with certain conditions to check type of command
     *
     * @param c Command to run
     * @return Whether command stopped by interrupt
     */
    @Synchronized
    @Throws(InterruptedException::class)
    protected fun runCommand(c: Command) {
        var isInterrupted = false
        var stopByIH = false
        lastRunCommand = c

        if (isPaused.get()) {
            if (waitForNotify())
                throw InterruptedException("waitForNotify was interrupted")
        }

        if (c is SpawnNewThread) { //Add all children thread to list to kill later
            children.addAll(c.threads)
        }

        try {
            isInterrupted = c.changeRobotState() //Actually run the command
        } catch (e: InterruptedException) {
            isInterrupted = true
        }

        if (isInterrupted)
            throw InterruptedException(c.javaClass.getName() + " was interrupted")

        if (isPaused.get()) {
            stopByIH = true
            if (c is Translate) {
                if (c.direction.code % 4 == 0) { // Went forward or backward
                    c.target = c.target - Math.abs(robot.lfMotor.position + robot.rfMotor.position + robot.lbMotor.position + robot.rbMotor.position) / 4
                } else if (c.direction.code % 2 == 0) { //Went left or right
                    c.target = c.target - Math.abs(robot.lfMotor.position + robot.lbMotor.position) / 2
                } else {
                    var count = 0
                    var avg = 0.0
                    for (i in 0..3) { //add distance based off only the motors that moved
                        if (c.multiplier[i] != 0) {
                            count++
                            when (i) {
                                0 -> avg += robot.lfMotor.position.toDouble()
                                1 -> avg += robot.rfMotor.position.toDouble()
                                2 -> avg += robot.lbMotor.position.toDouble()
                                3 -> avg += robot.rbMotor.position.toDouble()
                            }
                        }
                    }
                    avg = Math.abs(avg) / count //average the count
                    c.target = avg / Math.sqrt(2.0) //reduce it by multiplier
                }
            }
        }

        if (isPaused.get()) {
            if (waitForNotify())
                throw InterruptedException("waitForNotify was interrupted")
        }

        if (stopByIH)
        //Resuming after stopped by Interrupt Handler, call command again and resume Translate where it left off
            try {
                if (c is Translate) {
                    isInterrupted = c.changeRobotState()
                } else {
                    isInterrupted = c.changeRobotState()
                }
            } catch (e: InterruptedException) {
                isInterrupted = true
            }

        if (isInterrupted)
            throw InterruptedException(c.javaClass.getName() + " was interrupted")
    }

    /**
     * Gets the data related to the monitorThread given
     *
     * @param obj Class to look for
     * @return Data
     */
    protected fun getMonitorData(obj: Class<out MonitorThread>): Boolean {
        val temp = monitorData[obj.name]
        monitorData.put(obj.name, false)
        return temp
    }

    private fun waitForNotify(): Boolean {
        synchronized(this) {
            while (isPaused.get()) {
                try {
                    //                    this.wait();
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    return true
                }

            }
        }
        return false
    }

    /**
     * Removes all monitorThreads of class type object
     *
     * @param object class to remove
     */
    @Synchronized
    fun removeMonitor(`object`: Class<out MonitorThread>) {
        val remove = ArrayList<MonitorThread>()
        for (mt in monitorThreads) {
            if (`object`.isInstance(mt))
                remove.add(mt)
        }
        monitorData.remove(`object`.name)
        for (mt in remove) {
            mt.interrupt()
            monitorThreads.remove(mt)
        }
    }

    /**
     * Determines whether this thread or ISR is alive
     *
     * @return isAlive
     */
    fun allIsAlive(): Boolean {
        return this.isAlive || interruptHandler.isAlive
    }

    /**
     * Method with real commands and running commands
     */
    @Throws(InterruptedException::class)
    protected abstract fun realRun()

    /**
     * Used to delegate monitorThreads and attach interrupts
     */
    protected open fun addPresets() {}

    /**
     * Attaches intterupt to the ISR with a certain procedure
     *
     * @param condition
     * @param action
     */
    fun attachInterrupt(condition: Condition, action: LogicThread) {
        interrupts.put(condition, action)
    }

    /**
     * Kills all children spawned by SpawnNewThread
     */
    @Synchronized
    fun killChildren() {
        for (x in children)
        //Kill any threads made using spawn new thread
            if (x.isAlive)
                x.interrupt()
    }

    /**
     * Kills all monitorThreads associated with this LogicThread
     */
    @Synchronized private fun killMonitorThreads() {
        for (x in monitorThreads)
            if (x.isAlive)
                x.interrupt()
    }

    /**
     * Binds monitorThread to LogicThread so that it can reference it in code
     *
     * @param monitorThread monitorThread to be bound to LogicThread
     * @param runAsThread Whether or not monitorThread should be start as new Thread
     */
    @JvmOverloads
    fun delegateMonitor(monitorThread: MonitorThread, runAsThread: Boolean = false) {
        monitorThreads.add(monitorThread)
        monitorData.put(monitorThread.javaClass.getName(), false)
        if (runAsThread) {
            monitorThread.setThread(true)
            monitorThread.start()
        }
    }
}
/**
 * Binds monitorThread to LogicThread so that it can reference it in code without starting it as a Thread
 *
 * @param monitorThread monitorThread to be bound to LogicThread
 */
