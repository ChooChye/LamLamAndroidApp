package com.choochyemeilin.lamlam.Scan.StockCount


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.ScanHistory.ScanHistoryObj
import com.choochyemeilin.lamlam.helpers.Utils
import kotlinx.android.synthetic.main.stock_count_items.view.*

class StockCountAdapter(
    private var mutableMapLoans: MutableMap<String, Int>,
    private var mutableMapHistory: MutableMap<String, Int>
) : RecyclerView.Adapter<StockCountAdapter.StockCountVH>() {

    private lateinit var newArrayList : ArrayList<ScanHistoryObj>
    class StockCountVH(itemView: View) : RecyclerView.ViewHolder(itemView){
        val prodName = itemView.stockCountItems_tv_prodName
        val stockCount = itemView.stockCountItems_tv_qty
        val status = itemView.stockCount_tv_status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCountVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_count_items, parent, false)
        return StockCountVH(itemView)
    }


    override fun onBindViewHolder(holder: StockCountVH, position: Int) {
        //Utils.log("Adapter : Loans = $mutableMapLoans \n\n History = $mutableMapHistory")

        var listLoans = mutableMapLoans.toList()
        var listHistory = mutableMapHistory.toList()
        val totalLoanQty = listLoans[position].second

        holder.prodName.text = listLoans[position].first

        try {
            if(mutableMapHistory.containsKey(listLoans[position].first)){
                val totalScannedQty = mutableMapHistory[listLoans[position].first]
                holder.stockCount.text = "($totalScannedQty/$totalLoanQty)"

                if(totalScannedQty == totalLoanQty){
                    holder.status.text = "BALANCED"
                    holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorSuccess))
                    holder.status.visibility = View.VISIBLE
                }else{
                    holder.status.text = "IMBALANCED"
                    holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorError))
                    holder.status.visibility = View.VISIBLE
                }
            }else{
                holder.stockCount.text = "(0/$totalLoanQty)"
            }
        }catch (e : Exception){
            Utils.log(e.message.toString())
        }
    }

    override fun getItemCount(): Int {
        return mutableMapLoans.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}