package com.choochyemeilin.lamlam.Loans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.Loans.Classes.LoanApplication
import com.choochyemeilin.lamlam.Loans.adapters.LoansPendingAdapter
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_loans_pending.*
import kotlinx.android.synthetic.main.fragment_loans_pending.view.*
import java.lang.Exception


class LoansPending : Fragment() {

    var pendingLoans: ArrayList<LoanApplication> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_loans_pending, container, false)
        view.loansPending_progressBar.visibility = View.VISIBLE
        getPendingLoans(object : FbCallback {
            override fun onCallback(arr: ArrayList<LoanApplication>) {
                try {
                    pendingLoans.clear()
                    for (i in arr) {
                        if (i.status.toUpperCase() == "PENDING") {
                            pendingLoans.add(i)
                        }
                    }

                    view.loansPending_rv.adapter = LoansPendingAdapter(pendingLoans)
                    view.loansPending_rv.layoutManager = LinearLayoutManager(view.context)
                    view.loansPending_rv.setHasFixedSize(true)
                    view.loansPending_progressBar.visibility = View.GONE
                } catch (e: Exception) {
                    Utils.toast(
                        view.context,
                        "An error has occurred. Please restart the application",
                        0
                    )
                    Utils.log(e.message.toString())
                }

            }
        })
        return view
    }

    private fun getPendingLoans(callback: FbCallback): List<LoanApplication> {
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
                        val products: MutableMap<String, Int> = mutableMapOf()
                        for (i in 0 until product.childrenCount) {
                            val prodName = product.child(i.toString()).value
                            products[prodName.toString()] = 0
                            //products.add(product.child(i.toString()).value.toString())
                        }
                        val item = LoanApplication(loanID, date, status, products)
                        list.add(item)
                        //{loanDate=2020-11-26 23:16, loanID=45647, productName=[Pink Sweatshirt, Blue Sweatshirt, Levi's Jeans (Black)], status=pending}
                    }
                }
                callback.onCallback(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.toast(view?.context!!, "An error has occurred #0984 | ${error.message}", 1)
            }

        })
        return list
    }

    override fun onStop() {
        super.onStop()
        pendingLoans.clear()
    }
}