package com.smart.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smart.weatherapp.R

class RecyclerAdapter(ctx : Context, itemDailyWeatherList : List<String>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private  var itemList : List<String>
    init {
        itemList = itemDailyWeatherList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_cardview, viewGroup, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = itemList[i]
    }


    override fun getItemCount(): Int {
        return  itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemTitle: TextView
        init {
            itemTitle = itemView.findViewById(R.id.totalAmountTv)
        }
    }



}