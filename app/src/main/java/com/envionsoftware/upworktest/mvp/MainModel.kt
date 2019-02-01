package com.envionsoftware.upworktest.mvp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.wifi.WifiManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.EditText
import android.widget.Toast
import com.envionsoftware.upworktest.models.AlbumModel
import com.envionsoftware.upworktest.models.PicturesPicture
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import java.lang.Exception
import java.util.*

class MainModel {
    var wifiName: String? = null
    var city: String? = null
    private var context: Activity? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun setupContext(activity: Activity) {
        context = activity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        setupWifi()
        setupCity()
    }

    fun setupWifi() {
        context ?: return
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_WIFI_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context!!,
                arrayOf(Manifest.permission.ACCESS_WIFI_STATE),
                203
            )
        } else {
            val wifiMgr = context?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiMgr.connectionInfo
            wifiName = wifiInfo.ssid.replace("\"", "")
        }
    }

    fun setupCity() {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                202
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                it?.let { location ->
                    val coder = Geocoder(context)
                    val result = coder.getFromLocation(location.latitude, location.longitude, 1)
                    if (result.size > 0)
                        city = result[0].locality
                }
            }

        }
    }

    fun savePhoto(photo: PicturesPicture, album: AlbumModel? = null){
        Realm.getDefaultInstance().use {
            val lastAlbum = album ?: AlbumModel.getLastAlbum()
            if (lastAlbum != null) {
                it.beginTransaction()
                lastAlbum.pictures.add(photo)
                lastAlbum.lastUpdate = Date()
                it.commitTransaction()
            } else {
                it.beginTransaction()
                it.copyToRealm(photo)
                it.commitTransaction()
            }
        }
    }

    fun createAlbum(title: String, parentAlbum: AlbumModel? = null){
        val albumModel = AlbumModel()
        albumModel.lastUpdate = Date()
        albumModel.name = title
        Realm.getDefaultInstance().use {
            try {
                if(parentAlbum != null){
                    it.beginTransaction()
                    parentAlbum.subAlbums.add(albumModel)
                    it.commitTransaction()
                }else {
                    it.beginTransaction()
                    it.copyToRealm(albumModel)
                    it.commitTransaction()
                }
            }catch (e: RealmPrimaryKeyConstraintException) {
                Toast.makeText(context, "Album already exist", Toast.LENGTH_SHORT).show()
            }

        }
    }
}