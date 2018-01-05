package virtualRobot.logicThreads.competition

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark

import virtualRobot.commands.GetVuMarkSide
import virtualRobot.hardware.DumbColorSensor
import virtualRobot.hardware.Motor
import virtualRobot.hardware.Servo
import virtualRobot.logicThreads.AutonomousLogicThread

/**
 * Created by ethan on 9/22/17.
 */

class BlueNearBasicAutoLogic : AutonomousLogicThread() {
    private var leftFront: Motor? = null
    private var leftBack: Motor? = null
    private var rightFront: Motor? = null
    private var rightBack: Motor? = null
    private var jewelArm: Servo? = null
    private var colorSensor: DumbColorSensor? = null
    @Throws(InterruptedException::class)
    override fun realRun() {
        leftFront = robot.lfMotor
        leftBack = robot.lbMotor
        rightFront = robot.rfMotor
        rightBack = robot.rbMotor
        colorSensor = robot.colorSensor
        jewelArm = robot.jewelServo
        jewelArm!!.position = 0.65
        val dist = 0
        val travel = 200
        val power = 0.5
        var startPosition: Int
        Thread.sleep(1000)

        val er = GetVuMarkSide()
        runCommand(er)

        val red = colorSensor!!.red
        var blue = colorSensor!!.blue
        robot.addToTelemetry("Red ", red)
        robot.addToTelemetry("Blue ", blue)
        //        blue = Math.max(1, blue);
        if (red != 0 || blue != 0) {
            blue = Math.max(1, blue)
            val rat = red / blue.toDouble()
            if (rat >= 1.5) {
                startPosition = leftFront!!.position
                leftFront!!.power = -power
                leftBack!!.power = -power
                rightFront!!.power = power
                rightBack!!.power = power
                while (rightFront!!.position - startPosition < travel) {
                }
                leftFront!!.power = power
                leftBack!!.power = power
                rightFront!!.power = -power
                rightBack!!.power = -power
                while (rightFront!!.position - startPosition > -travel) {
                }
                //                dist = travel;
            } else if (rat <= 0.5) {
                startPosition = leftFront!!.position
                leftFront!!.power = power
                leftBack!!.power = power
                rightFront!!.power = -power
                rightBack!!.power = -power
                while (rightFront!!.position - startPosition > -travel) {
                }
                leftFront!!.power = -power
                leftBack!!.power = -power
                rightFront!!.power = power
                rightBack!!.power = power
                while (rightFront!!.position - startPosition < travel) {
                }
                //                dist = -travel;
            }
        }
        robot.stopMotors()
        jewelArm!!.position = 0.0
        Thread.sleep(2000)
        startPosition = leftFront!!.position
        leftFront!!.power = power
        leftBack!!.power = power
        rightFront!!.power = power
        rightBack!!.power = power

        var yes = 1440.0

        when (currentVuMark) {
            RelicRecoveryVuMark.LEFT -> yes = 100.0
            RelicRecoveryVuMark.CENTER -> yes = 300.0
            RelicRecoveryVuMark.RIGHT -> yes = 500.0
        }

        while (leftFront!!.position - startPosition < yes * 0.85 && !Thread.interrupted()) {
        }
        robot.stopMotors()

        Thread.sleep(1000)
        startPosition = leftFront!!.position
        leftFront!!.power = power
        leftBack!!.power = power
        rightFront!!.power = -power
        rightBack!!.power = -power

        while (leftFront!!.position - startPosition < 750 * 0.85 && !Thread.interrupted()) {
        }

        robot.stopMotors()

        Thread.sleep(1000)
        leftFront!!.power = -power
        leftBack!!.power = -power
        rightFront!!.power = -power
        rightBack!!.power = -power

        while (leftFront!!.position - startPosition > -50 * 0.85 && !Thread.interrupted()) {
        }
        robot.stopMotors()

        Thread.sleep(1000)
        leftFront!!.power = power
        leftBack!!.power = power
        rightFront!!.power = power
        rightBack!!.power = power

        while (leftFront!!.position - startPosition + dist < 50 * 0.85 && !Thread.interrupted()) {
        }
        robot.stopMotors()


        while (!Thread.interrupted()) {

        }
    }
}
