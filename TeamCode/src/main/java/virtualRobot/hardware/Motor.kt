package virtualRobot.hardware

import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType

import virtualRobot.SallyJoeBot
import virtualRobot.commands.Command

/*
The virtual Motor component
 */
class Motor {
    protected var robot = Command.ROBOT
    internal var position: Sensor
    internal var motorType: MotorConfigurationType


    @Volatile private var power: Double = 0.toDouble()

    val rawValue: Int
        @Synchronized get() = position.rawValue.toInt()

    init {
        power = 0.0
        position = Sensor()
    }

    @Synchronized
    fun getPower(): Double {
        //		if (robot != null) {
        //			robot.addToTelemetry("motorTime: ", System.currentTimeMillis());
        //		}
        return power
    }

    @Synchronized
    fun setPower(newPower: Double) {
        if (java.lang.Double.isNaN(newPower)) {
            throw IllegalArgumentException("Motor power cannot be NaN")
        }
        power = newPower
        if (power > MAX_POWER) {
            power = 1.0
        }

        if (power < -MAX_POWER) {
            power = -1.0
        }
    }

    @Synchronized
    fun setMotorType(type: MotorConfigurationType): Motor {
        motorType = type
        return this
    }

    @Synchronized
    fun getMotorType(): MotorConfigurationType {
        return motorType
    }

    @Synchronized
    fun setPosition(position: Int) {
        this.position.rawValue = position.toDouble()
    }

    @Synchronized
    fun getPosition(): Int {
        return position.value.toInt()
    }

    @Synchronized
    fun clearEncoder() {
        position.clearValue()
    }

    companion object {
        val MAX_POWER = 1.0
        val STATIONARY = 0.0
    }
}