package com.rko

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View

class MyAttrView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyStyleable)
        val count = typedArray.indexCount
        for (i in 0 until count) {
            when (val attrName = typedArray.getIndex(i)) {
                R.styleable.MyStyleable_attr_str -> {
                    Log.i("WWE", "attr_str value -> ${typedArray.getString(attrName)}")
                }
                R.styleable.MyStyleable_attr_bool -> {
                    Log.i("WWE", "attr_bool value -> ${typedArray.getBoolean(attrName, false)}")
                }
                R.styleable.MyStyleable_attr_int -> {
                    Log.i("WWE", "attr_int value -> ${typedArray.getInt(attrName, 0)}")
                }
                R.styleable.MyStyleable_attr_ref -> {
                    Log.i("WWE", "attr_ref value -> ${typedArray.getDimension(attrName, 0f)}")
                }
            }
        }
        typedArray.recycle()
    }
}