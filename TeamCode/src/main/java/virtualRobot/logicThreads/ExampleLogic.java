package virtualRobot.logicThreads;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.ShustinClass;
import virtualRobot.commands.RotateEncoder;
import virtualRobot.commands.Translate;

/**
 * Created by ethachu19 on 3/31/2017.
 *
 *
 */

public class ExampleLogic extends AutonomousLogicThread {
    @Override
    protected void addPresets() {

    }

    @Override
    protected void realRun() throws InterruptedException {
//        runCommand(new Rotate(90))
        boolean last = false;
        while (true) {
            robot.addToTelemetry("Meme", last);
            robot.moveRollerLifts(last);
            last = !last;
            Thread.sleep(1000);
        }
    }
}
