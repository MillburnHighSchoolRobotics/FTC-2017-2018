package virtualRobot

import com.qualcomm.robotcore.hardware.CRServo

import org.opencv.core.Mat

import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.ConcurrentHashMap

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import virtualRobot.hardware.ColorSensor
import virtualRobot.hardware.ContinuousRotationServo
import virtualRobot.hardware.DumbColorSensor
import virtualRobot.hardware.IMU
import virtualRobot.hardware.Motor
import virtualRobot.hardware.Sensor
import virtualRobot.hardware.Servo
import virtualRobot.hardware.StateSensor
import virtualRobot.utils.CVTelemetry
import virtualRobot.utils.MatConverterFactory

/**
 * Created by DOSullivan on 9/14/16.
 * All of our our virtual hardware and there getters are housed in SallyJoeBot
 */
class SallyJoeBot {
    //Robot Constants
    @Volatile
    var initialBattery: Double = 0.toDouble()
    val wheelDiameter = 5.0
    val botWidth = 5.0
    val botLength = 5.0

    //Data to pass to UpdateThread
    @get:Synchronized
    val progress: ArrayList<String>
    @get:Synchronized
    val telemetry: ConcurrentHashMap<String, Any>

    //Motors and Servos
    @get:Synchronized
    val lfMotor: Motor
    @get:Synchronized
    val lbMotor: Motor
    @get:Synchronized
    val rfMotor: Motor
    @get:Synchronized
    val rbMotor: Motor
    @get:Synchronized
    val rollerRight: ContinuousRotationServo
    @get:Synchronized
    val rollerLeft: Motor
    @get:Synchronized
    val boxLeft: ContinuousRotationServo
    @get:Synchronized
    val boxRight: ContinuousRotationServo
    @get:Synchronized
    val relicArm: Motor
    @get:Synchronized
    val liftLeft: Motor
    @get:Synchronized
    val liftRight: Motor
    @get:Synchronized
    val relicArmWinch: ContinuousRotationServo
    @get:Synchronized
    val relicArmWrist: Servo
    @get:Synchronized
    val relicArmClaw: Servo
    @get:Synchronized
    val jewelServo: Servo
    //All of Autonomous and TeleopRobot's functions are created e.g. (public synchronized Motor getMotor() {return Motor;}

    @get:Synchronized
    val colorSensor: DumbColorSensor

    //Sensors
    @get:Synchronized
    val imu: IMU
    @get:Synchronized
    val voltageSensor: Sensor
    @get:Synchronized
    val joystickController1: JoystickController
    @get:Synchronized
    val joystickController2: JoystickController
    @get:Synchronized
    val stateSensor: StateSensor

    //CVTelemetry
    private var cvtel: CVTelemetry? = null
    private val ipaddr = "http://172.20.95.207:8080/"

    //Motors, sensors, servos instantiated (e.g Motor = new Motor(), some positions can also be set if desired
    init {
        joystickController1 = JoystickController()
        joystickController2 = JoystickController()
        voltageSensor = Sensor()
        progress = ArrayList()
        telemetry = ConcurrentHashMap()
        stateSensor = StateSensor()
        imu = IMU()

        lfMotor = Motor()
        lbMotor = Motor()
        rfMotor = Motor()
        rbMotor = Motor()
        rollerLeft = Motor()
        rollerRight = ContinuousRotationServo()
        boxLeft = ContinuousRotationServo()
        boxRight = ContinuousRotationServo()
        relicArm = Motor()
        liftLeft = Motor()
        liftRight = Motor()
        relicArmWinch = ContinuousRotationServo()
        relicArmWrist = Servo()
        relicArmClaw = Servo()
        jewelServo = Servo()
        colorSensor = DumbColorSensor()
        //capLift = new SyncedMotors(LiftLeftMotor, LiftRightMotor, LiftLeftEncoder, LiftRightEncoder, KP, KI, KD, SyncedMotors.SyncAlgo.POSITION);
        //capLift.setRatio(1);

    }

    @Synchronized
    fun stopMotors() {
        lfMotor.power = 0.0
        rfMotor.power = 0.0
        lbMotor.power = 0.0
        rbMotor.power = 0.0
    }

    @Synchronized
    fun addToProgress(s: String) {
        progress.add(s)
    }

    @Synchronized
    fun addToTelemetry(s: String, arg: Any) {
        telemetry.put(s, arg)
    }

    @Synchronized
    fun initCVTelemetry() {
        cvtel = Retrofit.Builder()
                .baseUrl(ipaddr)
                .addConverterFactory(MatConverterFactory.create())
                .build()
                .create<CVTelemetry>(CVTelemetry::class.java!!)
    }

    @Synchronized
    @Throws(IOException::class)
    fun sendCVTelemetry(windowName: String, img: Mat): Call<Void> {
        return cvtel!!.sendImage(windowName, img)
    }
}
