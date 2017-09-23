package virtualRobot.logicThreads.competition;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.EthanClass;
import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;
import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by ethan on 9/22/17.
 */

public class Red1BasicAutoLogic extends AutonomousLogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        runCommand(new Translate(50, Translate.Direction.FORWARD, 0, .5));

        runCommand(new EthanClass());

        if(redIsLeft.get()) {
            robot.getJewelServo().setPosition(1);
            //runCommand(new Rotate(-90, .5));
        }
        else{
            robot.getJewelServo().setPosition(0);
            //runCommand(new Translate(100, Translate.Direction.RIGHT, 0, .5));
            //runCommand(new Rotate(-90, .5));
        }

        runCommand(new Translate(75, Translate.Direction.BACKWARD, 0, .5));
        runCommand(new Translate(100, Translate.Direction.LEFT, 0, .5));

        runCommand(new GetVuMarkSide());

        runCommand(new Translate(200, Translate.Direction.LEFT, 0, .5));

        runCommand(new Rotate(-90,.5));

        if(currentVuMark == RelicRecoveryVuMark.LEFT)
            runCommand(new Translate(300, Translate.Direction.LEFT, 0, .5));
        else if(currentVuMark == RelicRecoveryVuMark.CENTER)
            runCommand(new Translate(250, Translate.Direction.LEFT, 0, .5));
        else if(currentVuMark == RelicRecoveryVuMark.RIGHT)
            runCommand(new Translate(200, Translate.Direction.LEFT, 0, .5));

        runCommand(new Translate(100, Translate.Direction.FORWARD, 0, .5));

        robot.getClawLeft().setPosition(0);
        robot.getClawRight().setPosition(1);

    }
}
