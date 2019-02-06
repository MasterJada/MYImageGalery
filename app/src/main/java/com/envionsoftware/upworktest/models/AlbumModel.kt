package com.envionsoftware.upworktest.models

import android.view.View
import com.envionsoftware.upworktest.adapters.AlbumAdapter
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import java.io.File
import java.util.*

open class AlbumModel: RealmObject() {
    @PrimaryKey
    var name: String = ""
    var subAlbums = RealmList<AlbumModel>()
    var pictures = RealmList<PicturesPicture>()
    var lastUpdate: Date? = null

    fun fillView(vh: AlbumAdapter.AlbumVH){
        if(pictures.isNotEmpty())
        pictures.last()?.let {
            Picasso.get().load(File(it.imageUri)).resize(100,100).centerCrop().into(vh.picture)
        }
        else {
            vh.picture.visibility = View.GONE
        }
        vh.title.text = "$name (${pictures.size})"

    }


    companion object {
        fun getLastAlbum(): AlbumModel?{
            Realm.getDefaultInstance().use {
                return   it.where<AlbumModel>().sort("lastUpdate").findAll().lastOrNull()
            }
        }

    }

}