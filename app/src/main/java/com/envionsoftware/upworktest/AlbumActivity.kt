package com.envionsoftware.upworktest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.envionsoftware.upworktest.fragments.PicturesFragment

class AlbumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
       val album =  intent?.extras?.getString("album")
        title = album
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, PicturesFragment.getInstance(album))
            .commit()
    }
}
