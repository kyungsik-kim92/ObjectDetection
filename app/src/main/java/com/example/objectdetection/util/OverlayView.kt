/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.objectdetection

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.objectdetection.R
import java.util.LinkedList
import kotlin.math.max
import org.tensorflow.lite.task.vision.detector.Detection

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results: List<Detection> = LinkedList<Detection>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var scaleFactor: Float = 1f

    private var bounds = Rect()

    private lateinit var itemTouch: (String) -> Unit

    init {
        initPaints()
    }

    fun clear() {
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.bounding_box_color)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        for (result in results) {

            if (result.categories[0].score > 0.5){

                val drawableRect = getDrawableRect(result)

                canvas.drawRect(getDrawableRect(result), boxPaint)

                // Create text to display alongside detected objects
                val drawableText =
                    result.categories[0].label

                // Draw rect behind display text
                textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)

                val textWidth = bounds.width()
                val textHeight = bounds.height()

                canvas.drawRect(
                    drawableRect.left,
                    drawableRect.top,
                    drawableRect.left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                    drawableRect.top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                    textBackgroundPaint
                )

                // Draw text for detected object
                canvas.drawText(
                    drawableText,
                    drawableRect.left,
                    drawableRect.top + bounds.height(),
                    textPaint
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val toTouchItem = results.find { item ->
                getDrawableRect(item).contains(event.x, event.y)
            }
            toTouchItem?.let {
                itemTouch.invoke(it.categories[0].label)
            }
        }
        return super.onTouchEvent(event)
    }

    fun setResults(
        detectionResults: MutableList<Detection>,
        imageHeight: Int,
        imageWidth: Int,
    ) {
        results = detectionResults

        // PreviewView is in FILL_START mode. So we need to scale up the bounding box to match with
        // the size that the captured images will be displayed.
        scaleFactor = max(width * 1f / imageWidth, height * 1f / imageHeight)
    }

    fun getTouchItem(itemTouchListener: (String) -> Unit) {
        itemTouch = itemTouchListener
    }

    // Draw bounding box around detected objects
    private fun getDrawableRect(item: Detection): RectF {
        val boundingBox = item.boundingBox
        val top = boundingBox.top * scaleFactor
        val bottom = boundingBox.bottom * scaleFactor
        val left = boundingBox.left * scaleFactor
        val right = boundingBox.right * scaleFactor
        // Draw bounding box around detected objects
        return RectF(left, top, right, bottom)
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}
