package virtualRobot.hardware

import virtualRobot.utils.Vector3f

/**
 * Created by ethachu19 on 12/5/2016.
 */

class AxisSensor {
    internal var values = Vector3f()
    internal var offset = Vector3f()

    //return the current softValue of the sensor
    val valueX: Double
        @Synchronized get() {
            var retVal = 0.0
            synchronized(this) {
                retVal = values.subtract(offset).x
            }
            return retVal
        }

    val valueY: Double
        @Synchronized get() {
            var retVal = 0.0
            synchronized(this) {
                retVal = values.subtract(offset).y
            }
            return retVal
        }

    val valueZ: Double
        @Synchronized get() {
            var retVal = 0.0
            synchronized(this) {
                retVal = values.subtract(offset).z
            }
            return retVal
        }

    val rawValueX: Double
        @Synchronized get() = valueVector.x

    val rawValueY: Double
        @Synchronized get() = valueVector.y

    val rawValueZ: Double
        @Synchronized get() = valueVector.z

    val valueVector: Vector3f
        @Synchronized get() = Vector3f(values).subtract(offset)

    @Synchronized
    fun clearValue() {
        synchronized(this) {
            offset = Vector3f(values)
        }
    }

    //allows the UpdateThread to set the HardValue
    @Synchronized
    fun setRawValue(hardValue: Vector3f) {
        synchronized(this) {
            this.values = hardValue
        }
    }
}
