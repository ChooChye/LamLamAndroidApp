package com.choochyemeilin.lamlam.Loans.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.AutoRepeatButton
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import kotlinx.android.synthetic.main.loanform_select_product_list.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max


class LoanFormAdapter(
    private var arr: MutableMap<String, Int> = mutableMapOf(),
    var fbCallback: FbCallback

) : RecyclerView.Adapter<LoanFormAdapter.LoanFormViewHolder>() {
    private var convertedMap = arr.toList()
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: LoanFormViewHolder, position: Int) {
        holder.plusBtn.id = position
        holder.minusBtn.id = position
        val productName = convertedMap[position].first
        holder.prodName.text = productName
        holder.counter.id = position


        //Set all products to 0
        mutableList.apply {
            this[productName] = 0
        }

        holder.minusBtn.setOnTouchListener(
            AutoRepeatButton(400, 100,
                View.OnClickListener { minusCounter(holder) })
        )

        holder.plusBtn.setOnTouchListener(
            AutoRepeatButton(400, 100,
                View.OnClickListener { plusCounter(holder) })
        )
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun plusCounter(holder: LoanFormViewHolder) {
        val position = holder.plusBtn.id
        val product = holder.prodName.text.toString()
        var count : Int = holder.counter.text.toString().toInt()
        val maxQty : Int = convertedMap[position].second
        if(count == maxQty){
            count = maxQty
            Utils.toast(holder.itemView.context, "Maximum quantity has been exceeded for $product", 0);
        }else{
            count++
        }
        holder.counter.text = count.toString()
        mutableList[product] = count
        fbCallback.pushForLoanForm(mutableList)
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
        fbCallback.pushForLoanForm(mutableList)
    }
}