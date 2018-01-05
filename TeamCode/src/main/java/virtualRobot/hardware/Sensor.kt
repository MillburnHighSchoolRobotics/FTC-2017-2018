package virtualRobot.hardware

/**
 * Created by shant on 10/8/2015.
 *
 * A class that reads in values from the sensors, including motors.
 * All sensors and encoders should extend this class
 * Motor Encoders, Color Sensor, etc should use this class
 */
open class Sensor {
    //allows the UpdateThread to set the HardValue
    @Volatile
    @get:Synchronized
    @set:Synchronized
    var rawValue: Double = 0.toDouble()
    @Volatile protected var offset: Double = 0.toDouble()

    //return the current softValue of the sensor
    val value: Double
        @Synchronized get() = rawValue - offset


    //Soft clears a sensor or encoder value
    @Synchronized
    fun clearValue() {
        offset = rawValue
    }

    @Synchronized
    fun incrementRawValue(delta: Double) {
        this.rawValue += delta
    }

}
