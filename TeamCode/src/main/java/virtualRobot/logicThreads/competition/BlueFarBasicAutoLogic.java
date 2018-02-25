package virtualRobot.logicThreads.competition;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.RotateEncoder;
import virtualRobot.commands.ShustinClass;
import virtualRobot.commands.Translate;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.GlobalUtils;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.UNKNOWN;

/**
 * Created by ethan on 9/22/17.
 */

public class BlueFarBasicAutoLogic extends AutonomousLogicThread {
    private double power = 0.5;

    @Override
    protected void realRun() throws InterruptedException {
        int rot = 0;
        int push = 0;

        if (GlobalUtils.withoutVumark)
            currentVuMark = GlobalUtils.forcedVumark;
        else
            runCommand(new GetVuMarkSide(1000));

        robot.addToTelemetry("Current VuMark: ", currentVuMark);

        robot.moveJewelServo(true);
        Thread.sleep(1000);

        int red = robot.getColorSensor().getRed();
        int blue = robot.getColorSensor().getBlue();
        if ((red != 0 || blue != 0)) {
            double rat = red / (double) blue;
            robot.addToTelemetry("CS", red + " " + blue + " " + rat);
            if (rat <= 0.6) {
                robot.moveJewelRotater(1);
                Thread.sleep(500);
                robot.moveJewelRotater(0);
            } else if (rat >= 1.5) {
                robot.moveJewelRotater(-1);
                Thread.sleep(500);
                robot.moveJewelRotater(0);
            }
        }
        robot.moveJewelServo(false);
        Thread.sleep(500);

        rot = 0;
        if (currentVuMark == UNKNOWN) {
            currentVuMark = LEFT;
        }
        switch (currentVuMark) {
            case LEFT:
                rot = 17; //1926
                push = 700;
                break;
            case CENTER:
                rot = 35; //2126
                push = 850;
                break;
            case RIGHT:
                rot = 48;
                push = 1000;
                break;
        }
        runCommand(new Translate(1100, Translate.Direction.BACKWARD,0,power));
        runCommand(new RotateEncoder(rot,power));
        robot.moveFlipper(true);
        Thread.sleep(500);
        runCommand(new Translate(push, Translate.Direction.BACKWARD, 0, power));
        runCommand(new Translate(300, Translate.Direction.FORWARD, 0, power));
    }
}
