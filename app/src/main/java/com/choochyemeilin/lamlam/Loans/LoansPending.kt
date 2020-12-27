package com.choochyemeilin.lamlam.Loans

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.Loans.adapters.LoansPendingAdapter
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_loans_pending.view.*
import kotlin.collections.ArrayList
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback

class LoansPending : Fragment() {

    private var utils : Utils = Utils
    var datas: ArrayList<LoanApplication> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_loans_pending, container, false)

        getPendingLoans(object : FbCallback{
            override fun onCallback(arr: ArrayList<LoanApplication>) {
                datas = arr
                view.loansPending_rv.adapter = LoansPendingAdapter(datas)
                view.loansPending_rv.layoutManager = LinearLayoutManager(view.context)
                view.loansPending_rv.setHasFixedSize(true)
            }
        })
        return view
    }

    private fun getPendingLoans(callback: FbCallback) : List<LoanApplication>{
        var list = ArrayList<LoanApplication>()

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Loans")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    dss.children.forEach {
                        val loanID = it.child("loanID").value.toString().toInt()
                        val date = it.child("loanDate").value.toString()
                        val status = it.child("status").value.toString()
                        val product = it.child("productName")
                        val products : ArrayList<String> = ArrayList()
                        for (i in 0 until product.childrenCount){
                            products.add(product.child(i.toString()).value.toString())
                        }
                        val item = LoanApplication(loanID,date, status, products)
                        list.add(item)
                        //{loanDate=2020-11-26 23:16, loanID=45647, productName=[Pink Sweatshirt, Blue Sweatshirt, Levi's Jeans (Black)], status=pending}
                    }
                }
                callback.onCallback(list)
            }

            override fun onCancelled(error: DatabaseError) {
                utils.toast(view?.context!!, "An error has occurred #0984 | ${error.message}", 1)
            }

        })
        return list
    }
}