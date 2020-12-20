package com.choochyemeilin.lamlam.ReturnItems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyStocks : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = databaseReference.getReference("Products")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_stocks)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recyclerView=findViewById(R.id.list_view_recycle)
        logRecyclerView()
    }

    private fun logRecyclerView(){
      var  FirebaseRecyclerAdapter=object: FirebaseRecyclerAdapter<Products,TextItemViewHolder>(
          Products::class.java,
          R.layout.my_stocks_list,
          TextItemViewHolder::class.java,
          myRef
      ){
          val currentUserDb=myRef.reference.child("User")
          currentUserDb?.child("Staff ID")?.setValue(editTextNumber_register_staffID.text.toString())
      }

    }

    // View Holder
    class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView){

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

}