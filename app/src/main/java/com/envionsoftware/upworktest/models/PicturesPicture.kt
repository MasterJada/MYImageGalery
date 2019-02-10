package com.envionsoftware.upworktest.models


import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.envionsoftware.upworktest.R
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

open class PicturesPicture: RealmObject(), IPictureModel {
    @PrimaryKey
    var imageUri: String? = null
    var date: Date? = Date()
    @LinkingObjects("pictures")
     val album: RealmResults<AlbumModel>? =null

    fun getDate(): String {
       return SimpleDateFormat("dd MMM", Locale.getDefault()).format(date)
    }

    override fun getViewId(): Int {
        return R.layout.pictures_image
    }
    override fun fillItem(v: View) {
        val image = v.findViewById<ImageView>(R.id.iv_picture)
       if(!imageUri.isNullOrBlank()) {
           Glide.with(v).load(imageUri).into(image)
        }
    }
    override fun collSpan(): Int {
        return 1
    }
}