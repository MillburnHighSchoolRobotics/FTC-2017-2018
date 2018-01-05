package virtualRobot.logicThreads

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark

import java.util.concurrent.atomic.AtomicBoolean

import virtualRobot.LogicThread
import virtualRobot.commands.Translate

/**
 * Created by Ethan Mak on 9/10/2017.
 */

abstract class AutonomousLogicThread : LogicThread() {
    var currentVuMark = RelicRecoveryVuMark.UNKNOWN
    var redIsLeft = AtomicBoolean(false)


    @Throws(InterruptedException::class)
    protected fun depositGlyph() {
        runCommand(Translate(100.0, Translate.Direction.FORWARD, 0.0))
        //TODO: RUN BOX TO DEPOSIT GLYPH
        runCommand(Translate(100.0, Translate.Direction.BACKWARD, 0.0))
    }
}
