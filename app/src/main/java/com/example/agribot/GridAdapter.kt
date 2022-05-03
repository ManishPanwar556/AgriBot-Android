package com.example.agribot

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class GridAdapter(var context: Context, var list: ArrayList<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    fun modifyData(pos:Int,data:String){
        list[pos]=data;
        notifyDataSetChanged();
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = View.inflate(context, R.layout.grid_layout_item, null)
        val textView = view.findViewById<TextView>(R.id.weatherInfo);
        val imageView=view.findViewById<ImageView>(R.id.logo)
        if(p0==0)
        imageView.setBackgroundResource(R.drawable.humidity);

        else if(p0==1)
        imageView.setBackgroundResource(R.drawable.soil);

        else if(p0==2)
        imageView.setBackgroundResource(R.drawable.temperature);

        else if(p0==3)
        imageView.setBackgroundResource(R.drawable.waterlevel);

        textView.text = list[p0];
        return view;
    }

}