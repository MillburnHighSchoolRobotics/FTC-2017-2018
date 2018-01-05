package virtualRobot.hardware

/**
 * Created by shant on 12/10/2015.
 * Virtual Color Sensor. Can red, blue, green, and alpha of sensor
 * Form of ARGB
 */
class ColorSensor : Sensor() {

    val red: Double
        get() {
            val color = value.toInt()
            return (color shr 16 and 0xFF).toDouble()
        }

    val blue: Double
        get() {
            val color = value.toInt()
            return (color and 0xFF).toDouble()
        }

    val green: Double
        get() {
            val color = value.toInt()
            return (color shr 8 and 0xFF).toDouble()
        }

    val alpha: Double
        get() {
            val color = value.toInt()
            return (color shr 24 and 0xFF).toDouble()
        }
}
