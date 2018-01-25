package virtualRobot.commands;

import virtualRobot.SallyJoeBot;
import virtualRobot.hardware.ContinuousRotationServo;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.Servo;

/**
 * Created by david on 1/24/18.
 */

public class HitJewel extends Command {

    private static SallyJoeBot robot = ROBOT;
    private SallyJoeBot.Team team;
    private static final double RIGHTPOS = -1, LEFTPOS = 1, CENTERPOS = 0;

    public HitJewel(SallyJoeBot.Team team) {
        this.team = team;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        robot.addToProgress("Begin Jewel Servo");
        Servo arm = robot.getJewelServo();
        ContinuousRotationServo hitter = robot.getJewelHitter();
        arm.setPosition(0.6);
        Thread.sleep(1000);
        DumbColorSensor cs = robot.getColorSensor();
        int red = cs.getRed();
        int blue = cs.getBlue();
        if (red != 0 || blue != 0) {
            blue = Math.max(1, blue);
            double rat = red/(double)blue;
            robot.addToTelemetry("CS", red + " " + blue + " " + rat);
            if (rat >= 1.5) {
                switch (team) {
                    case RED:
                        hitter.setSpeed(LEFTPOS);
                        Thread.sleep(500);
                        hitter.setSpeed(RIGHTPOS);
                        Thread.sleep(500);
                        hitter.setSpeed(CENTERPOS);
                        Thread.sleep(500);
                        break;
                    case BLUE:
                        hitter.setSpeed(RIGHTPOS);
                        Thread.sleep(500);
                        hitter.setSpeed(LEFTPOS);
                        Thread.sleep(500);
                        hitter.setSpeed(CENTERPOS);
                        Thread.sleep(500);
                        break;
                }
            } else if (rat <= 0.6) {
                switch (team) {
                    case RED:
                        hitter.setSpeed(RIGHTPOS);
                        Thread.sleep(500);
                        hitter.setSpeed(LEFTPOS);
                        Thread.sleep(500);
                        hitter.setSpeed(CENTERPOS);
                        Thread.sleep(500);
                        break;
                    case BLUE:
                        hitter.setSpeed(LEFTPOS);
                        Thread.sleep(500);
                        hitter.setSpeed(RIGHTPOS);
                        Thread.sleep(500);
                        hitter.setSpeed(CENTERPOS);
                        Thread.sleep(500);
                        break;
                }
            }
            robot.addToProgress("Complete Jewel Servo");
            arm.setPosition(0.07);
        }
        return false;
    }
}
