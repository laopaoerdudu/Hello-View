package com.rko

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity

// It is better to use reflection to modify mFactory2 to prevent flashback.
// Please refer to https://github.com/ximsfei/Android-skin-support
class TestActivity : AppCompatActivity() {
    // It used to load skin Views
    private val skinViews = mutableListOf<SkinSupportable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflater.from(this).factory2 = object : LayoutInflater.Factory2 {
            override fun onCreateView(
                parent: View?,
                name: String,
                context: Context,
                attrs: AttributeSet
            ): View? {
                // If the widget is button then use SkinButton instead
                if ("Button" == name) {
                    val view = SkinButton(context, attrs)
                    skinViews.add(view)
                    return view
                }
                return null
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return onCreateView(null, name, context, attrs)
            }
        }
        super.onCreate(savedInstanceState)
    }
}