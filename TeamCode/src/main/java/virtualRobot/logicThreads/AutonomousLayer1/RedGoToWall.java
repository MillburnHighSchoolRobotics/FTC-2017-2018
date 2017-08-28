package virtualRobot.logicThreads.AutonomousLayer1;

import android.util.Log;

import org.firstinspires.ftc.teamcode.UpdateThread;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.SallyJoeBot;
import virtualRobot.Condition;
import virtualRobot.LogicThread;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.Translate;

/**
 * Created by 17osullivand on 11/3/16.
 * Knocks ball, strafes to wall
 */

public class RedGoToWall extends LogicThread  {
    public static final boolean WITH_SONAR = UpdateThread.WITH_SONAR; //Are we using a sonar?
    public static final double CLOSE_TO_WALL = 16 ; //How close we want to strafe to wall
    public static final double SONAR_ERROR_MAX = CLOSE_TO_WALL+3; //the threshold at which if a sonar is >= than this when at wall, it's wrong
    public static final double SONAR_ERROR_MIN = CLOSE_TO_WALL-3; //the threshold at which if a sonar is <= than this when at wall, it's wrong
    protected static final double INT_ANGLE = -BlueGoToWall.INT_ANGLE;
  AtomicBoolean sonarWorks;

    public RedGoToWall(AtomicBoolean sonarWorks) {
        super();
        this.sonarWorks = sonarWorks;
    }
    @Override
    public void realRun() {
        warrenPlan();
        runCommand(new Pause(500));
        robot.addToProgress("Went To Wall");

        /*runCommand(new Pause(500));
        Translate escapeWall = new Translate(500, Translate.Direction.BACKWARD_LEFT, 0); //
        runCommand(escapeWall); //Move Away from wall
        runCommand(new Pause(100));
        //runCommand(new Rotate(INT_ANGLE, 1)); //Rotate In such a way to glance the ball
        runCommand(new Translate(5500, Translate.Direction.BACKWARD_RIGHT, 0));
        runCommand(new Pause(200));
        runCommand(new Translate(500, Translate.Direction.BACKWARD, 0)); //Continue Backward (relative to the angle we just rotated to)
        runCommand(new Pause(200));
        runCommand(new Rotate(0, .5, 1000)); //Straighten out (note that rotate takes in a target value, not a relative value). So this will return us to the angle we started our bot at.
        runCommand(new Pause(200));
        Translate strafeRight = new Translate(1950, Translate.Direction.RIGHT, 0, .3); //Strafe towards the wall. Stop at 2000 or when the sonar says, "hey you're too close guy"


        if (WITH_SONAR) {
            strafeRight.addCondition(new Condition() {
                @Override
                public boolean isConditionMet() {
                    double sonarRight = robot.getSonarRight().getFilteredValue();
                    double sonarLeft = robot.getSonarLeft().getFilteredValue();
                    Log.d("UltraSOUND", "" + robot.getSonarLeft().getValue() + "" + robot.getSonarRight().getValue());
                    if (sonarRight <= SONAR_ERROR_MIN || sonarLeft <= SONAR_ERROR_MIN || sonarRight >= SONAR_ERROR_MAX || sonarLeft >= SONAR_ERROR_MAX) {
                        sonarWorks.set(false);

                    } else if (sonarRight < CLOSE_TO_WALL || sonarLeft < CLOSE_TO_WALL) {
                        sonarWorks.set(true);
                        robot.addToProgress("SONAR GOOD DATA");
                        return true;
                    } else {
                        sonarWorks.set(true);
                    }
                    return false;
                }
            });
        }
        runCommand(strafeRight);
        runCommand(new Pause(200));
        runCommand(new Rotate(0, .5, 700)); //Straighten out again
        runCommand(new Pause(200));
        robot.addToProgress("Went To Wall");*/
}
private void davePlan(){ //head to first beacon, in another thread we'll shoot
    runCommand(new Pause(500));
    Translate escapeWall = new Translate(800, Translate.Direction.BACKWARD, 0); //
    runCommand(escapeWall); //Move Away from wall
    runCommand(new Pause(100));
    ///runCommand(new Rotate(INT_ANGLE, 1)); //Rotate In such a way to glance the ball
    runCommand(new Rotate(-45, .5, 2000));
    runCommand(new Pause(500));
    runCommand(new Translate(4500, Translate.Direction.BACKWARD, 0,1, -45));
    runCommand(new Pause(200));
    runCommand(new Rotate(0, .5, 1000)); //Straighten out (note that rotate takes in a target value, not a relative value). So this will return us to the angle we started our bot at.
    runCommand(new Pause(200));
    /*Translate strafeRight = new Translate(1950, Translate.Direction.RIGHT, 0, .3); //Strafe towards the wall. Stop at 2000 or when the sonar says, "hey you're too close guy"


    if (WITH_SONAR) {
        strafeRight.addCondition(new Condition() {
            @Override
            public boolean isConditionMet() {
                double sonarRight = robot.getSonarRight().getFilteredValue();
                double sonarLeft = robot.getSonarLeft().getFilteredValue();
                Log.d("UltraSOUND", "" + robot.getSonarLeft().getValue() + "" + robot.getSonarRight().getValue());
                if (sonarRight <= SONAR_ERROR_MIN || sonarLeft <= SONAR_ERROR_MIN || sonarRight >= SONAR_ERROR_MAX || sonarLeft >= SONAR_ERROR_MAX) {
                    sonarWorks.set(false);

                } else if (sonarRight < CLOSE_TO_WALL || sonarLeft < CLOSE_TO_WALL) {
                    sonarWorks.set(true);
                    robot.addToProgress("SONAR GOOD DATA");
                    return true;
                } else {
                    sonarWorks.set(true);
                }
                return false;
            }
        });
    }
    runCommand(strafeRight);*/
    robot.addToProgress("Went To Wall");
    }
    private void warrenPlan() { //We've already fired balls and are on our way to the second beacon.
        runCommand(new Rotate(53.5, .5, 1500));
        runCommand(new Pause(200));
        runCommand(new Translate(5300, Translate.Direction.BACKWARD, 0,1,50));
        runCommand(new Pause(200));
        runCommand(new Rotate(90, .3, 1500));
        runCommand(new Pause(200));
        Translate strafeRight = new Translate(1500, Translate.Direction.RIGHT, 0, .3); //Strafe towards the wall. Stop at 2000 or when the sonar says, "hey you're too close guy"


        if (WITH_SONAR) {
            strafeRight.addCondition(new Condition() {
                @Override
                public boolean isConditionMet() {
                    double sonarRight = robot.getSonarRight().getFilteredValue();
                    double sonarLeft = robot.getSonarLeft().getFilteredValue();
                    Log.d("UltraSOUND", "" + robot.getSonarLeft().getValue() + "" + robot.getSonarRight().getValue());
                    if (sonarRight <= SONAR_ERROR_MIN || sonarLeft <= SONAR_ERROR_MIN || sonarRight >= SONAR_ERROR_MAX || sonarLeft >= SONAR_ERROR_MAX) {
                        sonarWorks.set(false);

                    } else if (sonarRight < CLOSE_TO_WALL || sonarLeft < CLOSE_TO_WALL) {
                        sonarWorks.set(true);
                        robot.addToProgress("SONAR GOOD DATA");
                        return true;
                    } else {
                        sonarWorks.set(true);
                    }
                    return false;
                }
            }, "BREAK");
        }
        runCommand(strafeRight);
    }
    private void mehmetPlan() { //Shoot ball go to first beacon

        runCommand(new Rotate(30, 0, 1000));
        runCommand(new Pause(500));
        runCommand(new Translate(3500, Translate.Direction.BACKWARD, 0,1,30));
        runCommand(new Pause(500));
        runCommand(new Rotate(90, .5, 1000));
        runCommand(new Pause(500));
        Translate strafeRight = new Translate(1950, Translate.Direction.RIGHT, 0, .3); //Strafe towards the wall. Stop at 2000 or when the sonar says, "hey you're too close guy"

        if (WITH_SONAR) {
            strafeRight.addCondition(new Condition() {
                @Override
                public boolean isConditionMet() {
                    double sonarRight = robot.getSonarRight().getFilteredValue();
                    double sonarLeft = robot.getSonarLeft().getFilteredValue();
                    Log.d("UltraSOUND", "" + robot.getSonarLeft().getValue() + "" + robot.getSonarRight().getValue());
                    if (sonarRight <= SONAR_ERROR_MIN || sonarLeft <= SONAR_ERROR_MIN || sonarRight >= SONAR_ERROR_MAX || sonarLeft >= SONAR_ERROR_MAX) {
                        sonarWorks.set(false);

                    } else if (sonarRight < CLOSE_TO_WALL || sonarLeft < CLOSE_TO_WALL) {
                        sonarWorks.set(true);
                        robot.addToProgress("SONAR GOOD DATA");
                        return true;
                    } else {
                        sonarWorks.set(true);
                    }
                    return false;
                }
            }, "BREAK");
        }
        runCommand(strafeRight);
        runCommand(new Pause(200));
    }

}
