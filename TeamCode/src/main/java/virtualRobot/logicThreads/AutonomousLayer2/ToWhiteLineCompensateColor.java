package virtualRobot.logicThreads.AutonomousLayer2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.SallyJoeBot;
import virtualRobot.Condition;
import virtualRobot.GodThread;
import virtualRobot.LogicThread;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.commands.AllignWithBeacon;
import virtualRobot.commands.MoveMotor;
import virtualRobot.commands.MoveMotorPID;
import virtualRobot.commands.MoveServo;
import virtualRobot.commands.Pause;
import virtualRobot.commands.SpawnNewThread;
import virtualRobot.commands.Translate;
import virtualRobot.commands.WallTrace;
import virtualRobot.commands.killChildren;
import virtualRobot.components.Servo;

/**
 * Created by 17osullivand on 12/2/16.
 */

public class ToWhiteLineCompensateColor extends LogicThread {
    //Note that displacement is handled in exitCondition
    public static final double WALL_TRACE_SONAR_THRESHOLD = 17; //How close we want to trace wall
    public static final double MAX_ALLOWABLE_DISPLACEMENT_TO_FIRST_LINE = 2300; //Max Displacement To The First Line
    public static final double MAX_ALLOWABLE_DISPLACEMENT_TO_SECOND_LINE = 2400; //Max Displacement To The Second Line
    public static final double MAX_DISTANCE_WHEN_COLOR_FAILS = 900;
    public static final double BLIND_ADJUSTMENT_FIRST = 870;
    public static final double BLIND_ADJUSTMENT_SECOND = 1085;
    public static final double ESCAPE_WALL = 400;
    AtomicBoolean allSensorsFail; //has other Line Sensor triggered
    AtomicBoolean lastSensorTriggered, firstSensorTriggered, redIsLeft, maxDistanceReached;
    AtomicBoolean sonarWorks;
    AtomicBoolean colorTriggered;
    VuforiaLocalizerImplSubclass vuforia;
    GodThread.Line type;
    private boolean escapeWall = true;
    private boolean withWallTrace = false;
    private Mode mode = Mode.NORMAL;
    private double maxDistance = Double.MAX_VALUE;


    private static final int whiteTape = 13;
    private final Condition atwhitelineSecondTry = new Condition() {
        public boolean isConditionMet() {//checks if tape or light sensors close to tape are triggered, then checks far one
            if ((robot.getColorSensor().getRed() >= whiteTape && robot.getColorSensor().getBlue() >= whiteTape && robot.getColorSensor().getGreen() >= whiteTape && robot.getColorSensor().getBlue() < 255)) {
                allSensorsFail.set(false);
                colorTriggered.set(true);
                robot.addToProgress("ColorSensorTriggered");
                return true;
            } else if (robot.getLightSensor1().getRawValue() >= .62) {
                if (type == GodThread.Line.RED_FIRST_LINE || type == GodThread.Line.BLUE_SECOND_LINE) {
                   firstSensorTriggered.set(true);} else {
                   lastSensorTriggered.set(true);
                }
                allSensorsFail.set(false);
                robot.addToProgress("LightSensor1Triggered");
                return true;
            } else if (robot.getLightSensor3().getRawValue() >= .62) {
                allSensorsFail.set(false);
                robot.addToProgress("LightSensor3Triggered");
               return true;
            } else if (robot.getLightSensor2().getRawValue() >= .62) {
               allSensorsFail.set(false);
                robot.addToProgress("LightSensor2Triggered");
                return true;
            } else if ((robot.getLightSensor4().getRawValue() >= .62)) {
                allSensorsFail.set(false);
                if (type == GodThread.Line.BLUE_FIRST_LINE || type == GodThread.Line.RED_SECOND_LINE) {
                    firstSensorTriggered.set(true);
                } else {
                    lastSensorTriggered.set(true);
                }
                robot.addToProgress("LightSensor4Triggered");
                return true;
            }

            return false;
        }
    };
    private final Condition atwhitelineFirstTry = new Condition() {
        @Override
        public boolean isConditionMet() {//checks if tape or light sensors close to tape are triggered, then checks far one
            if ((robot.getColorSensor().getRed() >= whiteTape && robot.getColorSensor().getBlue() >= whiteTape && robot.getColorSensor().getGreen() >= whiteTape && robot.getColorSensor().getBlue() < 255)) {
                allSensorsFail.set(false);
                colorTriggered.set(true);
                robot.addToProgress("ColorSensorTriggered");
                return true;
            }

            return false;
        }
    };

    public ToWhiteLineCompensateColor(GodThread.Line type, AtomicBoolean firstSensorTriggered, AtomicBoolean lastSensorTriggered, AtomicBoolean allSensorsFail, AtomicBoolean sonarWorks, AtomicBoolean redIsLeft, AtomicBoolean colorTriggered, VuforiaLocalizerImplSubclass vuforia, Mode mode) {
        super();
        this.type = type;
        this.allSensorsFail = allSensorsFail;
        this.firstSensorTriggered = firstSensorTriggered;
        this.lastSensorTriggered = lastSensorTriggered;
        this.sonarWorks = sonarWorks;
        this.redIsLeft = redIsLeft;
        this.vuforia = vuforia;
        this.mode = mode;
        this.colorTriggered = colorTriggered;
    }

    @Override
    public void realRun() {

        Translate.setGlobalAngleMod(type.getColor() == GodThread.ColorType.RED ? 90 : -90);
        if (mode == Mode.NORMAL) {
            robot.getLFEncoder().clearValue();
            robot.getRFEncoder().clearValue();
            robot.getLBEncoder().clearValue();
            robot.getRBEncoder().clearValue();
            if (type.getLine() == GodThread.LineType.FIRST && sonarWorks.get()) {
                runCommand(new Pause(200));
                runCommand(new Translate(100, Translate.Direction.LEFT, 0).setTolerance(25));
                runCommand(new Pause(200));

            }
            if (type.getLine() == GodThread.LineType.FIRST && !sonarWorks.get() && escapeWall) {
                runCommand(new Translate(ESCAPE_WALL, Translate.Direction.LEFT, 0));
                runCommand(new Pause(200));

            }
            if (type.getLine() == GodThread.LineType.FIRST) {
                if (sonarWorks.get() && withWallTrace) {
                    runCommand(new WallTrace(type.getColor() == GodThread.ColorType.BLUE ? WallTrace.Direction.FORWARD : WallTrace.Direction.BACKWARD, WALL_TRACE_SONAR_THRESHOLD,1500)); //so we don't risk detecting too early

                } else {
                    runCommand(new Translate(1400, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.FORWARD : Translate.Direction.BACKWARD, 0, 1.0)); //so we don't risk detecting too early
                }
                runCommand(new Pause(200));

            }
            if (type.getLine() == GodThread.LineType.SECOND) {
                if (sonarWorks.get() && withWallTrace) {
                    runCommand(new WallTrace(type.getColor() == GodThread.ColorType.BLUE ? WallTrace.Direction.BACKWARD : WallTrace.Direction.FORWARD, WALL_TRACE_SONAR_THRESHOLD,1700)); //so we don't recheck the same line

                } else {
                    runCommand(new Translate(2400, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.BACKWARD : Translate.Direction.FORWARD, 0, 1.0)); //so we don't recheck the same line
                }
                    runCommand(new Pause(200));

            }

            allignWithColor();
        } else if (mode == Mode.COLOR_FAILED) {
            colorFailed();
        }
        else if (mode == Mode.ALL_FAILED) {
            allignBlindly();
        }

    }

    private void allignWithColor() {
        colorTriggered.set(false);
        Translate firstDisplacement;
        if (type.getLine() == GodThread.LineType.FIRST)
            firstDisplacement = new Translate(MAX_ALLOWABLE_DISPLACEMENT_TO_FIRST_LINE, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.FORWARD : Translate.Direction.BACKWARD, 0, .15);
        else
            firstDisplacement = new Translate(MAX_ALLOWABLE_DISPLACEMENT_TO_SECOND_LINE, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.BACKWARD : Translate.Direction.FORWARD, 0, .15);
        firstDisplacement.addCondition(atwhitelineFirstTry, "BREAK");
        runCommand(firstDisplacement);

    }

    private void colorFailed() {
        allSensorsFail.set(true);
        Translate secondDisplacement;
        if (type.getLine() == GodThread.LineType.FIRST)
            secondDisplacement = new Translate(MAX_DISTANCE_WHEN_COLOR_FAILS, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.BACKWARD : Translate.Direction.FORWARD, 0, .15);
        else
            secondDisplacement = new Translate(MAX_DISTANCE_WHEN_COLOR_FAILS, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.FORWARD : Translate.Direction.BACKWARD, 0, .15);
        secondDisplacement.addCondition(atwhitelineSecondTry, "BREAK");
        runCommand(secondDisplacement);
    }

    private void allignBlindly() {
        Translate blindAdjustment;
        if (type.getLine() == GodThread.LineType.FIRST)
            blindAdjustment = new Translate(BLIND_ADJUSTMENT_FIRST, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.FORWARD : Translate.Direction.BACKWARD, 0, .15).setTolerance(25);
        else
            blindAdjustment = new Translate(BLIND_ADJUSTMENT_SECOND, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.BACKWARD : Translate.Direction.FORWARD, 0, .15).setTolerance(25);
        //runCommand(blindAdjustment);
    }





   /*ALLIGNMENT METHODS PAST THIS SHOULD NOT BE USED




    */














    private void withoutAllign() {
        Translate firstDisplacement;
        if (type.getLine() == GodThread.LineType.FIRST)
            firstDisplacement = new Translate(MAX_ALLOWABLE_DISPLACEMENT_TO_FIRST_LINE, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.FORWARD : Translate.Direction.BACKWARD, 0, .2);
        else
            firstDisplacement = new Translate(MAX_ALLOWABLE_DISPLACEMENT_TO_SECOND_LINE, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.BACKWARD : Translate.Direction.FORWARD, 0, .2);
        allSensorsFail.set(true);
        //firstDisplacement.addCondition(atwhitelineSecond);
        runCommand(firstDisplacement);
       /* runCommand(new Pause(500));
        Translate secondDisplacement = new Translate(MAX_ALLOWABLE_DISPLACEMENT_BACK_TO_LINE, type.getColor()== GodThread.ColorType.BLUE ? Translate.Direction.BACKWARD : Translate.Direction.FORWARD, 0, .1);
        secondDisplacement.addCondition(atwhitelineFirst);
        runCommand(secondDisplacement);
        runCommand(new Pause(500));
        allSensorsFail.set(true);
        Translate thirdDisplacement = new Translate(MAX_ALLOWABLE_DISPLACEMENT_BACK_TO_LINE, type.getColor()== GodThread.ColorType.BLUE ? Translate.Direction.FORWARD : Translate.Direction.BACKWARD, 0, .1);
        thirdDisplacement.addCondition(atwhitelineSecond);
        runCommand(thirdDisplacement);*/

            /*if(robot.getLightSensor4().getRawValue()> .61)
                runCommand(new Translate(type.getLine() == GodThread.LineType.FIRST ? 75 : 100,Translate.Direction.BACKWARD,0).setTolerance(50));
            else if(robot.getLightSensor1().getRawValue()> .61)
                runCommand(new Translate(type.getLine() == GodThread.LineType.FIRST ? 75 : 100,Translate.Direction.FORWARD,0).setTolerance(50));

        if(type.getLine() == GodThread.LineType.SECOND && robot.getSonarLeft().getValue()> 21)
            runCommand(new Translate(75,Translate.Direction.FORWARD,0).setTolerance(50));

        if (type.getLine() == GodThread.LineType.FIRST) {
            fireBalls();
        }*/
        runCommand(new Pause(200));
        if (type.getLine() == GodThread.LineType.FIRST)
            runCommand(new AllignWithBeacon(vuforia, redIsLeft, type.getColor() == GodThread.ColorType.BLUE ? AllignWithBeacon.Direction.FORWARD : AllignWithBeacon.Direction.BACKWARD));
        else
            runCommand(new AllignWithBeacon(vuforia, redIsLeft, type.getColor() == GodThread.ColorType.RED ? AllignWithBeacon.Direction.FORWARD : AllignWithBeacon.Direction.BACKWARD));
        runCommand(new Pause(1000));
    }

    private void onlyAllign() {
        //TODO: Add wallTrace instead of translate if sonar works
        if (type.getLine() == GodThread.LineType.FIRST) {
            runCommand(new Translate(1500, type.getColor() == GodThread.ColorType.RED ? Translate.Direction.BACKWARD : Translate.Direction.FORWARD, 0, .2));
            runCommand(new Pause(200));

            runCommand(new AllignWithBeacon(vuforia, redIsLeft, type.getColor() == GodThread.ColorType.BLUE ? AllignWithBeacon.Direction.FORWARD : AllignWithBeacon.Direction.BACKWARD, 3000, maxDistance, maxDistanceReached, type.getColor() == GodThread.ColorType.RED ? 90 : -90));
        } else {
            runCommand(new Translate(1500, type.getColor() == GodThread.ColorType.BLUE ? Translate.Direction.BACKWARD : Translate.Direction.FORWARD, 0, .2));
            runCommand(new Pause(200));
            runCommand(new AllignWithBeacon(vuforia, redIsLeft, type.getColor() == GodThread.ColorType.RED ? AllignWithBeacon.Direction.FORWARD : AllignWithBeacon.Direction.BACKWARD, 3000, maxDistance, maxDistanceReached, type.getColor() == GodThread.ColorType.RED ? 90 : -90));
        }
        runCommand(new Pause(1000));

    }


    private double getAvgDistance() {
        double LFvalue = robot.getLFEncoder().getValue();
        double RFvalue = robot.getRFEncoder().getValue();
        double LBvalue = robot.getLBEncoder().getValue();
        double RBvalue = robot.getRBEncoder().getValue();
        Log.d("AVGDIST", " " + Math.abs((Math.abs(LFvalue) + Math.abs(RFvalue) + Math.abs(LBvalue) + Math.abs(RBvalue)) / 4));
        return (Math.abs(LFvalue) + Math.abs(RFvalue) + Math.abs(LBvalue) + Math.abs(RBvalue)) / 4;
    }

    private void fireBalls() {
        runCommand(new Translate(400, Translate.Direction.BACKWARD_LEFT, 0));
        runCommand(new MoveServo(new Servo[]{robot.getFlywheelStopper()}, new double[]{0})); //move flywheel

        LogicThread spinFlywheel = new LogicThread() {
            @Override
            public void realRun() {
                runCommand(new MoveMotorPID(87, robot.getFlywheel(), robot.getFlywheelEncoder()));
                runCommand(new Pause(1000));

            }
        };
        LogicThread moveReaper = new LogicThread() {
            @Override
            public void realRun() {
                runCommand(new Pause(2000));
                runCommand(new MoveMotor(robot.getReaperMotor(), .21));

            }
        };


        List<LogicThread> threads = new ArrayList<LogicThread>();
        threads.add(spinFlywheel);
        threads.add(moveReaper);

        SpawnNewThread fly = new SpawnNewThread((threads));

        runCommand(fly);
        runCommand(new Pause(5000));
        runCommand(new killChildren(this));
        runCommand(new Translate(400, Translate.Direction.FORWARD_RIGHT, 0));

    }

    public enum Mode {
        NORMAL,
        COLOR_FAILED,
        ALL_FAILED;
    }
}
