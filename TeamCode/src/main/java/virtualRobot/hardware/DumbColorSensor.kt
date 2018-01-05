package virtualRobot.hardware

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by david on 11/3/17.
 */

class DumbColorSensor {
    //    private AtomicInteger red;
    //    private AtomicInteger green;
    //    private AtomicInteger blue;
    //	public DumbColorSensor() {
    //		red = new AtomicInteger();
    //		green = new AtomicInteger();
    //		blue = new AtomicInteger();
    //	}

    @get:Synchronized
    @set:Synchronized
    var red: Int = 0
    @get:Synchronized
    @set:Synchronized
    var green: Int = 0
    @get:Synchronized
    @set:Synchronized
    var blue: Int = 0


}
