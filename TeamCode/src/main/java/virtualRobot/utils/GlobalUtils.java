package virtualRobot.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by ethan on 9/20/17.
 */

public class GlobalUtils {
    private static Activity currentActivity = null;

    public static Activity getCurrentActivity() {
        if (currentActivity == null)
            throw new ExceptionInInitializerError("Failed to set current activity");
        return currentActivity;
    }

    public static void setCurrentActivity(@NonNull Activity activity) {
        currentActivity = activity;
    }
}
