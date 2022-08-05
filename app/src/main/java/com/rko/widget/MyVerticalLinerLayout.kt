package com.rko.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup

class MyVerticalLinerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 将所有的子 View 进行测量
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        // MyFlowLayout 的相关测量策略
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecModel = MeasureSpec.getMode(heightMeasureSpec)
        var heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        when {
            widthSpecMode == MeasureSpec.AT_MOST && heightSpecModel == MeasureSpec.AT_MOST -> {
                Log.i("WWE", "#28 invoked")
                setMeasuredDimension(getMaxWidth(), getTotalHeight())
            }

            widthSpecMode == MeasureSpec.AT_MOST -> {
                Log.i("WWE", "#33 invoked")
                setMeasuredDimension(getMaxWidth(), heightSpecSize)
            }

            heightSpecModel == MeasureSpec.AT_MOST -> {
                Log.i("WWE", "#38 invoked")
                setMeasuredDimension(widthSpecSize, getTotalHeight())
            }

            else -> {
                Log.i("WWE", "#43 invoked")
                setMeasuredDimension(widthSpecSize, getTotalHeight())
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        var currentHeight = 0
        for (i in 0 until count) {
            val view = getChildAt(i)
            view.layout(
                left,
                currentHeight,
                left + view.measuredWidth,
                currentHeight + view.measuredHeight
            )
            currentHeight += view.measuredHeight
        }
    }

    private fun getMaxWidth(): Int {
        var maxWidth = 0
        val count = childCount
        for (i in 0 until count) {
            val width = getChildAt(i).measuredWidth
            if (maxWidth < width) {
                maxWidth = width
            }
        }
        return maxWidth
    }

    private fun getTotalHeight(): Int {
        var totalHeight = 0
        val count = childCount
        for (i in 0 until count) {
            totalHeight += getChildAt(i).measuredHeight
        }
        return totalHeight
    }
}