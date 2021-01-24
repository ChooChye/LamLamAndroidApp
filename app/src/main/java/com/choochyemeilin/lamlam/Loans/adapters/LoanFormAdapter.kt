package com.choochyemeilin.lamlam.Loans.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import kotlinx.android.synthetic.main.loanform_select_product_list.view.*

class LoanFormAdapter(

    private var arr: ArrayList<String> = ArrayList(),
    var fbCallback: FbCallback

) : RecyclerView.Adapter<LoanFormAdapter.LoanFormViewHolder>() {


    private var mutableList = mutableMapOf<String, Int>()


    class LoanFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prodName: TextView = itemView.loanform_tv_prodName
        val counter: TextView = itemView.loanForm_tv_counter
        val plusBtn: Button = itemView.loanForm_btn_plus
        val minusBtn: Button = itemView.loanForm_btn_minus
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LoanFormViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.loanform_select_product_list, parent, false)
        return LoanFormViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: LoanFormViewHolder, position: Int) {
        holder.prodName.text = arr[position]
        holder.counter.id = position
        mutableList.apply { this[arr[position]] = 0 }
        holder.minusBtn.setOnClickListener { minusCounter(holder) }
        holder.plusBtn.setOnClickListener { plusCounter(holder) }
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    private fun plusCounter(holder: LoanFormViewHolder) {
        val product = holder.prodName.text.toString()
        val count = holder.counter.text.toString().toInt() + 1
        holder.counter.text = count.toString()
        mutableList[product] = count
        fbCallback.push(mutableList)
    }

    private fun minusCounter(holder: LoanFormViewHolder) {
        val product = holder.prodName.text.toString()
        var count: Int = holder.counter.text.toString().toInt()
        var x: Int
        if (count != 0) {
            x = --count
            holder.counter.text = x.toString()
            mutableList[product] = x
        }
        fbCallback.push(mutableList)
    }
}