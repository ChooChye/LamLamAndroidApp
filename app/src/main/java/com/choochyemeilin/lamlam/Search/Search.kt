package com.choochyemeilin.lamlam.Search


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_search.*


class Search : AppCompatActivity() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("Products")
    private lateinit var arrayList: ArrayList<Products>
    private var utils : Utils = Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("SEARCH")
        supportActionBar?.elevation = 0f

        arrayList = ArrayList()
        setupArray()

        rv_result.setHasFixedSize(true)


        val searchText : EditText = findViewById(R.id.search_text)

        searchText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    search(p0.toString().toUpperCase())
                }

            }
        )
    }

    private fun search(kword: String) {
        val searchArrayList : ArrayList<Products> = ArrayList()
        for(list in arrayList){
            if(list.product_name.toString().toUpperCase().contains(kword)){
                searchArrayList.add(list)
            }
        }
        val myAdapter = SearchAdapter(searchArrayList, applicationContext)
        rv_result.adapter = myAdapter
        rv_result.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        myAdapter.notifyDataSetChanged()
    }

    private fun setupArray() {

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val catItem: Products? = dss.getValue(Products::class.java)
                    if (catItem != null) {
                        arrayList.add(catItem)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                utils.toast(applicationContext, "Search Error #1256 | $error", 0)
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}