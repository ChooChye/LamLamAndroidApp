package com.choochyemeilin.lamlam.Home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.choochyemeilin.lamlam.R

class HomeAdapter(var context: Context, var arrayList: ArrayList<HomeItem>) : BaseAdapter()  {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view:View           = View.inflate(context, R.layout.cardview_home, null)
        var icons: ImageView    = view.findViewById(R.id.icons)
        var names: TextView     = view.findViewById(R.id.name_textView)

        var listItem:HomeItem = arrayList.get(p0)

        icons.setImageResource(listItem.icons!!)
        names.text = listItem.name

        return view
    }

    override fun getItem(p0: Int): Any {
        return arrayList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

}