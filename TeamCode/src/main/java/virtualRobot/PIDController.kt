package virtualRobot

/**
 * Created by Yanjun on 10/14/2015.
 * Our PID Controller implementation.
 */
class PIDController() {
    var kp: Double = 0.toDouble()
    private var kI: Double = 0.toDouble()
    private var kD: Double = 0.toDouble()
    private var threshold: Double = 0.toDouble()
    private val switchSign = false
    private val maxI = java.lang.Double.MAX_VALUE

    private var P: Double = 0.toDouble()
    private var I: Double = 0.toDouble()
    private var D: Double = 0.toDouble()

    var target: Double = 0.toDouble()

    init {
        kp = 0.0
        kI = 0.0
        kD = 0.0
        threshold = 0.0

        P = 0.0
        I = 0.0
        D = 0.0
    }

    constructor(kP: Double, kI: Double, kD: Double) : this() {

        this.kp = kP
        this.kI = kI
        this.kD = kD

        threshold = 0.0
    }

    constructor(kP: Double, kI: Double, kD: Double, threshold: Double) : this(kP, kI, kD) {

        this.threshold = threshold
    }

    constructor(kP: Double, kI: Double, kD: Double, threshold: Double, target: Double) : this(kP, kI, kD, threshold) {

        this.target = target
    }

    constructor(kP: Double, kI: Double, kD: Double, threshold: Double, target: Double, switchSign: Boolean) : this(kP, kI, kD, threshold, target) {
        this.switchSign = switchSign
    }

    constructor(kP: Double, kI: Double, kD: Double, threshold: Double, target: Double, switchSign: Boolean, maxValue: Double) : this(kP, kI, kD, threshold, target, switchSign) {
        this.maxI = maxValue
    }

    fun getPIDOutput(currentValue: Double): Double {
        D = target - currentValue - P
        P = target - currentValue

        if (!switchSign) {
            if (Math.abs(currentValue - target) < threshold) {
                I = P + I
            } else {
                I = 0.0
            }
        } else {
            if (Math.abs(currentValue - target) > threshold) {
                I = P + I
            } else {
                I = 0.0
            }
        }

        I = Math.min(I, maxI)

        return kp * P + kI * I + kD * D
    }

    fun setThreshold(threshold: Double) {
        this.threshold = threshold
    }

    fun setKI(kI: Double) {
        this.kI = kI
    }

    fun setKD(kD: Double) {
        this.kD = kD
    }

    override fun toString(): String {
        return P.toString() + " " + I + " " + D
    }
}
