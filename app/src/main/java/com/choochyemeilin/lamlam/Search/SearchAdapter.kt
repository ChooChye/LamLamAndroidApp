package com.choochyemeilin.lamlam.Search

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_list_layout.view.*
import kotlin.collections.ArrayList


class SearchAdapter(
    private var arrayList: ArrayList<Products>,
    private var context: Context
) : Adapter<SearchAdapter.ViewHolder>() {


    //View Holder
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.search_list_layout, parent, false)
        return object : ViewHolder(view) {}
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val products: Products = arrayList.get(position)
        loadImage(holder, products.image)
        holder.itemView.search_layout_prodName.text = products.product_name
        holder.itemView.search_layout_stockCount.text = "In Stock (${products.qty})"
    }

    private fun loadImage(holder: ViewHolder, getImage : String) {
        var image : String
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.reference.child("products/" + getImage)
        gsReference.downloadUrl.addOnSuccessListener { Uri ->
            image = Uri.toString()
            Picasso.get().load(image).into(holder.itemView.search_layout_image)
        }.addOnFailureListener {
            image = "fail"
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}