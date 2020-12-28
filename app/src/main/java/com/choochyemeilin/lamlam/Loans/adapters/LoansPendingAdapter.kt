package com.choochyemeilin.lamlam.Loans.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.Loans.LoanApplication
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_loans_pending.view.*
import kotlinx.android.synthetic.main.loans_pending_list_layout.view.*

class LoansPendingAdapter(
    private val dataList: List<LoanApplication>
) : RecyclerView.Adapter<LoansPendingAdapter.ViewHolder>() {
    private var utils: Utils = Utils

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.loan_tv_title
        val date = itemView.loan_tv_date
        val status = itemView.loan_tv_status

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.loans_pending_list_layout, parent, false)
        return ViewHolder(itemView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        val context = holder.itemView.context
        holder.title.text = "#${currentItem.loanID}"
        holder.date.text = currentItem.loanDate
        holder.status.text = currentItem.status.toUpperCase()
        if (holder.status.text.toString() == "PENDING") {
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        }
        holder.title.setOnClickListener { showDialog(context, position) }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    //Show Dialog
    private fun showDialog(context: Context, position: Int) {

        var builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        val role = "admin".toLowerCase()
        val data = dataList[position]
        var msg = "Date Applied : ${data.loanDate}\n\n" +
                "Products Requested : \n"
        for (i in 0 until data.productName.size) {
            msg += "$i - ${data.productName[i]}\n"
        }
        builder
            .setTitle("LOAN ID #${dataList[position].loanID}")
            .setMessage(msg)
        if (role == "staff") {
            builder.setNegativeButton("OK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
        } else {
            builder
                .setPositiveButton("APPROVE") { dialogInterface: DialogInterface, _: Int ->
                    actionLoan(dataList[position].loanID!!.toInt(), "approved")
                    dialogInterface.dismiss()
                }
                .setNeutralButton("CANCEL") { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                .setNegativeButton("REJECT") { dialogInterface: DialogInterface, _: Int ->
                    actionLoan(dataList[position].loanID!!.toInt(), "rejected")
                    dialogInterface.dismiss()
                }
        }
        builder.show()

    }

    private fun actionLoan(loanID: Int, status : String) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Loans")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    dss.children.forEach {
                        val key = dss.key.toString()
                        val dbLoanID = it.child("loanID").value
                        if(dbLoanID.toString().toInt() == loanID.toString().toInt()){
                            //update value
                            myRef.child(key).child(it.key.toString()).child("status").setValue(status.toLowerCase())
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}