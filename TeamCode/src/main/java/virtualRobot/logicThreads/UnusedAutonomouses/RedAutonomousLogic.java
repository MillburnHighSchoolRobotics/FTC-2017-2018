package virtualRobot.logicThreads.UnusedAutonomouses;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.SallyJoeBot;
import virtualRobot.Condition;
import virtualRobot.LogicThread;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;

/**
 * Created by DOSullivan on 9/14/2016.
 * Go Trump.
 * Knocks off ball, takes pic of first beacon
 */
@Deprecated
public class RedAutonomousLogic extends LogicThread {
    AtomicBoolean redIsLeft, sonarWorks;
    VuforiaLocalizerImplSubclass vuforia;
    public static double Line = 4;
    public RedAutonomousLogic(AtomicBoolean redIsLeft,VuforiaLocalizerImplSubclass vuforia) {
        super();
        this.redIsLeft = redIsLeft;
        this.vuforia = vuforia;
    }
    @Override
    public void realRun() {

        final double currentLine = robot.getLightSensor1().getRawValue(); //The current value of the color sensor
        Line = currentLine; //So BlueMoveToSecondBeacon knows the current value of the color sensor
        final Condition atwhiteline = new Condition() {
            @Override
            public boolean isConditionMet() {
                if (Math.abs(robot.getLightSensor1().getRawValue() - currentLine) > .7) {
                    return true;
                }
                return false;
            }
        }; //if our line sensor detects a change >.7, we're at the line, stop moving!


        Translate escapeWall = new Translate(1500, Translate.Direction.BACKWARD, 0); //
       runCommand(escapeWall); //Move Away from wall
       runCommand(new Pause(500));
       runCommand(new Rotate(-40, 1)); //Rotate In such a way to glance the ball
        runCommand(new Pause(500));
//        runCommand(
//                new MoveServo(
//                        new Servo[]{robot.getCapServo()},
//                        new double[]{.2}
//                )
//        );
        runCommand(new Pause(500));
        runCommand(new Translate(10000, Translate.Direction.BACKWARD, 0, 1, -40)); //Continue forward (relative to the angle we just rotated to)
        runCommand(new Pause(500));
        runCommand(new Rotate(0, 1)); //Straighten out (note that rotate takes in a target value, not a relative value). So this will return us to the angle we started our bot at.
       runCommand(new Pause(500));
        Translate strafeRight = new Translate(3200, Translate.Direction.RIGHT, 0, .3); //Strafe towards the wall. Stop at 3500 or when the sonar says, "hey you're too close guy"
        strafeRight.addCondition(new Condition() {
            @Override
            public boolean isConditionMet() {
                Log.d("UltraSOUND", "" + robot.getSonarLeft().getValue() + "" + robot.getSonarRight().getValue());

                if (robot.getSonarRight().getValue() < 10 ) {
                    return true;
                }
                return false;
            }
        }, "BREAK");
        runCommand(strafeRight);
       runCommand(new Pause(1000));
        runCommand(new Rotate(0, 1)); //Straighten out again
        runCommand(new Pause(1000));
        // WallTrace toWhiteLine =  new WallTrace(WallTrace.Direction.BACKWARD, 8); //Move towards the white line, staying parallel to the wall, until the white line is deteceted
        Translate toWhiteLine = new Translate(Translate.RunMode.CUSTOM, Translate.Direction.BACKWARD, 0, .15);
        toWhiteLine.addCondition(atwhiteline, "BREAK");
        runCommand(toWhiteLine);
        robot.addToProgress("Went to Line");
        runCommand(new Pause(500));
        //WallTrace toWhiteLine2 =  new WallTrace(WallTrace.Direction.FORWARD, 8); //To account for slight overshoot of beacon thanks to momentum
        Translate toWhiteLine2 = new Translate(Translate.RunMode.CUSTOM, Translate.Direction.FORWARD, 0, .15);
        toWhiteLine2.addCondition(atwhiteline, "BREAK");
        runCommand(toWhiteLine2);
        /*runCommand(new Rotate(0, 1));
        runCommand(new Pause(2000));
        Translate moveToWall2 =  new Translate(Translate.RunMode.CUSTOM, Translate.Direction.RIGHT, 0, .2);
        moveToWall2.addCondition(
                new Condition() {
                    @Override
                    public boolean isConditionMet() {
                        Log.d("UltraSOUND", "" + robot.getSonarLeft().getValue() + "" + robot.getSonarRight().getValue());

                        if (robot.getSonarRight().getValue() < 9  ) {
                            return true;
                        }
                        return false;
                    }
                });
        runCommand(new Pause(2000));
        runCommand(new Rotate(0, 1));
        runCommand(new Pause(2000));*/
        runCommand(new Pause(500));
        runCommand(new Rotate(0, 1));
        runCommand(new Pause(500));
        // FTCTakePicture pic = new FTCTakePicture(redIsLeft,vuforia); //Take a picture of beacon
        //runCommand(pic);


        //Strafe left to move towards wall
        /*
        Translate moveToWall = new Translate(1000, Translate.Direction.RIGHT, -10);
        moveToWall.addCondition(new Condition() {
            @Override
            public boolean isConditionMet() {
               if (robot.getUltrasonicSensor().getValue() < 10) {
                    return true;
                }
                return false;
            }
        });

        //Strafe to first white line
        Translate moveToWhiteLine = new Translate(1000, Translate.Direction.BACKWARD, 0);
        moveToWhiteLine.addCondition(atwhiteline);
        runCommand(moveToWhiteLine);
*/
    }
}
