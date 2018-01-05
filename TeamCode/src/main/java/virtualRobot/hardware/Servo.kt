package virtualRobot.hardware

import virtualRobot.utils.MathUtils

/**
 * Created by Alex on 9/30/2015.
 * The virtual servo component
 */
class Servo {
    @Volatile
    @get:Synchronized
    var position = 0.0
        @Synchronized set(position) {
            var position = position
            if (java.lang.Double.isNaN(position))
                position = 0.0
            field = MathUtils.clamp(position, 0.0, 1.0)
        }
}
