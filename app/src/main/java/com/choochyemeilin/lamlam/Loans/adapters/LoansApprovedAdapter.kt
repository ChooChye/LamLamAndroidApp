package com.choochyemeilin.lamlam.Loans.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.Loans.Classes.LoanApplication
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import kotlinx.android.synthetic.main.loans_pending_list_layout.view.*

class LoansApprovedAdapter(
    private val dataList: List<LoanApplication>
) : RecyclerView.Adapter<LoansApprovedAdapter.ViewHolder>() {
    private var utils: Utils = Utils

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.loan_tv_title
        val date: TextView = itemView.loan_tv_date
        val status: TextView = itemView.loan_tv_status

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
        if (holder.status.text.toString() == "APPROVED") {
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorSuccess))
        } else {
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorError))
        }
        holder.itemView.setOnClickListener { showDialog(context, position) }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    //Show Dialog
    private fun showDialog(context: Context, position: Int) {
        var builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        val data = dataList[position]
        var msg = "Date Applied : ${data.loanDate}\n\n" +
                "Products Requested : \n"
        val x = data.productName
        var index = 1
        x.forEach {
            val key = it.key
            val qty = it.value
            msg += "$index - $key ($qty)\n"
            index++
        }
        builder
            .setTitle("LOAN ID #${dataList[position].loanID}")
            .setMessage(msg)
            .setNegativeButton("OK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }
}