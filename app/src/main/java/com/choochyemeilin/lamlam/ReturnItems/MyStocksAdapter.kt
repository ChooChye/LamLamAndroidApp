package com.choochyemeilin.lamlam.ReturnItems

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import kotlinx.android.synthetic.main.my_stocks_list.view.*


class MyStocksAdapter (

    private var context: Context,
    private var arrayList: ArrayList<Products>

) : RecyclerView.Adapter<MyStocksAdapter.ViewHolder>() {


    //View Holder
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_stocks_list, parent, false)
        return object : ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val products: Products = arrayList.get(position)
        holder.itemView.textView_stock_name.text = products.product_name
        holder.itemView.textView_stock_date.text = products.returnDate
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}