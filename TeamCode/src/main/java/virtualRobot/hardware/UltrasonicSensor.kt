package virtualRobot.hardware

/**
 * Created by 17osullivand on 11/4/16.
 */

class UltrasonicSensor : Sensor() {
    val filteredValue: Double
        get() {
            val levels = DoubleArray(10)
            for (i in levels.indices) {
                levels[i] = value
            }
            for (i in 1 until levels.size) {
                val temp = levels[i]
                var j: Int
                j = i - 1
                while (j >= 0 && temp < levels[j]) {
                    levels[j + 1] = levels[j]
                    j--
                }
                levels[j + 1] = temp
            }
            return levels[levels.size / 2]
        }

}
