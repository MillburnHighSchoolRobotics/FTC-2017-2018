package virtualRobot.hardware

import virtualRobot.utils.Vector3f

/**
 * Created by ethan on 9/21/17.
 */

class IMU {
    private val linearAccel: AxisSensor
    private val totalAccel: AxisSensor

    private val angularVelocity: AxisSensor

    private val yaw: Sensor
    private val pitch: Sensor
    private val roll: Sensor

    @Volatile
    var linearAcquisition: Long = 0
    @Volatile
    var totalAcquisition: Long = 0
    @Volatile
    var angleAcquisition: Long = 0

    val heading: Double
        get() = yaw.value

    val rawHeading: Double
        get() = yaw.rawValue

    val rawPitch: Double
        get() = pitch.rawValue

    val rawRoll: Double
        get() = roll.rawValue

    init {
        linearAccel = AxisSensor()
        totalAccel = AxisSensor()
        linearAcquisition = 0
        totalAcquisition = 0

        angularVelocity = AxisSensor()

        yaw = Sensor()
        pitch = Sensor()
        roll = Sensor()
        angleAcquisition = 0
    }

    fun setLinearAccel(v: Vector3f) {
        linearAccel.setRawValue(v)
    }

    fun setTotalAccel(v: Vector3f) {
        totalAccel.setRawValue(v)
    }

    fun clearLinearAccel() {
        linearAccel.clearValue()
    }

    fun clearTotalAccel() {
        totalAccel.clearValue()
    }

    fun getLinearAccel(): Vector3f {
        return linearAccel.valueVector
    }

    fun getTotalAccel(): Vector3f {
        return totalAccel.valueVector
    }

    fun setAngularVelocity(v: Vector3f) {
        angularVelocity.setRawValue(v)
    }

    fun clearAngularVelocity() {
        angularVelocity.clearValue()
    }

    fun getAngularVelocity(): Vector3f {
        return angularVelocity.valueVector
    }

    fun setYaw(x: Double) {
        yaw.rawValue = x
    }

    fun setPitch(x: Double) {
        pitch.rawValue = x
    }

    fun setRoll(x: Double) {
        roll.rawValue = x
    }

    fun getPitch(): Double {
        return pitch.value
    }

    fun getRoll(): Double {
        return roll.value
    }

    fun clearHeading() {
        yaw.clearValue()
    }

    fun clearPitch() {
        pitch.clearValue()
    }

    fun clearRoll() {
        roll.clearValue()
    }
}
