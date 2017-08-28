package virtualRobot.logicThreads.AutonomousLayer3;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.SallyJoeBot;
import virtualRobot.Condition;
import virtualRobot.GodThread;
import virtualRobot.LogicThread;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.WallTrace;
import virtualRobot.logicThreads.AutonomousLayer2.ToWhiteLine;

/**
 * Created by 17osullivand on 11/3/16.
 * Accounts for slight overshoot when going to line
 */
@Deprecated
public class AllignLineUltraLine extends LogicThread  {
    GodThread.Line type;
    double currentLine;
    public static final double WALL_TRACE_SONAR_THRESHOLD = ToWhiteLine.WALL_TRACE_SONAR_THRESHOLD; //How close we want to trace wall
    public static final double CORRECTION_VALUE = AllignLineUltraNoLine.CORRECTION_VALUE; //since we've very much overshot the line, we need to go back;
    public static final double CORRECTION_VALUE_TWO = 1200;
    VuforiaLocalizerImplSubclass vuforia;
    AtomicBoolean redIsLeft;
    public AllignLineUltraLine(GodThread.Line type, double currentLine, AtomicBoolean redIsLeft, VuforiaLocalizerImplSubclass vuforia) {
        this.type = type;
        this.currentLine = currentLine;
        this.redIsLeft = redIsLeft;
        this.vuforia = vuforia;
    }

    @Override
    public void realRun() {
        final Condition atwhiteline = new Condition() {
            @Override
            public boolean isConditionMet() {
                if (Math.abs(robot.getLightSensor1().getRawValue() - currentLine) > 1.85 || robot.getLightSensor1().getRawValue() > .73) {
                    return true;
                }
                return false;
            }
        };
        robot.addToProgress("Alligning with Line, with Ultra and Line");
       if (type==GodThread.Line.RED_FIRST_LINE || type==GodThread.Line.BLUE_SECOND_LINE) {
           WallTrace toWhiteLine2;
           //if (type==GodThread.Line.RED_FIRST_LINE)
           //toWhiteLine2 =  new WallTrace(WallTrace.Direction.FORWARD,  WALL_TRACE_SONAR_THRESHOLD, CORRECTION_VALUE);
           //else
               //toWhiteLine2 =  new WallTrace(WallTrace.Direction.FORWARD,  WALL_TRACE_SONAR_THRESHOLD, CORRECTION_VALUE_TWO);

           //toWhiteLine2.addCondition(atwhiteline);
           //runCommand(toWhiteLine2);
           runCommand(new Pause(500));
           runCommand(new Rotate(0, 1));
           runCommand(new Pause(500));
           //FTCTakePicture pic = new FTCTakePicture(redIsLeft,vuforia); //Take a picture of beacon
           //runCommand(pic);
           runCommand(new Pause(500));

       }
       else if (type==GodThread.Line.RED_SECOND_LINE || type==GodThread.Line.BLUE_FIRST_LINE) {
           WallTrace toWhiteLine2;
              // if (type==GodThread.Line.BLUE_FIRST_LINE)
          // toWhiteLine2 =  new WallTrace(WallTrace.Direction.BACKWARD,  WALL_TRACE_SONAR_THRESHOLD, CORRECTION_VALUE);
           //else
                   //toWhiteLine2 =  new WallTrace(WallTrace.Direction.BACKWARD,  WALL_TRACE_SONAR_THRESHOLD, CORRECTION_VALUE_TWO);
           //toWhiteLine2.addCondition(atwhiteline);
           //runCommand(toWhiteLine2);
           runCommand(new Pause(500));
           runCommand(new Rotate(0, 1));
           runCommand(new Pause(500));
           //FTCTakePicture pic = new FTCTakePicture(redIsLeft,vuforia); //Take a picture of beacon
           //runCommand(pic);
           runCommand(new Pause(500));

       }

    }


}
