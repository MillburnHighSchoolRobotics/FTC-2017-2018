package virtualRobot.hardware

import virtualRobot.SallyJoeBot
import virtualRobot.utils.MathUtils
import virtualRobot.utils.Matrix
import virtualRobot.utils.Vector3f

/**
 * Created by ethachu19 on 12/1/16
 * StateSensor detects robots velocity and position
 */
class StateSensor : Sensor() {
    @set:Synchronized
    var position: Vector3f? = null
        @Synchronized get() = Vector3f(field)
    @set:Synchronized
    var velocity: Vector3f? = null
        @Synchronized get() = Vector3f(field)
    private var robot: SallyJoeBot? = null

    init {
        this.position = Vector3f()
        this.velocity = Vector3f()
    }

    fun setRobot(robot: SallyJoeBot): StateSensor {
        this.robot = robot
        return this
    }
}
