package virtualRobot

import virtualRobot.utils.BetterLog

import virtualRobot.commands.Command

/**
 * Created by shant on 1/10/2016.
 * Monitor threads are used by delegateMonitor in LogicThread.
 * They have the abstract method setStatus, where a particular monitor thread is told how to determine whether
 * the monitor thread should be triggered or not.
 */
abstract class MonitorThread : Thread() {
    @Volatile private var status: Boolean = false //usually should be TRUE, if stuff goes wrong, make it FALSE
    var isThread = false
    protected var robot: SallyJoeBot

    init {
        robot = Command.ROBOT
        NORMAL = true
        status = true
    }

    /**
     * Sets isThread
     *
     * @param isThread
     */
    fun setThread(isThread: Boolean) {
        this.isThread = isThread
    }

    override fun run() { //Constantly Runs as long as the status is normal. As soon as it isn't, it stops running. The god Thread in the meantime will have detected that the status is not normal.
        while (!Thread.currentThread().isInterrupted) {
            if (setStatus() != NORMAL) {
                status = !NORMAL
            }
            //BetterLog.d("Monitor", Boolean.toString(status));
        }

    }

    /**
     * Gets the status bast
     *
     * @return Current Status
     */
    @Synchronized
    fun getStatus(): Boolean {
        if (!isThread)
            return setStatus()
        val temp = status
        resetStatus()
        return temp
    }

    /**
     * Puts status back to normal
     */
    private fun resetStatus() {
        status = NORMAL
    }

    abstract fun setStatus(): Boolean

    companion object {
        var NORMAL: Boolean = false
    }
}
