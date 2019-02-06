package com.envionsoftware.upworktest.mvp

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.EditText
import com.envionsoftware.upworktest.MainActivity
import com.envionsoftware.upworktest.TakePhotoManager
import com.envionsoftware.upworktest.models.AlbumModel
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.realm.Realm
import java.util.*

class MainPresenter(val model: MainModel): SearchView.OnQueryTextListener, View.OnFocusChangeListener  {

    val queryPublisher = PublishSubject.create<String>()
    private val disposables = ArrayList<Disposable>()
    private val photoManager = TakePhotoManager()

    private var view: MainActivity? = null
    fun attachView(activity: MainActivity){
        view = activity
        model.setupContext(activity)
    }

    fun detachView(){
        view = null
    }

    fun viewReady(){
        view?.let { model.setupContext(it) }
    }


    fun takePhoto(album: AlbumModel? = null) {
        view ?: return
        if (ContextCompat.checkSelfPermission(view!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                view!!,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                201
            )
        } else {
            val images = view?.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return
            disposables.add(photoManager.takePicture(view!!, images).subscribe { picture ->
                model.savePhoto(picture, album)
                disposables.forEach { it.dispose() }
            })

        }
    }

    fun addAlbum(album: AlbumModel? = null) {
        view ?: return
        val edtitText = EditText(view)
        val text = model.city ?: model.wifiName
        edtitText.setText(text ?: "")
        AlertDialog.Builder(view!!)
            .setTitle("Album name")
            .setView(edtitText)
            .setPositiveButton("Ok") { dialog, p1 ->
                if (edtitText.text.isNotEmpty()) {
                    model.createAlbum(edtitText.text.toString(), album)
                }
                dialog.dismiss()
            }.setNegativeButton("cancel") { dialog, which ->
                dialog.dismiss()
            }.show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        queryPublisher.onNext(query ?: "")
        queryPublisher.onComplete()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        queryPublisher.onNext(newText ?: "")
        return true
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if(hasFocus){
            view?.changeFragment(1)
        }
    }

    fun onRequestPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 201 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePhoto()
        }
        if (requestCode == 202 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            model.setupCity()
        }
        if (requestCode == 203 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            model.setupWifi()
        }
        if (requestCode == 204 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            model.setupWifiFromNetwork()
        }
    }
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        //TakePhotoManager.instance.activityResult(requestCode, resultCode, data)
        photoManager.activityResult(requestCode, resultCode, data)
    }
}