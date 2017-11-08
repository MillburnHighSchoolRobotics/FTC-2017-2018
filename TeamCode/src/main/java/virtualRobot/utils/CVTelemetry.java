package virtualRobot.utils;

import org.opencv.core.Mat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by david on 11/7/17.
 */

public interface CVTelemetry {
    @POST("/")
    Call<Void> sendImage(@Header("window-name") String windowName, @Body Mat img);
}
