 package com.choochyemeilin.lamlam.ReturnItems

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Search.SearchAdapter
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_my_stocks.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.rv_result
import kotlinx.android.synthetic.main.my_stocks_list.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

 class MyStocks : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = databaseReference.getReference("Categories")
    private lateinit var arrayList: ArrayList<Products>
    private var utils : Utils = Utils

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_stocks)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = "My Stocks"

        arrayList = ArrayList()
        list_view_recycle.setHasFixedSize(true)

       // recyclerView=findViewById(R.id.list_view_recycle)
       StocksRecyclerView()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun StocksRecyclerView(){
    /*
      var  FirebaseRecyclerAdapter=object: FirebaseRecyclerAdapter<Products,TextItemViewHolder>(
          Products::class.java,
          R.layout.my_stocks_list
      ){
          val currentUserDb=myRef.reference.child("User")
          currentUserDb?.child("Staff ID")?.setValue(editTextNumber_register_staffID.text.toString())
      }
     */

       /* var query : Query = myRef.child("Tops").orderByChild("product_name")

   //     var query : Query = myRef
        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                    for (dss in snapshot.children) {
                            dss.children.forEach {

                            }
                            //utils.log("${dss.value}")

                            val productItem: Products? = dss.getValue(Products::class.java)
                            if (productItem != null) {
                                arrayList.add(productItem)
                            }

                        val myAdapter = MyStocksAdapter(applicationContext, arrayList)
                        list_view_recycle.adapter = myAdapter
                        list_view_recycle.layoutManager = LinearLayoutManager(
                            applicationContext, LinearLayoutManager.VERTICAL,
                            false
                        )
                        myAdapter.notifyDataSetChanged()


                    }

            }


            override fun onCancelled(error: DatabaseError) {
                utils.log("$error")
            }

        })


        */
        var loansRef: DatabaseReference = databaseReference.getReference("Loans")
        var productRef: DatabaseReference = databaseReference.getReference("Products")
        loansRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val productItem : Products? = dss.getValue(Products::class.java)
                    dss.children.forEach {
                        val status = it.child("status").value
                        if (status.toString() == "approved") {
                            val loopName=it.child("productName")

                            loopName.children.forEach{
                                val k=it.key
                                val quantity=it.value

                                val objName=Products()
                              //  arrayList.add()
                            }

                            val pname=productRef.orderByChild("product_name")
                            val qty=it.child("productName")

                            if (qty.equals(pname)){
                                val img=productRef.orderByChild("image")
                                val pqty=qty.getValue().toString()

                                //      val pimg= img.toString()
                                //    holder.itemView.image_mystock.setImageResource(pimg)


                            }
                            val date=it.child("returnDate").value

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

}