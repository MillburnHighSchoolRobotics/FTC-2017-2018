package virtualRobot.hardware

import virtualRobot.PIDController
import virtualRobot.utils.MathUtils

/**
 * Created by ethachu19 on 9/23/2016.
 * A purelely virtual component with no direct physical component.
 * It represents two other virtual motors (or two sets of motors),
 * and syncs those two motors or two sets of motors
 */
class SyncedMotors {
    private var type: SyncMode? = null
    private var algo: SyncAlgo? = null
    internal var masterA: Motor
    internal var slaveA: Motor
    private var oldTimeA: Long = 0
    private var oldTimeB: Long = 0
    private var oldEncoderA: Double = 0.toDouble()
    private var oldEncoderB: Double = 0.toDouble()
    val encoder: Sensor
    private val encoderB: Sensor
    internal var masterB: SyncedMotors
    internal var slaveB: SyncedMotors
    private var pid: PIDController? = null
    private var ratio: Double = 0.toDouble()
    private var lastRatio: Double = 0.toDouble()
    private var power: Double = 0.toDouble()
    @get:Synchronized
    var speedRatio: Double = 0.toDouble()
        private set
    private var second0 = false
    private var first0 = false

    val speedA: Double
        @Synchronized get() {
            if (type == SyncMode.MOTORS) {
                val temp = encoder.value
                val tempTime = System.currentTimeMillis()
                val res = (temp - oldEncoderA) * 1000 / if (tempTime - oldTimeA == 0L) 1 else tempTime - oldTimeA
                oldTimeA = tempTime
                oldEncoderA = temp
                return res
            }
            return masterB.speedA
        }

    val speedB: Double
        @Synchronized get() {
            if (type == SyncMode.MOTORS) {
                val temp = encoderB.value
                val tempTime = System.currentTimeMillis()
                val res = (temp - oldEncoderB) * 1000 / if (tempTime - oldTimeB == 0L) 1 else tempTime - oldTimeB
                oldTimeB = tempTime
                oldEncoderB = temp
                return res
            }
            return slaveB.speedA
        }

    constructor(a: Motor, b: Motor, eA: Sensor, eB: Sensor, KP: Double, KI: Double, KD: Double, algo: SyncAlgo) {
        this.masterA = a
        this.slaveA = b
        this.encoder = eA
        this.encoderB = eB
        type = SyncMode.MOTORS
        this.algo = algo
        if (algo == SyncAlgo.SPEED) {
            pid = PIDController(KP, KI, KD, 0.01, 1.0)
        } else {
            pid = PIDController(KP, KI, KD, 10.0, 1.0)
        }
        this.encoder.clearValue()
        this.encoderB.clearValue()
    }

    constructor(a: SyncedMotors, b: SyncedMotors, KP: Double, KI: Double, KD: Double, algo: SyncAlgo) {
        this.masterB = a
        this.slaveB = b
        this.algo = algo
        if (algo == SyncAlgo.SPEED) {
            pid = PIDController(KP, KI, KD, 0.01, 1.0)
        } else {
            pid = PIDController(KP, KI, KD, 5.0, 0.0)
        }
        type = SyncMode.SIDES
    }

    @Synchronized
    fun setRatio(ratio: Double) {
        this.ratio = ratio
        this.pid!!.target = ratio
    }

    @Synchronized
    fun setRatioAndPower(pw1: Double, pw2: Double) {
        if (pw1 == 0.0) {
            this.first0 = true
            this.power = pw2
            move()
            return
        } else
            this.first0 = false
        if (pw2 == 0.0) {
            this.second0 = true
            this.power = pw1
            move()
            return
        } else
            this.second0 = false
        this.ratio = pw1 / pw2
        this.pid!!.target = ratio
        this.power = pw1
        if (!MathUtils.equals(ratio, lastRatio)) {
            zeroEncoders()
            lastRatio = ratio
        }
        move()
    }

    @Synchronized
    fun setPower(power: Double) {
        this.power = MathUtils.clamp(power, -1.0, 1.0)
        move()
    }

    @Synchronized
    fun move() {
        var adjust = 0.0
        if (!first0 || !second0) {
            if (algo == SyncAlgo.SPEED) {
                val speedA = speedA
                val speedRatio = if (speedA == 0.0) 0 else speedB / speedA
                this.speedRatio = speedRatio
                adjust = pid!!.getPIDOutput(speedRatio)
            } else {
                adjust = if (type == SyncMode.MOTORS) pid!!.getPIDOutput(encoder.value * ratio - encoderB.value) else pid!!.getPIDOutput(masterB.encoder.value * ratio - slaveB.encoder.value)
                speedRatio = adjust
            }
        }
        var slavePower = MathUtils.clamp(power * ratio + adjust, -1.0, 1.0)
        var realPower = MathUtils.clamp(power - adjust, -1.0, 1.0)
        if (first0) {
            realPower = 0.0
            slavePower = power
        }
        if (second0) {
            slavePower = 0.0
            realPower = power
        }
        if (type == SyncMode.MOTORS) {
            masterA.setPower(realPower)
            slaveA.setPower(slavePower)
        } else {
            masterB.setPower(realPower)
            slaveB.setPower(slavePower)
            masterB.move()
            slaveB.move()
        }
    }

    @Synchronized
    fun desync() {

    }

    @Synchronized
    fun zeroEncoders() {
        if (type == SyncMode.MOTORS) {
            encoder.clearValue()
            encoderB.clearValue()
        } else {
            masterB.zeroEncoders()
            slaveB.zeroEncoders()
        }
    }

    internal enum class SyncMode {
        MOTORS, SIDES
    }

    enum class SyncAlgo {
        POSITION, SPEED
    }
}
