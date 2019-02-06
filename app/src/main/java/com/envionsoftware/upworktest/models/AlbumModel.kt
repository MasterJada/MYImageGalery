package com.envionsoftware.upworktest.models


import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import java.util.*

open class AlbumModel: RealmObject() {
    @PrimaryKey
    var name: String = ""
    var subAlbums = RealmList<AlbumModel>()
    var pictures = RealmList<PicturesPicture>()
    var lastUpdate: Date? = null


    companion object {
        fun getLastAlbum(): AlbumModel?{
            Realm.getDefaultInstance().use {
                return   it.where<AlbumModel>().sort("lastUpdate").findAll().lastOrNull()
            }
        }

    }

}