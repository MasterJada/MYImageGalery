package com.envionsoftware.upworktest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.MotionEvent
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detailed.*

class DetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        intent?.extras?.getString("image").apply {
            Glide.with(this@DetailedActivity)
                .load(this)
                .into(iv_photo)
        } ?: finish()
        var rY = 0F
        iv_photo.setOnTouchListener { v, event ->
            val dif = event.rawY - rY
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    rY = event.rawY
                    true
                }
                MotionEvent.ACTION_UP -> {
                    print(dif)
                    if (Math.abs(dif) > 900) {
                        iv_photo.alpha = 1F
                        ActivityCompat.finishAfterTransition(this)
                    } else
                        iv_photo.animate().translationY(0F)
                            .alpha(1F)
                            .setDuration(100)
                            .start()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (Math.abs(dif) > 900) {
                        ActivityCompat.finishAfterTransition(this)
                    } else {
                        iv_photo.translationY = dif
                        iv_photo.alpha = 1 - Math.abs(dif) / 1000
                    }
                    true
                }
                else -> false
            }

        }
    }


}
