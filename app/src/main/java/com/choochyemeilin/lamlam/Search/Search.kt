package com.choochyemeilin.lamlam.Search


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_search.*
import com.choochyemeilin.lamlam.Search.SearchAdapter


class Search : AppCompatActivity() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("Categories")
    private lateinit var arrayList: ArrayList<Products>
    private var utils : Utils = Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("SEARCH")
        supportActionBar?.elevation = 0f

        arrayList = ArrayList()
        rv_result.setHasFixedSize(true)

        val kword : String = search_text.text.toString()

        search_searchBtn.setOnClickListener { search(kword) }

        search_text.addTextChangedListener { object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()){
                    search("")
                }else{
                    search(p0.toString())
                }
            }

        }
        }
    }

    private fun search(kword: String){

        var query : Query = myRef.child("Tops").orderByChild("product_name")
            .startAt(kword)
            .endAt(kword + "\uf0ff")
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
                    val myAdapter = SearchAdapter(applicationContext, arrayList)
                    rv_result.adapter = myAdapter
                    rv_result.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                    myAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                utils.log("$error")
            }

        })
    }

    /*private fun setupRV() {
        val query: Query = FirebaseDatabase.getInstance()
            .reference
            .child("Categories").child("Tops")
            .limitToLast(50)

        val options: FirebaseRecyclerOptions<Products> = FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(query, Products::class.java)
            .build()

        adapter = SearchAdapter(options)

        rv_result.adapter = adapter
        rv_result.layoutManager = LinearLayoutManager(this)
    }*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}