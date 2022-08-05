package com.rko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rko.widget.BannerImageView

class MainActivity : AppCompatActivity() {
    private lateinit var image: BannerImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.ivBannerIcon)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // Log.i("WWE", "Width: ${image.width}, Height: ${image.height}")
    }
}