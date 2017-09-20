package virtualRobot.utils;

import virtualRobot.utils.BetterLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethan on 9/20/17.
 */

public class BetterLog {
    private static List<String> tags = new ArrayList<>();

    static {

    }

    public static void d(String tag, String msg){
        if (tags.contains(tag))
            BetterLog.d(tag, msg);
    }
}
