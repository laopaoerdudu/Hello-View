package com.rko.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout

/** 在绘制过程中，每个 View 和 ViewGroup 都会先调用 onDraw() 方法来绘制主体，再调用 dispatchDraw() 方法来绘制子 View。 */
class GrayLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val mPaint = Paint()

    init {
        mPaint.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
            setSaturation(0f) // 饱和度 API
        })
    }

    override fun draw(canvas: Canvas?) {
        canvas?.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)
        super.draw(canvas)
        canvas?.restore()
    }

    /** https://rengwuxian.com/ui-1-5/ */
    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)
        super.dispatchDraw(canvas)
        canvas?.restore()
    }
}