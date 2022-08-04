package com.rko.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.rko.R

class BannerImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var ratio: Float = 0.1f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerImageView)
        ratio = typedArray.getFloat(R.styleable.BannerImageView_ratio, 0.0f)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i("WWE", "ratio: $ratio")
        setMeasuredDimension(measuredWidth, (measuredWidth * ratio).toInt())
    }
}