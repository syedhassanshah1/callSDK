
package org.linphone.activities.call

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlin.math.max
import kotlin.math.min
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.core.Call

class VideoZoomHelper(context: Context, private var videoDisplayView: View) : GestureDetector.SimpleOnGestureListener() {
    private var scaleDetector: ScaleGestureDetector

    private var zoomFactor = 1f
    private var zoomCenterX = 0f
    private var zoomCenterY = 0f

    init {
        val gestureDetector = GestureDetector(context, this)

        scaleDetector = ScaleGestureDetector(context, object :
            ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                zoomFactor *= detector.scaleFactor
                // Don't let the object get too small or too large.
                // Zoom to make the video fill the screen vertically
                val portraitZoomFactor = videoDisplayView.height.toFloat() / (3 * videoDisplayView.width / 4)
                // Zoom to make the video fill the screen horizontally
                val landscapeZoomFactor = videoDisplayView.width.toFloat() / (3 * videoDisplayView.height / 4)
                zoomFactor = max(0.1f, min(zoomFactor, max(portraitZoomFactor, landscapeZoomFactor)))

                val currentCall: Call? = coreContext.core.currentCall
                if (currentCall != null) {
                    currentCall.zoom(zoomFactor, zoomCenterX, zoomCenterY)
                    return true
                }

                return false
            }
        })

        videoDisplayView.setOnTouchListener { _, event ->
            val currentZoomFactor = zoomFactor
            scaleDetector.onTouchEvent(event)

            if (currentZoomFactor != zoomFactor) {
                // We did scale, prevent touch event from going further
                return@setOnTouchListener true
            }

            // If true, gesture detected, prevent touch event from going further
            // Otherwise it seems we didn't use event,
            // allow it to be dispatched somewhere else
            gestureDetector.onTouchEvent(event)
        }
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        val currentCall: Call? = coreContext.core.currentCall
        if (currentCall != null) {
            if (zoomFactor > 1) {
                // Video is zoomed, slide is used to change center of zoom
                if (distanceX > 0 && zoomCenterX < 1) {
                    zoomCenterX += 0.01f
                } else if (distanceX < 0 && zoomCenterX > 0) {
                    zoomCenterX -= 0.01f
                }

                if (distanceY < 0 && zoomCenterY < 1) {
                    zoomCenterY += 0.01f
                } else if (distanceY > 0 && zoomCenterY > 0) {
                    zoomCenterY -= 0.01f
                }

                if (zoomCenterX > 1) zoomCenterX = 1f
                if (zoomCenterX < 0) zoomCenterX = 0f
                if (zoomCenterY > 1) zoomCenterY = 1f
                if (zoomCenterY < 0) zoomCenterY = 0f

                currentCall.zoom(zoomFactor, zoomCenterX, zoomCenterY)
                return true
            }
        }

        return false
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        val currentCall: Call? = coreContext.core.currentCall
        if (currentCall != null) {
            if (zoomFactor == 1f) {
                // Zoom to make the video fill the screen vertically
                val portraitZoomFactor = videoDisplayView.height.toFloat() / (3 * videoDisplayView.width / 4)
                // Zoom to make the video fill the screen horizontally
                val landscapeZoomFactor = videoDisplayView.width.toFloat() / (3 * videoDisplayView.height / 4)
                zoomFactor = max(portraitZoomFactor, landscapeZoomFactor)
            } else {
                resetZoom()
            }

            currentCall.zoom(zoomFactor, zoomCenterX, zoomCenterY)
            return true
        }

        return false
    }

    private fun resetZoom() {
        zoomFactor = 1f
        zoomCenterY = 0.5f
        zoomCenterX = zoomCenterY
    }
}
