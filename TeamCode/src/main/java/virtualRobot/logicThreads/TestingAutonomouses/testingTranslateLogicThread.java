package virtualRobot.logicThreads.TestingAutonomouses;

import virtualRobot.LogicThread;
import virtualRobot.commands.Translate;

/**
 * Created by ethachu19 on 10/11/2016.
 */

public class testingTranslateLogicThread extends LogicThread {

    @Override
    public void realRun() {
        /*robot.getLFEncoder().clearValue();
        robot.getRFEncoder().clearValue();
        robot.getLBEncoder().clearValue();
        robot.getRBEncoder().clearValue();*/
        Translate a = new Translate(10000, Translate.Direction.FORWARD, 0,1,0,null,5000);
        Translate b = new Translate(5000, Translate.Direction.BACKWARD, 0,1,0,null,5000);
        Translate c = new Translate(5000, Translate.Direction.FORWARD_LEFT, 0,1,0,null,5000);
        Translate d = new Translate(5000, Translate.Direction.BACKWARD_RIGHT, 0,1,0,null,5000);
        runCommand(a);
//        runCommand(new Rotate(-90));
//        runCommand(new Pause(500));
//        Translate.setGlobalAngleMod(-90);
//        runCommand(new Translate(1500, Translate.Direction.FORWARD, 0, .2));
//        runCommand(new Pause(2000));
//        runCommand(b);
//        runCommand(new Pause(2000));
//        runCommand(c);
//        runCommand(new Pause(2000));
//        runCommand(d);
//

    }
}
