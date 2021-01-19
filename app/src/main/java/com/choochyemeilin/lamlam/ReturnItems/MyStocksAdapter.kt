package com.choochyemeilin.lamlam.ReturnItems

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.Loans.Classes.LoanApplication
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_stocks_list.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

        val list = context.toList()
       /* // PENDING PENDING PENDING!!!!!
        holder.itemView.textView_stock_name.text = products.product_name
        holder.itemView.textView_stock_qty.text = products.qty
        holder.itemView.textView_stock_date.text = products.returnDate
        loadImage(holder, products.image)*/

        holder.itemView.textView_stock_name.text = list[position].first
        holder.itemView.textView_stock_qty.text = list[position].second.toString()
      //  holder.itemView.textView_stock_date.text=getName().toString()
        //  loadImage(holder, products.image)



        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Loans")

        myRef.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dss in snapshot.children) {
                        dss.children.forEach {
                            val loanDate = it.child("loanDate")
                            val product = it.child("productName")
                            val loanDate1 = it.child("loanDate").value.toString()
                            val product1 = it.child("productName").value.toString()
                            val status = it.child("status").value.toString()

                    //******        holder.itemView.textView_stock_date.text = loanDate1  //2021-1-6 12:39

                   //         holder.itemView.textView_stock_date.text = getReturnDate(loanDate).toString()
                            val pname = productRef.orderByChild("product_name").toString()
                            if(status.toUpperCase() == "PENDING") {

                                product.children.forEach {

                                    val key = it.key.toString()
                            //             holder.itemView.textView_stock_date.text = key
                                     if (key.equals(pname)) {
                                    val img = productRef.orderByChild("image")
                                    loadImage(holder, img.toString())

                                }

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


    }

    override fun getItemCount(): Int {
        return context.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun loadImage(holder: ViewHolder, getImage: String) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReturnDate(daysAgo: DataSnapshot): Date {

     //   val timeAddedLong = ServerValue.TIMESTAMP.toString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val data1=daysAgo.value as Date  //2021-1-6 12:39
        val test1: Any? =data1
        val data=data1.format(Date())
        val test = LocalDate.parse(daysAgo.toString(), formatter)

        val calendar = Calendar.getInstance()

        // Convert Date to Calendar
        calendar.time = data

        calendar.add(Calendar.DATE, -10)

        // Convert calendar back to Date
        val currentDatePlusOne = calendar.time

        return currentDatePlusOne
    }

    fun productImg():String{
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: Query = database.getReference("Products").orderByChild("product_name")
        var test="" as Unit

        myRef.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dss in snapshot.children) {
                        val image=dss.value

                        var image1=image as Unit
                        image1=test
                        return image1
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        return test.toString()
    }

    fun getName(): List<String> {
        var list = ArrayList<String>()

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Products")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dss in snapshot.children) {
                    val productName = dss.child("product_name").value.toString()
                    list.add(productName)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return list
    }
}

private fun Any?.format(date: Date): Date {
return date
}
