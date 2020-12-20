package com.choochyemeilin.lamlam.Search

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.search_list_layout.view.*

class SearchAdapter(

    private var context: Context,
    private var arrayList: ArrayList<Products>

) : Adapter<SearchAdapter.ViewHolder>() {


    //View Holder
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.search_list_layout, parent, false)
        return object : ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val products: Products = arrayList.get(position)
        holder.itemView.search_layout_prodName.text = products.product_name
        holder.itemView.search_layout_stockCount.text = products.qty
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}