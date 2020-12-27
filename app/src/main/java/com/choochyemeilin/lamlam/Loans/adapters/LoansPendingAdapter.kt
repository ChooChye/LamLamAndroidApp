package com.choochyemeilin.lamlam.Loans.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.Loans.LoanApplication
import com.choochyemeilin.lamlam.R
import kotlinx.android.synthetic.main.loans_pending_list_layout.view.*

class LoansPendingAdapter(
    private val dataList: List<LoanApplication>
) : RecyclerView.Adapter<LoansPendingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.loan_tv_title
        val date = itemView.loan_tv_date
        val status = itemView.loan_tv_status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.loans_pending_list_layout, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.title.text = "#${currentItem.loanID}"
        holder.date.text = currentItem.loanDate
        holder.status.text = currentItem.status.toUpperCase()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}