package com.envionsoftware.upworktest.models


import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.envionsoftware.upworktest.R
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import java.util.*

open class AlbumModel: RealmObject() {
    @PrimaryKey
    var name: String = ""
    var subAlbums = RealmList<AlbumModel>()
    var pictures = RealmList<PicturesPicture>()
    var lastUpdate: Date? = null
    @LinkingObjects("subAlbums")
    val parent: RealmResults<AlbumModel>? =null

    fun fillItem(
        v: View,
        cameraClick: ((item: AlbumModel) -> Unit)? = null,
        plusClick: ((item: AlbumModel) -> Unit)? = null,
        itemClickListener: ((item: AlbumModel) -> Unit)?
    ){
        val picture: ImageView = v.findViewById(R.id.iv_pic)
        val title: TextView = v.findViewById(R.id.tv_title)
        val camera: View = v.findViewById(R.id.ib_camera)
        val plus: View = v.findViewById(R.id.ib_plus)
        val subList: LinearLayout = v.findViewById(R.id.sub_list)

        picture.visibility = if(pictures.isNotEmpty()){
            pictures.lastOrNull()?.let {
                Glide.with(v).load(it.imageUri)
                    .into(picture)
            }
            View.VISIBLE
        }else {
         View.GONE
        }
        title.text =  "$name ${if(pictures.size > 0) "("+pictures.size+ ")" else "" }"
        camera.setOnClickListener { cameraClick?.invoke(this) }

        if(plusClick == null) plus.visibility = View.GONE
        plus.setOnClickListener { plusClick?.invoke(this) }
        v.setOnClickListener { itemClickListener?.invoke(this) }
        subList.removeAllViews()

        subAlbums.forEach {subAlbum ->
            val subView = LayoutInflater.from(v.context).inflate(R.layout.album_layout, subList, false)
            subList.addView(subView)
            subAlbum.fillItem(subView, cameraClick,null, itemClickListener )
            if(subAlbum == subAlbums.last()){
                subView.findViewById<View>(R.id.view).visibility = View.GONE
            }
        }
    }

    companion object {
        fun getLastAlbum(): AlbumModel?{
            Realm.getDefaultInstance().use {
                return   it.where<AlbumModel>().sort("lastUpdate").findAll().lastOrNull()
            }
        }

    }

}