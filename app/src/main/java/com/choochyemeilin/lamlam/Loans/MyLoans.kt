package com.choochyemeilin.lamlam.Loans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.Loans.adapters.LoansApprovedAdapter
import com.choochyemeilin.lamlam.Loans.adapters.LoansPendingAdapter
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_loans_myloans.view.*
import kotlinx.android.synthetic.main.fragment_loans_pending.view.*

class MyLoans : Fragment() {

    private var utils : Utils = Utils
    var approvedLoans : ArrayList<LoanApplication> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_loans_myloans, container, false)


        getPendingLoans(object : FbCallback {
            override fun onCallback(arr: ArrayList<LoanApplication>) {
                approvedLoans.clear()
                for (i in arr) {
                    if (i.status.toUpperCase() == "APPROVED" || i.status.toUpperCase() == "REJECTED") {
                        approvedLoans.add(i)
                    }
                }

                view.myloans_rv.adapter = LoansApprovedAdapter(approvedLoans)
                view.myloans_rv.layoutManager = LinearLayoutManager(view.context)
                view.myloans_rv.setHasFixedSize(true)
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
                list.clear()
                for (dss in snapshot.children) {
                    dss.children.forEach {
                        val loanID = it.child("loanID").value.toString().toInt()
                        val date = it.child("loanDate").value.toString()
                        val status = it.child("status").value.toString()
                        val product = it.child("productName")
                        val products: ArrayList<String> = ArrayList()
                        for (i in 0 until product.childrenCount) {
                            products.add(product.child(i.toString()).value.toString())
                        }
                        val item = LoanApplication(loanID, date, status, products)
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

    override fun onStop() {
        super.onStop()
        approvedLoans.clear()
    }

}