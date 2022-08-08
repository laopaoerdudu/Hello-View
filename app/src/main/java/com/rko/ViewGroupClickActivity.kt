package com.rko

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rko.widget.MyLayout

class ViewGroupClickActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewgroup_poc_layout)

//        findViewById<MyLayout>(R.id.container).setOnTouchListener { v, event ->
//            Log.i("WWE", "MyLayout on touch")
//            false
//        }

        findViewById<Button>(R.id.button1).setOnClickListener {
            Log.i("WWE", "button1 onClick")
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            Log.i("WWE", "button2 onClick")
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("WWE", "Activity -> onTouchEvent -> ${event?.action}")
        return super.onTouchEvent(event)
    }
}