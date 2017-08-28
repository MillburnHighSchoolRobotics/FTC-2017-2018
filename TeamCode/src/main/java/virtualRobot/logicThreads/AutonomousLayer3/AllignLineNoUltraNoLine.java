package virtualRobot.logicThreads.AutonomousLayer3;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.SallyJoeBot;
import virtualRobot.GodThread;
import virtualRobot.LogicThread;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.commands.FTCTakePicture;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;
import virtualRobot.commands.WallTrace;

/**
 * Created by 17osullivand on 11/3/16.
 * Accounts for slight overshoot when going to line
 * EVERYTHING IS BROKEEEN YAAAAY
 */
@Deprecated
public class AllignLineNoUltraNoLine extends LogicThread {
    public static final double CORRECTION_VALUE = AllignLineUltraNoLine.CORRECTION_VALUE; //since we've very much overshot the line, we need to go back;
    public static final double CORRECTION_VALUE_TWO = 1200;

    GodThread.Line type;
    VuforiaLocalizerImplSubclass vuforia;
    AtomicBoolean redIsLeft;
    public AllignLineNoUltraNoLine(GodThread.Line type, AtomicBoolean redIsLeft, VuforiaLocalizerImplSubclass vuforia) {
        this.type = type;
        this.redIsLeft = redIsLeft;
        this.vuforia = vuforia;
    }

    @Override
    public void realRun() {
        robot.addToProgress("Alligning with Line, with NO Ultra and NO Line");
        if (type== GodThread.Line.RED_FIRST_LINE || type== GodThread.Line.BLUE_SECOND_LINE) {
           Translate toWhiteLine2;
            if (type == GodThread.Line.RED_FIRST_LINE)
           toWhiteLine2 =  new Translate(CORRECTION_VALUE, Translate.Direction.FORWARD, 0, .15); //go slowly just to minimize error
            else
                toWhiteLine2 =  new Translate(CORRECTION_VALUE_TWO, Translate.Direction.FORWARD, 0, .15); //go slowly just to minimize error
            runCommand(toWhiteLine2);
            runCommand(new Pause(500));
            runCommand(new Rotate(0, 1));
            runCommand(new Pause(500));
            //FTCTakePicture pic = new FTCTakePicture(redIsLeft,vuforia); //Take a picture of beacon
            //runCommand(pic);
            runCommand(new Pause(500));

        }
        else if (type== GodThread.Line.RED_SECOND_LINE || type== GodThread.Line.BLUE_FIRST_LINE) {
           Translate toWhiteLine2;
            if (type == GodThread.Line.BLUE_FIRST_LINE)
            toWhiteLine2 =  new Translate(CORRECTION_VALUE, Translate.Direction.BACKWARD, 0, .15); //go slowly just to minimize error
            else
                toWhiteLine2 =  new Translate(CORRECTION_VALUE_TWO, Translate.Direction.BACKWARD, 0, .15); //go slowly just to minimize error
            runCommand(toWhiteLine2);
            runCommand(new Pause(500));
            runCommand(new Rotate(0, 1));
            runCommand(new Pause(500));
            //FTCTakePicture pic = new FTCTakePicture(redIsLeft,vuforia); //Take a picture of beacon
            //runCommand(pic);
            runCommand(new Pause(500));

        }

    }


}
