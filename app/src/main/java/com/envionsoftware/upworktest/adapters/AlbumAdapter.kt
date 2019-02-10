package com.envionsoftware.upworktest.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.envionsoftware.upworktest.R
import com.envionsoftware.upworktest.models.AlbumModel

class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.AlbumVH>() {

    var items: List<AlbumModel> = ArrayList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var cameraClick: ((item: AlbumModel)-> Unit)? = null
    var plusClick: ((item: AlbumModel)->Unit)? =null
    var itemClickListener: ((item: AlbumModel) -> Unit)? = null


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AlbumVH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.album_layout, p0, false)
        return AlbumVH(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(vh: AlbumVH, p1: Int) {

        val item = items[p1]
        item.fillItem(vh.itemView, cameraClick, plusClick, itemClickListener)
    }


    class AlbumVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}