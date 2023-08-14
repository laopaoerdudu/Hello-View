package com.rko

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var count = 10000
        findViewById<Button>(R.id.gcBtn).setOnClickListener {
            for (i in 0 until 1000) {
                val view = MyView(this, count)
                count++
            }
        }
    }
}