package com.envionsoftware.upworktest.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.envionsoftware.upworktest.R
import com.envionsoftware.upworktest.adapters.PictureAdapter
import com.envionsoftware.upworktest.models.AlbumModel
import com.envionsoftware.upworktest.models.IPictureModel
import com.envionsoftware.upworktest.models.PicturesHeader
import com.envionsoftware.upworktest.models.PicturesPicture
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_pictures.*

class PicturesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val album = arguments?.getString("albumName")
        if(album == null)
            result = realm.where<PicturesPicture>().findAll()
        else
            result = realm.where<PicturesPicture>().equalTo("album.name", album).findAll()

        return inflater.inflate(R.layout.fragment_pictures, container, false)
    }

    val realm = Realm.getDefaultInstance()

    private lateinit var result: RealmResults<PicturesPicture>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context, 6)
        val adapter = PictureAdapter()




        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return adapter.items[position].collSpan()
            }

        }
        rv_pictures.layoutManager = layoutManager
        rv_pictures.adapter = adapter


        adapter.items = transform(result)

        result?.addChangeListener { res ->
            adapter.items = transform(res)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun transform(input: List<PicturesPicture>?): List<IPictureModel> {
        input ?: return emptyList()
        val output = ArrayList<IPictureModel>()
        input.groupBy {
            it.getDate()
        }
            .forEach {
                output.add(PicturesHeader(it.key))
                output.addAll(it.value)
            }
        return output
    }

    companion object {

        fun getInstance(albumName: String? = null): PicturesFragment{
            val args = Bundle()
            if(albumName != null)
            args.putString("albumName", albumName)
            val fragment = PicturesFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
