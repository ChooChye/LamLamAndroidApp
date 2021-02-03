package com.choochyemeilin.lamlam.Scan.StockCount

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.ScanHistory.ScanHistoryObj
import kotlinx.android.synthetic.main.search_list_layout.view.*

class StockCountAdapter(
    private var mutableMap: MutableMap<String, Int>,
    private var arrayList: ArrayList<ScanHistoryObj>
) : RecyclerView.Adapter<StockCountAdapter.StockCountVH>() {

    class StockCountVH(itemView: View) : RecyclerView.ViewHolder(itemView){
        val prodName = itemView.search_layout_prodName
        val image = itemView.search_layout_image
        val stockCount = itemView.search_layout_stockCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCountVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_list_layout, parent, false)
        return StockCountVH(itemView)
    }

    override fun onBindViewHolder(holder: StockCountVH, position: Int) {
        val list = mutableMap.toList()
        val totalLoanQty = list[position].second
        val scanHistoryObj = arrayList[position]
        if(scanHistoryObj.product_name.equals(list[position].first)){
            val scannedQty = scanHistoryObj.scannedQty
            holder.prodName.text = list[position].first
            holder.stockCount.text = "($scannedQty/$totalLoanQty)"
        }
    }

    override fun getItemCount(): Int {
        return mutableMap.size
    }
}