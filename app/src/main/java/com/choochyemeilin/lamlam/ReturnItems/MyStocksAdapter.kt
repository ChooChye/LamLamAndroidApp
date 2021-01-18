package com.choochyemeilin.lamlam.ReturnItems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_stocks_list.view.*
import java.util.*


class MyStocksAdapter(

    private var context: MutableMap<String, Int>

) : RecyclerView.Adapter<MyStocksAdapter.ViewHolder>() {

    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    var loansRef: DatabaseReference = databaseReference.getReference("Loans")
    var productRef: DatabaseReference = databaseReference.getReference("Products")
    private var utils : Utils = Utils
    private lateinit var auth: FirebaseAuth

    //View Holder
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_stocks_list, parent, false)
        return object : ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


     //   val products: Products = arrayList.get(position)
      //  val products: Products = arrayList.get(position)
        val list = context.toList()
       /* // PENDING PENDING PENDING!!!!!
        holder.itemView.textView_stock_name.text = products.product_name
        holder.itemView.textView_stock_qty.text = products.qty
        holder.itemView.textView_stock_date.text = products.returnDate
        loadImage(holder, products.image)*/

        holder.itemView.textView_stock_name.text = list[position].first
        holder.itemView.textView_stock_qty.text = list[position].second.toString()


      //  loadImage(holder, products.image)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Loans")

        myRef.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dss in snapshot.children) {
                        dss.children.forEach {
                            val loanDate = it.child("loanDate").value.toString()
                            val product = it.child("productName")
                               holder.itemView.textView_stock_date.text = loanDate
                         //   holder.itemView.textView_stock_date.text = getReturnDate(loanDate).toString()



                            product.children.forEach {
                                val pname=productRef.orderByChild("product_name")
                                val key = it.key.toString()
                                if (key.equals(pname)){
                                    val img=productRef.orderByChild("image")
                                    loadImage(holder, img)
                                }

                            }




                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


/*
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

       /* loansRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    dss.children.forEach {
                        val status = it.child("staffID").value

                        val staffEmail = dss.child("staffEmail").value.toString()

                        if (status.toString() == "staffID") {

                        }

                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/
    }

    override fun getItemCount(): Int {
        return context.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun loadImage(holder: ViewHolder, getImage: Query) {
        var image : String
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.reference.child("products/$getImage")
        gsReference.downloadUrl.addOnSuccessListener { Uri ->
            image = Uri.toString()
            Picasso.get().load(image).into(holder.itemView.image_mystock)
            android.os.Handler().postDelayed({
                holder.itemView.mystock_progressBar.visibility = View.GONE
            }, 1000)
        }.addOnFailureListener {
            holder.itemView.image_mystock.setImageResource(R.drawable.no_image)
        }

    }

    fun getReturnDate(daysAgo: DataSnapshot): Date {




        val calendar = Calendar.getInstance()


        calendar.add(Calendar.DATE, -10)

        return calendar.time
    }
}