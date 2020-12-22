 package com.choochyemeilin.lamlam.ReturnItems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Search.SearchAdapter
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_my_stocks.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.rv_result

 class MyStocks : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = databaseReference.getReference("Categories")
    private lateinit var arrayList: ArrayList<Products>
    private var utils : Utils = Utils

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
        var query : Query = myRef.child("Tops").orderByChild("product_name")
          //  .startAt(kword)
           // .endAt(kword + "\uf0ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    arrayList.clear()
                    for (dss in snapshot.children) {
                        //utils.log("${dss.value}")
                        val productItem : Products? = dss.getValue(Products::class.java)
                        if(productItem != null){
                            arrayList.add(productItem)
                        }
                    }
                    val myAdapter = MyStocksAdapter(applicationContext, arrayList)
                    list_view_recycle.adapter = myAdapter
                    list_view_recycle.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                    myAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                utils.log("$error")
            }

        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

}