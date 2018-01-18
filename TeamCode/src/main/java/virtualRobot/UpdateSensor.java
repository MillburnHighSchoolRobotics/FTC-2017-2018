package virtualRobot;

import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by david on 1/17/18.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateSensor {
    String name();
    boolean enabled() default true;
    Class<? extends HardwareDevice> type();
}