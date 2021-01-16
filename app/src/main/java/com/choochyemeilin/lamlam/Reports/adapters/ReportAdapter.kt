package com.choochyemeilin.lamlam.Reports.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.Loans.adapters.LoansApprovedAdapter
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Reports.objects.ReportsObject
import kotlinx.android.synthetic.main.reports_item_layout.view.*

class ReportAdapter(
    private var arrayList: MutableMap<String, Int>
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>(){

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val prodName = itemView.reports_item_tv_prodName
        val qty = itemView.reports_item_tv_qty
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.reports_item_layout, parent, false)
        return ReportViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val list = arrayList.toList()
        holder.prodName.text = list[position].first
        holder.qty.text = list[position].second.toString()
    }

    override fun getItemCount(): Int {
        return arrayList.size+1
    }
}