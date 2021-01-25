package com.choochyemeilin.lamlam.ReturnItems

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.Loans.Classes.LoanApplication
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.fromJson
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_stocks_list.*
import kotlinx.android.synthetic.main.my_stocks_list.view.*
import kotlinx.android.synthetic.main.return_item_form.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class MyStocksAdapter(

    private var context: MutableMap<String, Int>
) : RecyclerView.Adapter<MyStocksAdapter.ViewHolder>() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var productRef: DatabaseReference = database.getReference("Products")
    private var utils : Utils = Utils


    //View Holder
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_stocks_list, parent, false)
        return object : ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val list = context.toList()

        holder.itemView.textView_stock_name.text = list[position].first
        holder.itemView.textView_stock_qty.text = list[position].second.toString()
        var sname=holder.itemView.textView_stock_name.text.toString()


       productRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dss in snapshot.children){

                    val pname=dss.child("product_name").value
                    val image=dss.child("image").value

                    if(sname==pname){

                        loadImage(holder, image.toString())
                     }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        val myRef: DatabaseReference = database.getReference("Loans")

        myRef.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dss in snapshot.children) {
                        dss.children.forEach {
                            val loanDate = it.child("loanDate").value
                            val product = it.child("productName")
                            val loanDate1 = it.child("loanDate").value.toString()
                            val product1 = it.child("productName").value.toString()
                            val status = it.child("status").value.toString()




                            product.children.forEach{
                                val pname=it.key
                                if(sname==pname){
                                    holder.itemView.textView_stock_date.text = loanDate1
                               //     holder.itemView.textView_stock_testing.text= testing.toString()
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun readJSON(json: String): List<Products> {

        return if (json != null)
            Gson().fromJson(json) //GsonExtension Call
        else
            listOf()
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

/*    @RequiresApi(Build.VERSION_CODES.O)
    fun getReturnDate(daysAgo: Int): Date {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

        return calendar.time
    *//* //   val timeAddedLong = ServerValue.TIMESTAMP.toString()
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

        return currentDatePlusOne*//*
    }*/


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
