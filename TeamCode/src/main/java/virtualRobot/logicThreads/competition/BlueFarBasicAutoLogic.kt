package virtualRobot.logicThreads.competition

import virtualRobot.hardware.DumbColorSensor
import virtualRobot.hardware.Motor
import virtualRobot.hardware.Servo
import virtualRobot.logicThreads.AutonomousLogicThread

/**
 * Created by ethan on 9/22/17.
 */

class BlueFarBasicAutoLogic : AutonomousLogicThread() {
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
        var dist = 0
        val travel = 200
        val sideTravel = 200
        val power = 0.5
        val startPosition: Int
        Thread.sleep(1000)
        val red = colorSensor!!.red
        var blue = colorSensor!!.blue
        robot.addToTelemetry("Red ", red)
        robot.addToTelemetry("Blue ", blue)
        //        blue = Math.max(1, blue);
        val rat = red / blue.toDouble()
        if (red != 0 || blue != 0) {
            blue = Math.max(1, blue)
            if (rat >= 1.5) {
                startPosition = leftFront!!.position
                leftFront!!.power = power
                leftBack!!.power = power
                rightFront!!.power = power
                rightBack!!.power = power
                while (leftFront!!.position - startPosition < travel) {
                }
                dist = travel
            } else if (rat <= 0.5) {
                startPosition = leftFront!!.position
                leftFront!!.power = -power
                leftBack!!.power = -power
                rightFront!!.power = -power
                rightBack!!.power = -power
                while (leftFront!!.position - startPosition > -travel) {
                }
                dist = -travel
            }
        }
        jewelArm!!.position = 0.0
        robot.stopMotors()
        Thread.sleep(2000)
        //        startPosition = leftFront.getPosition();
        //        leftFront.setPower(power);
        //        leftBack.setPower(-power);
        //        rightFront.setPower(-power);
        //        rightBack.setPower(power);
        //        while (leftFront.getPosition() - startPosition < sideTravel) {}
        //        Thread.sleep(2000);
        //        startPosition = leftFront.getPosition();
        //        leftFront.setPower(power);
        //        leftBack.setPower(power);
        //        rightFront.setPower(power);
        //        rightBack.setPower(power);
        //        while ((leftFront.getPosition() - startPosition) + dist < 1440*1 && !Thread.interrupted()) {}
        //        robot.stopMotors();
        while (!Thread.interrupted()) {
        }
    }
}
