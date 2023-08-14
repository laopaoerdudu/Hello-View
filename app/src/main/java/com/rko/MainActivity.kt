package com.rko

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rko.widgets.GrayLinearLayout
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)

        // int drawableId = resources.getIdentifier("shark", "drawable", "com.skin.skin_pack_making");
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        try {
            if ("FrameLayout" == name) {
                val num = attrs.attributeCount
                (0 until num).forEach { i ->
                    val attributeName = attrs.getAttributeName(i)
                    val attributeValue = attrs.getAttributeValue(i)
                    if ("id" == attributeName) {
                        val resId = attributeValue.substring(1).toInt()
                        val resName = resources.getResourceName(resId)
                        if ("android:id/content" == resName) {
                            return GrayLinearLayout(context, attrs).apply {
                                background = window.decorView.background
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return super.onCreateView(parent, name, context, attrs)
    }
}