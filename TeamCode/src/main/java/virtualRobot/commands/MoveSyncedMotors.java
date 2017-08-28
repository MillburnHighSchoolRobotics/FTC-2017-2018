package virtualRobot.commands;

import virtualRobot.hardware.SyncedMotors;

/**
 * Created by ethachu19 on 9/24/2016.
 * Moves SynchronizedMotors
 */
public class MoveSyncedMotors extends Command {

	SyncedMotors synced;
    double power;
	public MoveSyncedMotors(SyncedMotors synced, double power) {
		this.synced = synced; this.power = power;
	}

    @Override
    protected int activate(String s) {
        switch(s) {
            case "BREAK":
                return BREAK;
            case "END":
                return END;
        }
        return NO_CHANGE;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        synced.setPower(power);
        return Thread.currentThread().isInterrupted();
    }
}