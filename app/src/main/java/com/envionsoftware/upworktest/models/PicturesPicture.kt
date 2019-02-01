package com.envionsoftware.upworktest.models


import android.view.View
import android.widget.ImageView
import com.envionsoftware.upworktest.R
import com.squareup.picasso.Picasso
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

open class PicturesPicture: RealmObject(), IPictureModel {
    @PrimaryKey
    var imageUri: String? = null
    var date: Date? = Date()

    fun getDate(): String {
       return SimpleDateFormat("dd MMM", Locale.getDefault()).format(date)
    }

    override fun getViewId(): Int {
        return R.layout.pictures_image
    }
    override fun fillItem(v: View) {
        val image = v.findViewById<ImageView>(R.id.iv_picture)
       if(!imageUri.isNullOrBlank()) {
           Picasso.get().load(File(imageUri)).resize(100,100).centerCrop().into(image)
        }
    }
    override fun collSpan(): Int {
        return 1
    }
}