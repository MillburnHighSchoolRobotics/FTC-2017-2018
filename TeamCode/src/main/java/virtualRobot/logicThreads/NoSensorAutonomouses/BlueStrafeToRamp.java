package virtualRobot.logicThreads.NoSensorAutonomouses;

import virtualRobot.SallyJoeBot;
import virtualRobot.LogicThread;
import virtualRobot.commands.MoveMotor;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;

/**
 * Created by 17osullivand on 10/28/16.
 * Gets on ramp, deposits balls
 */
@Deprecated
public class BlueStrafeToRamp extends LogicThread {
    @Override
    public void realRun (){
        runCommand(new Translate(7000, Translate.Direction.BACKWARD, 0));
        runCommand(new Pause(500));
        runCommand(new Translate(5500, Translate.Direction.BACKWARD_LEFT,0)); //move away from beacon towards corner of field in front of ramp
        runCommand(new Pause(500));
        runCommand(new Rotate(335)); //Rotate to face ramp
        runCommand(new Pause(500));
        runCommand(new Translate(3000, Translate.Direction.FORWARD,0)); //Get onto Ramp
//        runCommand(new MoveMotor(robot.getReaperMotor())); //Spin the balls into ramp
    }
}
