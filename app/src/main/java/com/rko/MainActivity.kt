package com.rko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.rko.widget.BannerImageView

class MainActivity : AppCompatActivity() {
    private lateinit var image: BannerImageView
    private lateinit var btnPOC: Button
    private lateinit var container: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container = findViewById(R.id.container)
        image = findViewById(R.id.ivBannerIcon)
        btnPOC = findViewById(R.id.btnPOC)

        btnPOC.setOnClickListener {
            Log.d("WWE", "onClick execute")
        }

        btnPOC.setOnTouchListener { _, event ->
            Log.i("WWE", "Button onTouch action -> ${event.action}")
            true
        }

        /**
         * ImageView 和按钮不同，它是默认不可点击的
         * ImageView 的 onTouch 方法里返回 true，这样可以保证 ACTION_DOWN 之后的其它 action 都能得到执行
         */
        image.setOnTouchListener { _, event ->
            Log.d("WWE", "BannerImageView onTouch action -> ${event.action}")
            true
        }

        container.setOnTouchListener { _, event ->
            Log.d("WWE", "ConstraintLayout onTouch action -> ${event.action}")
            false
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // Log.i("WWE", "Width: ${image.width}, Height: ${image.height}")
    }
}