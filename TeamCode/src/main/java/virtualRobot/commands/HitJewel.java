package virtualRobot.commands;

import virtualRobot.SallyJoeBot;
import virtualRobot.hardware.DumbColorSensor;
import virtualRobot.hardware.Servo;

/**
 * Created by david on 1/24/18.
 */

public class HitJewel extends Command {

    private static SallyJoeBot robot = ROBOT;
    private SallyJoeBot.Team team;
    private static final double RIGHTPOS = 0, LEFTPOS = 1, CENTERPOS = 0.5;

    public HitJewel(SallyJoeBot.Team team) {
        this.team = team;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        Servo arm = robot.getJewelServo();
        Servo hitter = robot.getJewelHitter();
        arm.setPosition(0.67);
        Thread.sleep(1000);
        DumbColorSensor cs = robot.getColorSensor();
        int red = cs.getRed();
        int blue = cs.getBlue();
        if (red != 0 || blue != 0) {
            blue = Math.max(1, blue);
            double rat = red/(double)blue;
            if (rat >= 1.2) {
                switch (team) {
                    case RED:
                        hitter.setPosition(LEFTPOS);
                        Thread.sleep(500);
                        hitter.setPosition(CENTERPOS);
                        Thread.sleep(500);
                        Thread.sleep(500);
                        break;
                    case BLUE:
                        hitter.setPosition(RIGHTPOS);
                        Thread.sleep(500);
                        hitter.setPosition(CENTERPOS);
                        Thread.sleep(500);
                        break;
                }
            } else if (rat <= 0.8) {
                switch (team) {
                    case RED:
                        hitter.setPosition(RIGHTPOS);
                        Thread.sleep(500);
                        hitter.setPosition(CENTERPOS);
                        Thread.sleep(500);
                        break;
                    case BLUE:
                        hitter.setPosition(LEFTPOS);
                        Thread.sleep(500);
                        hitter.setPosition(CENTERPOS);
                        Thread.sleep(500);
                        break;
                }
            }
            arm.setPosition(0.07);
        }
        return false;
    }
}
