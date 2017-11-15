package virtualRobot.commands;

import org.firstinspires.ftc.teamcode.UpdateThread;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import virtualRobot.SallyJoeBot;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by ethan on 9/22/17.
 */

public class EthanClass extends Command {
    VuforiaLocalizerImplSubclass vuforiaInstance;
    AutonomousLogicThread currentThread;

    private int width, height;
    private int kSize = 11;
    Scalar redUpper = new Scalar(50, 50, 200);
    Scalar redLower = new Scalar(0, 0, 50);
    Scalar blueUpper = new Scalar(50, 0, 0);
    Scalar blueLower = new Scalar(200, 50, 50);
    private static final SallyJoeBot robot = Command.ROBOT;

    public EthanClass() {
        vuforiaInstance = UpdateThread.vuforiaInstance;
        width = vuforiaInstance.rgb.getBufferWidth();
        height = vuforiaInstance.rgb.getHeight();
        robot.initCVTelemetry();
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        if (parentThread instanceof AutonomousLogicThread)
            currentThread = ((AutonomousLogicThread) parentThread);
        else
            throw new RuntimeException("Was not called in Autonomous?");
        Mat img = new Mat(height, width, CvType.CV_8UC1);
        img.put(0,0,vuforiaInstance.rgb.getPixels().array());
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGR);
        try {
            robot.sendCVTelemetry("Image", img).execute();
        } catch (IOException e) {
            robot.addToTelemetry("Image", e.getMessage());
        }
        Mat blur = new Mat();
        Imgproc.GaussianBlur(img, blur, new Size(kSize,kSize), 0);
        Mat red = new Mat();
        Core.inRange(img, redLower, redUpper, red);
        try {
            robot.sendCVTelemetry("Red", red).execute();
        } catch (IOException e) {
            robot.addToTelemetry("RedImage", e.getMessage());
        }
        List<MatOfPoint> contours = new LinkedList<>();
        Imgproc.findContours(red.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
                return (int) Math.signum(Imgproc.contourArea(matOfPoint) - Imgproc.contourArea(t1));
            }
        });
        if (contours.size() <= 0) {
            currentThread.redIsLeft.set(false);
            return Thread.currentThread().isInterrupted();
        }
        Point centerRed = new Point();
        float[] radiusRed = new float[1];
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(contours.size() - 1).toArray()),centerRed,radiusRed);
        Mat blue = new Mat();
        Core.inRange(img, blueLower, blueUpper, blue);
        try {
            robot.sendCVTelemetry("Blue", img).execute();
        } catch (IOException e) {
            robot.addToTelemetry("BlueImage", e.getMessage());
        }
        contours = new LinkedList<>();
        Imgproc.findContours(blue.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
                return (int) Math.signum(Imgproc.contourArea(matOfPoint) - Imgproc.contourArea(t1));
            }
        });
        if (contours.size() <= 0) {
            currentThread.redIsLeft.set(false);
            return Thread.currentThread().isInterrupted();
        }
        Point centerBlue = new Point();
        float[] radiusBlue = new float[1];
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(contours.size() - 1).toArray()),centerBlue,radiusBlue);
        currentThread.redIsLeft.set(centerRed.x < centerBlue.x);
        return Thread.currentThread().isInterrupted();
    }
}
