package com.rko.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class FlowLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val viewPositionList = mutableListOf<PositionModel>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 将所有的子 View 进行测量
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        // 获取 MyFlowLayout 的相关测量策略
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecModel = MeasureSpec.getMode(heightMeasureSpec)
        var heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        val count = childCount
        var lineWidth = 0 // 每一行的宽度

        // 最终确定下来的每一行宽高
        var resultWidth = 0
        var resultHeight = 0

        for (i in 0 until count) {
            val childView = getChildAt(i)
            val childViewLayoutParams = childView.layoutParams as? MarginLayoutParams
            val childViewUsedWidth = childViewLayoutParams?.let {
                childView.measuredWidth + it.leftMargin + it.rightMargin
            } ?: childView.measuredWidth
            val childViewUsedHeight = childViewLayoutParams?.let {
                childView.measuredHeight + it.topMargin + it.bottomMargin
            } ?: childView.measuredHeight

            if (lineWidth + childViewUsedWidth < widthSpecSize) {
                lineWidth += childViewUsedWidth
                resultHeight = resultHeight.coerceAtLeast(childViewUsedHeight)
                resultWidth = lineWidth.coerceAtLeast(resultWidth)
            } else {
                resultWidth = lineWidth.coerceAtLeast(resultWidth)
                resultHeight += childViewUsedHeight
                lineWidth = childViewUsedWidth
            }

            childViewLayoutParams?.let {
                viewPositionList.add(
                    i, PositionModel(
                        left = lineWidth - (childViewUsedWidth + it.leftMargin),
                        top = resultHeight - (childViewUsedHeight + it.topMargin),
                        right = lineWidth - it.rightMargin,
                        bottom = resultHeight - it.bottomMargin
                    )
                )
            } ?: run {
                viewPositionList.add(
                    i, PositionModel(
                        left = lineWidth - childViewUsedWidth,
                        top = resultHeight - childViewUsedHeight,
                        right = lineWidth,
                        bottom = resultHeight
                    )
                )
            }
        }

        val measuredWidth = if (widthSpecMode == MeasureSpec.EXACTLY) {
            widthSpecSize
        } else {
            resultWidth
        }
        val measuredHeight = if (heightSpecModel == MeasureSpec.EXACTLY) {
            heightSpecSize
        } else {
            resultHeight
        }
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        for (i in 0 until count) {
            getChildAt(i).layout(
                viewPositionList[i].left,
                viewPositionList[i].top,
                viewPositionList[i].right,
                viewPositionList[i].bottom
            )
        }
    }

    data class PositionModel(
        val left: Int,
        val top: Int,
        val right: Int,
        val bottom: Int
    )
}