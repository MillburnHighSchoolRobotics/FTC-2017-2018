package virtualRobot.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.VuforiaLocalizerImplSubclass;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.*;

/**
 * Created by ethan on 9/20/17.
 */

public class GlobalUtils {
    private static Activity currentActivity = null;
    public static VuforiaLocalizerImplSubclass vuforiaInstance = null;
    public static RelicRecoveryVuMark forcedVumark = LEFT;
    private static int vumarkNum = 0;

    public static Activity getCurrentActivity() {
        if (currentActivity == null)
            throw new ExceptionInInitializerError("Failed to set current activity");
        return currentActivity;
    }

    public static void setCurrentActivity(@NonNull Activity activity) {
        currentActivity = activity;
    }

    public static void incrementVumark() {
        vumarkNum = (vumarkNum + 1) % 3;
        forcedVumark = new RelicRecoveryVuMark[] {LEFT, CENTER, RIGHT}[vumarkNum];
    }
}
