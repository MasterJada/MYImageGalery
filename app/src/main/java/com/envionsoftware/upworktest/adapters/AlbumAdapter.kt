package com.envionsoftware.upworktest.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AlbumVH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.album_layout, p0, false)
        return AlbumVH(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(p0: AlbumVH, p1: Int) {
        items[p1].fillView(p0)
        p0.camera.setOnClickListener {
            //items[p1].setAsLast()
            cameraClick?.invoke(items[p1])
        }
        p0.plus.setOnClickListener {
            plusClick?.invoke(items[p1])
        }
    }


    class AlbumVH(itemView: View) : RecyclerView.ViewHolder(itemView){
        val picture: ImageView = itemView.findViewById(R.id.iv_pic)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val camera: View = itemView.findViewById(R.id.ib_camera)
        val plus: View = itemView.findViewById(R.id.ib_plus)

    }
}