package com.choochyemeilin.lamlam.ReturnItems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_stocks_list.view.*
import java.text.SimpleDateFormat
import java.util.*


class MyStocksAdapter(

    private var context: MutableMap<String, Int>,
    private var loanDateArr: ArrayList<String>,
    private var oldestDate : String
) : RecyclerView.Adapter<MyStocksAdapter.ViewHolder>() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var productRef: DatabaseReference = database.getReference("Products")

    //View Holder
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_stocks_list, parent, false)
        return object : ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val list = context.toList()

        holder.itemView.textView_stock_name.text = list[position].first
        holder.itemView.textView_stock_qty.text = list[position].second.toString()

        if(loanDateArr[position] == oldestDate){
            holder.itemView.textView_stock_date.text = oldestDate
        }
        else{
            holder.itemView.textView_stock_date.text = loanDateArr[position]
            holder.itemView.textView_stock_return_date.text= getRDate(holder.itemView.textView_stock_date.text.toString())
        }

        var sname=holder.itemView.textView_stock_name.text.toString()

       productRef.addValueEventListener(object : ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {

               for (dss in snapshot.children) {

                   val pname = dss.child("product_name").value
                   val image = dss.child("image").value

                   if (sname == pname) {

                       loadImage(holder, image.toString())
                   }
               }
           }

           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }
       })
    }


    override fun getItemCount(): Int {
        return context.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun loadImage(holder: ViewHolder, getImage: String) {
        var image : String
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.reference.child("products/$getImage")
        gsReference.downloadUrl.addOnSuccessListener { Uri ->
            image = Uri.toString()
            Picasso.get().load(image).into(holder.itemView.image_mystock)
            android.os.Handler().postDelayed({
                holder.itemView.mystock_progressBar.visibility = View.GONE
            }, 1000)
        }.addOnFailureListener {
            holder.itemView.image_mystock.setImageResource(R.drawable.no_image)
        }

    }


    fun getRDate(dateString: String):String {

        val cal : Calendar = Calendar.getInstance()
        var sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //pattern must match with string
        val d : Date? = sdf.parse(dateString) //Convert into Date object
        cal.time = d //Convert Date object into Calendar Object
        cal.add(Calendar.DAY_OF_YEAR, 30) // Add 30 days into date  | Tue Feb 23 21:26:36 GMT+08:00 2021  -- Sample Output
        val date = sdf.format(cal.time) //format back into the same pattern -- Sample output 2021-02-23 21:26:36
        Utils.log(date.toString())

        return date
    }
}

