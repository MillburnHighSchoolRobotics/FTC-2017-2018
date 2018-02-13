package virtualRobot.logicThreads.testing;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Arrays;

import virtualRobot.LogicThread;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.utils.BetterLog;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by david on 2/12/18.
 */

public class AlignmentTestLogic extends LogicThread {
    private int width, height;
    static {
        if (!OpenCVLoader.initDebug()) {
            BetterLog.d("OpenCV", "OpenCV not loaded");
        } else {
            BetterLog.d("OpenCV", "OpenCV loaded");
        }
    }

    private VuforiaLocalizerImplSubclass vuforiaInstance;

    final int hue = 86; //107
    final int sat = 120; //143
    final int val = 12; //84
    final int del = 150; //128
    final double tolerance = 0.4;

    @Override
    protected void realRun() throws InterruptedException {
        robot.initCTelemetry();
        vuforiaInstance = GlobalUtils.vuforiaInstance;
        width = vuforiaInstance.rgb.getWidth();
        height = vuforiaInstance.rgb.getHeight();
        int target = (int)(Math.random() * 3);
        robot.addToTelemetry("Target", target);
        //Start CV
        while (true) {
            Mat img = new Mat();
            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
            Utils.bitmapToMat(bm, img);
            Imgproc.resize(img, img, new Size(0,0), 0.3, 0.3, Imgproc.INTER_LINEAR);
            Mat hsv = new Mat();
            Imgproc.GaussianBlur(img, hsv, new Size(5, 5), 0);
            Imgproc.cvtColor(hsv, hsv, Imgproc.COLOR_RGB2HSV);
            Mat inrange = new Mat();
            Core.inRange(hsv, new Scalar(hue, sat, val), new Scalar(hue + del, sat + del, val + del), inrange);
            try {
                robot.getCTelemetry().sendImage("InRange", inrange).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Mat lines = new Mat();
            Imgproc.HoughLines(inrange, lines, 1, Math.PI/180, 200/3);
            Log.d("Lines", lines.toString());
            int positions[] = new int[4];
            int lineCount = 0;
//            Log.d("Columns", Integer.toString(lines.cols()));
            for (int i = 0; i < lines.rows() && lineCount < 4; i++) {
                double[] currentLine = lines.get(i, 0);
                if (currentLine[1] > tolerance && currentLine[1] < (Math.PI - tolerance)) continue;
                boolean interferes = false;
                for (int j = 0; j < lineCount; j++) {
                    int dist = (int)Math.abs(Math.abs(currentLine[0]) - positions[j]);
                    if (dist < 75/3) {
                        interferes = true;
                        break;
                    }
                }
                if (!interferes) {
                    positions[lineCount] = (int)Math.abs(currentLine[0]);
                    lineCount++;
                }
            }
            Arrays.sort(positions, 0, lineCount);
            int avgSpace = 0;
            int avgPos = 0;
            for (int i = 0; i < lineCount; i++) {
                avgPos += positions[i];
                if (i < lineCount - 1) avgSpace += positions[i + 1] - positions[i];
            }
            if (lineCount > 0) avgPos /= lineCount;
            else avgPos = 0;
            if (lineCount > 1) avgSpace /= (lineCount - 1);
            else avgSpace = 0;
            Mat hough = new Mat();
            Imgproc.cvtColor(img, hough, Imgproc.COLOR_RGB2BGR);
            for (int i = 0; i < lineCount; i++) {
                double rho = Math.abs(positions[i]), theta = 0; //lmao
//            cout << rho << "\t";
//            if (i != lineCount - 1) cout << positions[i + 1] - positions[i] << '\t';
//            if (i != lineCount - 1) avgSpace += (positions[i + 1] - positions[i]);
                Point pt1 = new Point(), pt2 = new Point();
                double a = Math.cos(theta), b = Math.sin(theta);
                double x0 = a * rho, y0 = b * rho;
                pt1.x = (int)(x0 + 2000 * (-b));
                pt1.y = (int)(y0 + 2000 * (a));
                pt2.x = (int)(x0 - 1000 * (-b));
                pt2.y = (int)(y0 - 1000 * (a));
                //Target lines are green, others are red;
                Imgproc.line(hough, pt1, pt2, new Scalar(0, i == target || i == target + 1 ? 255 : 0, i == target || i == target + 1 ? 0 : 255), 3, Core.LINE_AA, 0);
            }
            try {
                robot.getCTelemetry().sendImage("Hough", hough).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            robot.addToTelemetry("Positions", Arrays.toString(positions));
            if (avgPos != 0 && avgSpace != 0 && target + 1 <= lineCount - 1) {
                int left, right;
                left = positions[target];
                right = positions[target + 1];
                int avg = (left + right) / 2;
                robot.addToTelemetry("Offset", avg - img.cols() / 2);
            }
        }
    }
}
