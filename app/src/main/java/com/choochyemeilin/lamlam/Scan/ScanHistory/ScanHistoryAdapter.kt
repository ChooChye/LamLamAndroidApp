package com.choochyemeilin.lamlam.Scan.ScanHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_scan_history.view.*
import kotlinx.android.synthetic.main.scan_history_items.view.*
import okhttp3.internal.notifyAll


class ScanHistoryAdapter(
    private var mutableList: MutableList<ScanHistoryObj>
) : RecyclerView.Adapter<ScanHistoryAdapter.ScanHistoryVH>() {

    private var removedPosition: Int = 0
    private var removedItem: String = ""


    class ScanHistoryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prodName = itemView.scanHistory_items_tv_prodName
        val time = itemView.scanHistory_items_tv_time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanHistoryVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.scan_history_items, parent, false)
        return ScanHistoryVH(itemView)
    }

    override fun onBindViewHolder(holder: ScanHistoryVH, position: Int) {

        holder.prodName.text = "1 x ${mutableList[position].product_name}"
        holder.time.text = mutableList[position].date + " " + mutableList[position].time
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.adapterPosition
        val dataSet = mutableList[position]
        removedPosition = position
        val time = dataSet.time
        removedItem = dataSet.product_name
        mutableList.removeAt(position)
        notifyItemRemoved(position)

        removeInDB(dataSet) // Remove in Firebase

        Snackbar.make(viewHolder.itemView, "$removedItem ($time) deleted", Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
                mutableList.add(removedPosition, dataSet)
                notifyItemInserted(removedPosition)
                undo(dataSet)
            }.show()
    }

    private fun removeInDB(dataSet: ScanHistoryObj) {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var myRef: DatabaseReference = database.getReference("ScanHistory")

        myRef.child(dataSet.date).child(dataSet.time).removeValue()
    }

    private fun undo(dataSet: ScanHistoryObj){
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var myRef: DatabaseReference = database.getReference("ScanHistory")

        myRef.child(dataSet.date).child(dataSet.time).child("0").setValue(dataSet)
    }


}