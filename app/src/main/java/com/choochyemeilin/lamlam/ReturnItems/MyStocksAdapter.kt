package com.choochyemeilin.lamlam.ReturnItems

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Search.SearchAdapter
import com.choochyemeilin.lamlam.helpers.Products
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.my_stocks_list.view.*
import kotlinx.android.synthetic.main.search_list_layout.view.*


class MyStocksAdapter (

    private var context: Context,
    private var arrayList: ArrayList<Products>


) : RecyclerView.Adapter<MyStocksAdapter.ViewHolder>() {

    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    var loansRef: DatabaseReference = databaseReference.getReference("Loans")
    var productRef: DatabaseReference = databaseReference.getReference("Products")

    //View Holder
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_stocks_list, parent, false)
        return object : ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val products: Products = arrayList.get(position)

        // PENDING PENDING PENDING!!!!!
        holder.itemView.textView_stock_name.text = products.product_name
        holder.itemView.textView_stock_qty.text = products.qty
        holder.itemView.textView_stock_date.text = products.returnDate
        loadImage(holder, products.image)


     /*   loansRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val productItem : Products? = dss.getValue(Products::class.java)
                    dss.children.forEach {
                        val status = it.child("status").value
                        if (status.toString() == "pending") {
                            val loopName=it.child("productName")

                            loopName.children.forEach{
                                val k=it.key
                                val quantity=it.value

                                holder.itemView.textView_stock_name.text = k.toString()
                                holder.itemView.textView_stock_qty.text=quantity.toString()

                            }

                           *//* val pname=productRef.orderByChild("product_name")
                             val qty=it.child("productName")

                            if (qty.equals(pname)){
                                val img=productRef.orderByChild("image")
                                val pqty=qty.getValue().toString()

                                val pimg= img.toString()
                            }*//*
                            loadImage(holder, products.image)
                            }
                            val date=it.child("returnDate").value
                            holder.itemView.textView_stock_date.text = date.toString()
                        }
                    }
                }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun loadImage(holder: ViewHolder, getImage : String) {
        var image : String
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.reference.child("products/$getImage")
        gsReference.downloadUrl.addOnSuccessListener { Uri ->
            image = Uri.toString()
            Picasso.get().load(image).into(holder.itemView.image_mystock)
            android.os.Handler().postDelayed( {
                holder.itemView.mystock_progressBar.visibility = View.GONE
            }, 1000)
        }.addOnFailureListener {
            holder.itemView.image_mystock.setImageResource(R.drawable.no_image)
        }

    }

    private fun loadName(holder: ViewHolder, getName : String) {
        loansRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val productItem : Products? = dss.getValue(Products::class.java)
                    dss.children.forEach {
                        val status = it.child("status").value
                        if (status.toString() == "pending") {
                            val loopName=it.child("productName")

                            loopName.children.forEach{
                                val k=it.key
                                val quantity=it.value

                                holder.itemView.textView_stock_name.text = k.toString()
                                holder.itemView.textView_stock_qty.text=quantity.toString()

                            }

                            /* val pname=productRef.orderByChild("product_name")
                              val qty=it.child("productName")

                             if (qty.equals(pname)){
                                 val img=productRef.orderByChild("image")
                                 val pqty=qty.getValue().toString()

                                 val pimg= img.toString()
                             }*/

                        }
                        val date=it.child("returnDate").value
                        holder.itemView.textView_stock_date.text = date.toString()
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}