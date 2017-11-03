package virtualRobot.logicThreads.competition;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.EthanClass;
import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.Translate;
import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by ethan on 9/22/17.
 */

public class RedFarBasicAutoLogic extends AutonomousLogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        runCommand(new Translate(50, Translate.Direction.FORWARD, 0));

        runCommand(new EthanClass());

        if(redIsLeft.get()) {
            robot.getJewelServo().setPosition(1);
        }
        else{
            robot.getJewelServo().setPosition(0);
        }

        runCommand(new Translate(50, Translate.Direction.LEFT, 0));

        runCommand(new GetVuMarkSide());

        runCommand(new Translate(50, Translate.Direction.BACKWARD, 0));

        if (currentVuMark == RelicRecoveryVuMark.LEFT) {
            runCommand(new Translate(150, Translate.Direction.LEFT, 0));
        } else if (currentVuMark == RelicRecoveryVuMark.CENTER) {
            runCommand(new Translate(200, Translate.Direction.LEFT, 0));
        } else if (currentVuMark == RelicRecoveryVuMark.RIGHT) {
            runCommand(new Translate(250, Translate.Direction.LEFT, 0));
        }

        runCommand(new Translate(50, Translate.Direction.FORWARD, 0));

        robot.moveClaw(true);
    }
}
