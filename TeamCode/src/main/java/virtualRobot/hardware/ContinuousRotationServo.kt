package virtualRobot.hardware

/**
 * Created by Alex on 10/1/2015.
 * Virtual ContinousRotationServo. Set Position
 */
class ContinuousRotationServo {

    internal var speed = 0.0

    @Synchronized
    fun getSpeed(): Double {
        var retVal = 0.0
        synchronized(this) {
            retVal = speed
        }
        return retVal
    }

    @Synchronized
    fun setSpeed(speed: Double) {
        var speed = speed
        speed = Math.max(Math.min(speed, 1.0), -1.0)
        synchronized(this) {
            this.speed = speed
        }
    }
}
