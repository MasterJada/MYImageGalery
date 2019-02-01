package com.envionsoftware.upworktest.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.envionsoftware.upworktest.MainActivity

import com.envionsoftware.upworktest.R
import com.envionsoftware.upworktest.adapters.AlbumAdapter
import com.envionsoftware.upworktest.models.AlbumModel
import io.reactivex.disposables.Disposable
import io.realm.Case
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_blank.*



class AlbumsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    private val realm = Realm.getDefaultInstance()
    private val albums = realm.where<AlbumModel>().sort("name", Sort.ASCENDING).findAll()
    private val disposables = ArrayList<Disposable>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         val adapter = AlbumAdapter()
        rv_albums.adapter = adapter
        rv_albums.layoutManager = LinearLayoutManager(context)
        adapter.items = albums
        adapter.cameraClick = {item ->
            (activity as? MainActivity)?.getPresenter()?.takePhoto(item)
        }
        adapter.plusClick = {item ->
            (activity as? MainActivity)?.getPresenter()?.addAlbum(item)
        }
        albums.addChangeListener { res ->
            adapter.items = res
        }
       disposables.add ((activity as MainActivity).getPresenter().queryPublisher.distinctUntilChanged()
            .subscribe({query ->
                adapter.items =  albums.where().contains("name", query,Case.INSENSITIVE).findAll()
            }, {
                it.printStackTrace()
            }, {
                adapter.items = albums
                disposables.forEach { it.dispose() }
            }))
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


}
