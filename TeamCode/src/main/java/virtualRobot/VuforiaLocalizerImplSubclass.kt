package virtualRobot

import com.qualcomm.robotcore.util.RobotLog
import com.vuforia.Frame
import com.vuforia.Image
import com.vuforia.PIXEL_FORMAT
import com.vuforia.State
import com.vuforia.Vuforia

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl


/**
 * Created by mehme_000 on 10/15/2016.
 * Memes, nothing but pure memes.
 */

class VuforiaLocalizerImplSubclass(parameters: VuforiaLocalizer.Parameters) : VuforiaLocalizerImpl(parameters) {

    var rgb: Image

    internal inner class CloseableFrame(other: Frame)// clone the frame so we can be useful beyond callback
        : Frame(other) {
        fun close() {
            super.delete()
        }
    }


    inner class VuforiaCallbackSubclass : VuforiaLocalizerImpl.VuforiaCallback() {

        @Synchronized override fun Vuforia_onUpdate(state: State) {
            super.Vuforia_onUpdate(state)
            // We wish to accomplish two things: (a) get a clone of the Frame so we can use
            // it beyond the callback, and (b) get a variant that will allow us to proactively
            // reduce memory pressure rather than relying on the garbage collector (which here
            // has been observed to interact poorly with the image data which is allocated on a
            // non-garbage-collected heap). Note that both of this concerns are independent of
            // how the Frame is obtained in the first place.
            val frame = CloseableFrame(state.frame)
            RobotLog.vv(VuforiaLocalizerImpl.TAG, "received Vuforia frame#=%d", frame.index)

            //Important Stuff:
            val num = frame.numImages

            for (i in (num - 1).toInt() downTo 0) {
                if (frame.getImage(i).format == PIXEL_FORMAT.RGB565) {
                    rgb = frame.getImage(i)
                    break
                }
            }
            //End important stuff.

            frame.close()
        }
    }

    init {
        stopAR()
        clearGlSurface()

        this.vuforiaCallback = VuforiaCallbackSubclass()
        startAR()

        //Set pixel format. This will be either RGB565 or RGB888 depedning on the phone used
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true)
    }

    fun clearGlSurface() {
        if (this.glSurfaceParent != null) {
            appUtil.synchronousRunOnUiThread {
                glSurfaceParent.removeAllViews()
                glSurfaceParent.overlay.clear()
                glSurface = null
            }
        }
    }
}
