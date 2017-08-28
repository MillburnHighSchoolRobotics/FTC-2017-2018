package virtualRobot.logicThreads.NoSensorAutonomouses;

import virtualRobot.SallyJoeBot;
import virtualRobot.LogicThread;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;

/**
 * Created by 17osullivand on 11/18/16.
 */

public class RedStrafeToCenterGoal extends LogicThread {
    @Override
    public void realRun (){
        Translate.setGlobalAngleMod(0);
        runCommand(new Translate(5000, Translate.Direction.LEFT,0)); //move away from beacon towards corner of field in front of ramp
        runCommand(new Pause(100));
        runCommand(new Rotate(160, .5, 1500));
        runCommand(new Pause(100));
        runCommand(new Translate(600, Translate.Direction.BACKWARD, 0, 1, 180, "To Ramp", 1000));
        runCommand(new Pause(100));


    }
}
