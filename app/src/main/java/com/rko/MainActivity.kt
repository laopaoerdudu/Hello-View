package com.rko

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val container: LinearLayout by lazy {
        findViewById(R.id.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        attachToRootIsTrue()
    }

    private fun inflateViewWithoutRoot() {
        container.addView(
            LayoutInflater
                .from(this)
                .inflate(R.layout.button_layout, null) // Note that the root here is null
        )
    }

    private fun inflateViewWithRoot() {
        container.addView(
            LayoutInflater
                .from(this)
                .inflate(R.layout.button_layout, container, false) // Note that the root here is container
        )
    }

    /**  Caused by: java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first. */
    private fun inflateViewWithRootAndAttachToRootIsTrue() {
        container.addView(
            LayoutInflater
                .from(this)
                .inflate(R.layout.button_layout, container, true) // Note that the attachToRoot here is true
        )
    }

    /**
     * 如果将第三个参数改成 true，就表示 `button_layout.xml` 布局已经自动被添加到 `container` 当中了，
     * 此时再去 call 一遍 `addView()` 方法，发现 `button_layout.xml` 已经有父布局了，自然就会抛出上面的异常。
     */
    private fun attachToRootIsTrue() {
        LayoutInflater
            .from(this)
            .inflate(R.layout.button_layout, container, true) // Note that the attachToRoot here is true
    }
}