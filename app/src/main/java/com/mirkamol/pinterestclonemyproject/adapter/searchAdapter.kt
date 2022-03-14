package com.mirkamol.pinterestclonemyproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mirkamol.pinterestclonemyproject.R
import com.mirkamol.pinterestclonemyproject.model.search.Topic
import com.mirkamol.pinterestclonemyproject.ui.fragments.SearchFragment

class searchAdapter(private val context: Context, private val items: ArrayList<Topic>, private val listener: SearchFragment) :
RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image = view.findViewById<ImageView>(R.id.photo_iv)
        val tv_search_text = view.findViewById<TextView>(R.id.tv_search_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is SearchViewHolder) {
            Glide.with(context).load(item.cover_photo.urls.raw).into(holder.image)
            holder.tv_search_text.text = item.title

            holder.image.setOnClickListener {
                listener.onItemClicked(item.title)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener{
        fun onItemClicked( text:String)
    }


}