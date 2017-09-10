package virtualRobot.commands;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.UpdateThread;

import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by Ethan Mak on 9/9/2017.
 */

public class GetVuMarkSide extends Command {
    VuforiaLocalizerImplSubclass vuforia = UpdateThread.vuforiaInstance;
    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;

    public GetVuMarkSide() {
        relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicTestTemplate");
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        relicTrackables.activate();
        RelicRecoveryVuMark mark = RelicRecoveryVuMark.UNKNOWN;
        do {
            mark = RelicRecoveryVuMark.from(relicTemplate);
            if (mark != RelicRecoveryVuMark.UNKNOWN) {
                if (parentThread instanceof AutonomousLogicThread)
                    ((AutonomousLogicThread) parentThread).currentVuMark = mark;
                break;
            }
        } while (mark == RelicRecoveryVuMark.UNKNOWN);
        relicTrackables.deactivate();
        return Thread.currentThread().isInterrupted();
    }
}
