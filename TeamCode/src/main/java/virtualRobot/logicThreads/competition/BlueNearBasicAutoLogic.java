package virtualRobot.logicThreads.competition;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.Command;
import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.HitJewel;
import virtualRobot.commands.RotateEncoder;
import virtualRobot.commands.Translate;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.Motor;
import virtualRobot.hardware.Servo;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.GlobalUtils;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.CENTER;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.RIGHT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.UNKNOWN;
import static virtualRobot.SallyJoeBot.Team.BLUE;

/**
 * Created by ethan on 9/22/17.
 */

public class BlueNearBasicAutoLogic extends AutonomousLogicThread {

    @Override
    protected void realRun() throws InterruptedException {
        int dist = 0;
//        Thread.sleep(5000);
//        runCommand(new GetVuMarkSide(1000));
        currentVuMark = GlobalUtils.forcedVumark;
        robot.addToTelemetry("Current VuMark: ", currentVuMark);

        robot.moveJewelServo(true);
        Thread.sleep(750);

        Log.d("Progress","Started Jewel");
        int red = robot.getColorSensor().getRed();
        int blue = robot.getColorSensor().getBlue();
        if ((red != 0 || blue != 0)) {
            double rat = red / (double) blue;
            robot.addToTelemetry("CS", red + " " + blue + " " + rat);
            if (rat <= 0.6) {
                Log.d("Progress","Jewel Right");
                robot.moveJewelRotater(1);
                Thread.sleep(500);
                robot.moveJewelRotater(0);
            } else if (rat >= 1.5) {
                Log.d("Progress","Jewel Left");
                robot.moveJewelRotater(-1);
                Thread.sleep(500);
                robot.moveJewelRotater(0);
            }
//            robot.addToProgress("Complete Jewel Servo");
        }
        robot.moveJewelServo(false);
        Log.d("Progress","Ended Jewel");
//        Log.d("Progress", "Jewel Completed");
        Thread.sleep(500);
//        }

        dist = 0;
        if (currentVuMark == UNKNOWN) {
            currentVuMark = LEFT;
        }
        switch (currentVuMark) {
            case LEFT:
                dist = 1250; //1926
                break;
            case CENTER:
                dist = 1550; //2126
                break;
            case RIGHT:
                dist = 1850;
                break;
        }

        Log.d("Progress", "Vumark " + currentVuMark.name());
        runCommand(new Translate(dist, Translate.Direction.BACKWARD,0,0.5));
        runCommand(new RotateEncoder(-90,0.5));
        robot.moveFipper(true);
        Thread.sleep(300);
        runCommand(new Translate(700, Translate.Direction.BACKWARD, 0, 0.5));
        runCommand(new Translate(300, Translate.Direction.FORWARD, 0, 0.5));
    }
}
