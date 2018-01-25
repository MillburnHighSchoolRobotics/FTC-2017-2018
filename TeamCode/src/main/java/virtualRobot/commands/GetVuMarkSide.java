package virtualRobot.commands;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by Ethan Mak on 9/9/2017.
 */

public class GetVuMarkSide extends Command {
    VuforiaLocalizerImplSubclass vuforia = GlobalUtils.vuforiaInstance;
    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;
    int timeout;

    public GetVuMarkSide() {
        this(1000);
    }

    public GetVuMarkSide(int timeout) {
        relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicTestTemplate");
        this.timeout = timeout;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        relicTrackables.activate();
        RelicRecoveryVuMark mark = RelicRecoveryVuMark.UNKNOWN;
        long oldTime = System.currentTimeMillis();
        for(int  i = 0; System.currentTimeMillis() - oldTime < timeout && mark == RelicRecoveryVuMark.UNKNOWN; i++) {
            mark = RelicRecoveryVuMark.from(relicTemplate);
//            Log.d("VuMeme", mark.name() + " " + i);
            if (mark != RelicRecoveryVuMark.UNKNOWN) {
                if (parentThread instanceof AutonomousLogicThread)
                    ((AutonomousLogicThread) parentThread).currentVuMark = mark;
                break;
            }
        }
        relicTrackables.deactivate();
        return Thread.currentThread().isInterrupted();
    }
}
