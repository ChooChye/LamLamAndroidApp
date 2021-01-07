 package com.choochyemeilin.lamlam.ReturnItems

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Search.SearchAdapter
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Retailers
import com.choochyemeilin.lamlam.helpers.Utils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_my_stocks.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.rv_result
import kotlinx.android.synthetic.main.my_stocks_list.*
import kotlinx.android.synthetic.main.my_stocks_list.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.jvm.java as java1

 class MyStocks : AppCompatActivity() {

     lateinit var mRecyclerView: RecyclerView

     var database: FirebaseDatabase = FirebaseDatabase.getInstance()
     private var myRef: DatabaseReference = database.getReference("Categories")
     private lateinit var arrayList: ArrayList<Products>
    private var utils : Utils = Utils
     private lateinit var auth: FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_stocks)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = "My Stocks"

        arrayList = ArrayList()
        list_view_recycle.setHasFixedSize(true)

      //  mRecyclerView=findViewById(R.id.list_view_recycle)
       StocksRecyclerView()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun StocksRecyclerView(){

        //----------ORIGINAL------------
       var query : Query = myRef.child("Tops").orderByChild("product_name")
    //    var query : Query = myRef.child("2021-01-07").orderByChild("productName")

   //     var query : Query = myRef
        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                    for (dss in snapshot.children) {
                            dss.children.forEach {

                            }
                            //utils.log("${dss.value}")

                            val productItem: Products? = dss.getValue(Products::class.java1)
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


//-----------MY STOCKS ADAPTER-----------------
        /*var loansRef: DatabaseReference = database.getReference("Loans")
        var productRef: DatabaseReference = database.getReference("Products")
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

                                val objName=Products()
                              //  arrayList.add()
                                textView_stock_name.text = k.toString()
                                textView_stock_qty.text=quantity.toString()
                            }

                           *//* val pname=productRef.orderByChild("product_name")
                            val qty=it.child("productName")

                            if (qty.equals(pname)){
                                val img=productRef.orderByChild("image")
                                val pqty=qty.getValue().toString()

                                //      val pimg= img.toString()
                                //    holder.itemView.image_mystock.setImageResource(pimg)


                            }*//*
                        //    val date=it.child("returnDate").value

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
*/

        /*//------ADAPTER INSIDE------ERROR---------
      var  FirebaseRecyclerAdapter=object: FirebaseRecyclerAdapter<Products,ItemViewHolder>(
          Products::class.java1,
          R.layout.my_stocks_list,
          ItemViewHolder::class.java,
          myRef
      ){
          override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
              TODO("Not yet implemented")
          }

          override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: Products) {
              val products: Products = arrayList.get(position)
              holder.itemView.textView_stock_name.text = products.product_name
              holder.itemView.textView_stock_qty.text = products.qty
              holder.itemView.textView_stock_date.text = products.returnDate
          }
      }
        mRecyclerView.adapter=FirebaseRecyclerAdapter*/
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

     fun getRetailerID(callback : FbCallback) : Int{
         val user = auth.currentUser?.email
         var retailerID  = 0
         val myRef: DatabaseReference = database.getReference("User")
         myRef.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 for(dss in snapshot.children){
                     val staffEmail = dss.child("staffEmail").value.toString()
                     if(user == staffEmail){
                         val retailerID = dss.child("retailerID").value.toString().toInt()
                         callback.onCallbackGetUserID(retailerID!!)
                     }
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 utils.log("Error has occurred #9372 | ${error.message}")
             }
         })
         return retailerID
     }

     fun getRetailerInfo(callback : FbCallback){
         val user = auth.currentUser?.email
         var retailerID : Int? = 0

         getRetailerID(object : FbCallback{
             override fun onCallbackGetUserID(uid: Int) {
                 super.onCallbackGetUserID(uid)
                 retailerID =  uid
             }
         })

         val myRef: DatabaseReference = database.getReference("Retailers")
         myRef.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 for(dss in snapshot.children){
                     val dbRetailerID = dss.child("retailerID").value.toString().toInt()
                     if(retailerID == dbRetailerID){
                         val retailerName = dss.child("retailerName").value.toString()
                         val retailerAddress = dss.child("retailerAddress").value.toString()
                         val arr : ArrayList<Retailers> = ArrayList()
                         arr.add(Retailers(dbRetailerID, retailerName, retailerAddress))
                         callback.onCallbackRetailer(arr)
                     }
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 utils.log("Error has occurred #9373 | ${error.message}")
             }
         })
     }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

}