package virtualRobot.logicThreads.NoSensorAutonomouses;

import virtualRobot.SallyJoeBot;
import virtualRobot.LogicThread;
import virtualRobot.commands.MoveMotor;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;

/**
 * Created by Warren on 10/6/2016.
 * For Red autonomous. Legalize it (still).
 * Gets on ramp, deposits balls
 */
@Deprecated
public class RedStrafeToRamp extends LogicThread {
    @Override
    public void realRun (){
        runCommand(new Translate(7000, Translate.Direction.FORWARD, 0));
        runCommand(new Pause(500));
        runCommand(new Translate(5500, Translate.Direction.FORWARD_LEFT,0));
        runCommand(new Pause(500));
        runCommand(new Rotate(25)); //Rotate to face ramp
        runCommand(new Pause(500));
        runCommand(new Translate(3000, Translate.Direction.FORWARD,0)); //Get onto Ramp
//        runCommand(new MoveMotor(robot.getReaperMotor())); //Spin balls into ramp
    }
}
