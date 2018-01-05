package virtualRobot.logicThreads.competition

import android.util.Pair

import java.util.Arrays

import virtualRobot.commands.EthanClass
import virtualRobot.commands.GetVuMarkSide
import virtualRobot.commands.MoveMotor
import virtualRobot.commands.Rotate
import virtualRobot.commands.SpawnNewThread
import virtualRobot.commands.Translate
import virtualRobot.logicThreads.AutonomousLogicThread
import virtualRobot.logicThreads.competition.utilities.StackingLogic
import virtualRobot.monitorThreads.TimeMonitor

import virtualRobot.logicThreads.competition.utilities.StackingLogic.GlyphColor.BROWN
import virtualRobot.logicThreads.competition.utilities.StackingLogic.GlyphColor.GREY
import virtualRobot.logicThreads.competition.utilities.StackingLogic.GlyphColor.UNKNOWN

/**
 * Created by Ethan Mak on 8/29/2017.
 */
class RedNearAutoLogic : AutonomousLogicThread() {
    internal val snake = arrayOf(arrayOf(GREY, GREY, BROWN, BROWN), arrayOf(GREY, BROWN, BROWN, GREY), arrayOf(BROWN, BROWN, GREY, GREY))
    internal val frog = arrayOf(arrayOf(BROWN, GREY, GREY, BROWN), arrayOf(GREY, BROWN, BROWN, GREY), arrayOf(BROWN, GREY, GREY, BROWN))
    internal val bird = arrayOf(arrayOf(BROWN, GREY, BROWN, GREY), arrayOf(GREY, BROWN, GREY, BROWN), arrayOf(BROWN, GREY, BROWN, GREY))

    internal var isFlipped = false
    internal var usedVuMark = false

    private var pattern: Array<Array<StackingLogic.GlyphColor>>? = null


    override fun addPresets() {
        delegateMonitor(TimeMonitor(25000.0))
    }

    @Throws(InterruptedException::class)
    override fun realRun() {
        val cryptobox = Array<Array<StackingLogic.GlyphColor>>(3) { arrayOfNulls(4) }

        for (x in cryptobox) {
            Arrays.fill(x, StackingLogic.GlyphColor.UNKNOWN)
        }

        val stackingLogic = StackingLogic()
        runCommand(SpawnNewThread(stackingLogic))

        runCommand(Translate(50.0, Translate.Direction.FORWARD, 0.0))

        runCommand(EthanClass())

        runCommand(Rotate(70.0))

        runCommand(GetVuMarkSide())

        runCommand(Rotate(20.0))

        if (redIsLeft.get()) {
            robot.jewelServo.position = 1.0
            runCommand(Rotate(-90.0))
        } else {
            robot.jewelServo.position = 0.0
            //runCommand(new Translate(100, Translate.Direction.RIGHT, 0, .5));
            runCommand(Rotate(90.0))
        }

        robot.rollerLeft.power = 1.0
        robot.rollerRight.speed = 1.0

        runCommand(Translate(2000.0, Translate.Direction.BACKWARD, 0.0))

        runCommand(Rotate(30.0))

        runCommand(Translate(2000.0, Translate.Direction.FORWARD, 0.0))

        runCommand(Rotate(30.0))

        while (stackingLogic.topGlyph == StackingLogic.GlyphColor.STACKING) {
            Thread.sleep(1)
        }

        stackingLogic.pause()
        var index = 1
        when (currentVuMark) {
            RelicRecoveryVuMark.LEFT -> index = 0
            RelicRecoveryVuMark.CENTER -> index = 1
            RelicRecoveryVuMark.RIGHT -> index = 2
        }
        val vuMarkIndex = index
        if (!getPattern(stackingLogic.topGlyph, stackingLogic.getGlyph(0), index)) {
            pattern = snake
            index = 2
            isFlipped = stackingLogic.topGlyph == BROWN
            usedVuMark = false
        } else {
            usedVuMark = true
        }

        if (index != 1)
            runCommand(Translate(75.0, if (index == 2) Translate.Direction.RIGHT else Translate.Direction.LEFT, 0.0))

        depositGlyph()
        cryptobox[index][0] = stackingLogic.getGlyph(0)
        cryptobox[index][1] = stackingLogic.getGlyph(1)
        stackingLogic.resumeThread()

        var indexData: Pair<Int, Int>?
        var i = 0
        while (getMonitorData(TimeMonitor::class.java)) {
            runCommand(Rotate(-30.0))
            robot.rollerLeft.power = 1.0
            robot.rollerRight.speed = 1.0
            runCommand(Translate((2000 + i * 300).toDouble(), Translate.Direction.BACKWARD, 0.0))
            runCommand(Translate((2000 + i * 300).toDouble(), Translate.Direction.FORWARD, 0.0))

            runCommand(Rotate(30.0))

            while (stackingLogic.topGlyph == StackingLogic.GlyphColor.STACKING) {
                Thread.sleep(1)
            }

            stackingLogic.pause()

            if (usedVuMark) {
                indexData = getIndex(stackingLogic.topGlyph, stackingLogic.getGlyph(0))
            } else {
                if (stackingLogic.getGlyph(0) == pattern!![vuMarkIndex][0].getRealColor(isFlipped) && stackingLogic.getGlyph(1) == pattern!![vuMarkIndex][1].getRealColor(isFlipped)) {
                    indexData = Pair(vuMarkIndex, 0)
                } else {
                    indexData = getIndex(stackingLogic.topGlyph, stackingLogic.getGlyph(0))
                }
            }

            //            runCommand(new MoveMotor(robot.getGlyphLift(), 1, indexData.second * 500, Translate.RunMode.WITH_PID, true));

            if (indexData!!.first != 1)
                runCommand(Translate(75.0, if (indexData.first == 2) Translate.Direction.RIGHT else Translate.Direction.LEFT, 0.0))

            depositGlyph()

            //runCommand(new MoveMotor(robot.getGlyphLift(), 1, indexData.second * -500, Translate.RunMode.WITH_PID, true));

            if (indexData.first != 1)
                runCommand(Translate(75.0, if (indexData.first == 2) Translate.Direction.LEFT else Translate.Direction.RIGHT, 0.0))

            if (Thread.currentThread().isInterrupted)
                break
            i++
        }
    }

    private fun getPattern(top: StackingLogic.GlyphColor, bottom: StackingLogic.GlyphColor, index: Int): Boolean {
        if (top == snake[index][1] && bottom == snake[index][1]) {
            pattern = snake
            return true
        }
        if (top == bird[index][1] && bottom == bird[index][1]) {
            pattern = bird
            return true
        }
        if (top == frog[index][1] && bottom == frog[index][1]) {
            pattern = frog
            return true
        }

        if (top == snake[index][1].getRealColor(true) && bottom == snake[index][1].getRealColor(true)) {
            pattern = snake
            isFlipped = true
            return true
        }
        if (top == bird[index][1].getRealColor(true) && bottom == bird[index][1].getRealColor(true)) {
            pattern = bird
            isFlipped = true
            return true
        }
        if (top == frog[index][1].getRealColor(true) && bottom == frog[index][1].getRealColor(true)) {
            pattern = frog
            isFlipped = true
            return true
        }
        return false
    }

    private fun getIndex(top: StackingLogic.GlyphColor, bottom: StackingLogic.GlyphColor): Pair<Int, Int>? {
        for (i in 0..2) {
            for (j in 0..2) {
                if (pattern!![i][j] == UNKNOWN) {
                    return if (pattern!![i][j] == bottom && pattern!![i][j + 1] == top)
                        Pair(i, j)
                    else
                        break
                }
            }
        }
        return null
    }
}
