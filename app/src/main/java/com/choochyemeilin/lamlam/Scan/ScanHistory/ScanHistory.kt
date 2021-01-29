package com.choochyemeilin.lamlam.Scan.ScanHistory


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_scan_history.*


class ScanHistory : AppCompatActivity() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("ScanHistory")
    private lateinit var viewAdapter: ScanHistoryAdapter
    private lateinit var deleteIcon : Drawable
    private var swipeBackground : ColorDrawable = ColorDrawable(Color.parseColor("#E76976"))

    private var currentStaffID = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Scan History")
        supportActionBar?.elevation = 0f

        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_24)!!

        Utils.getStaffID(object : FbCallback {
            override fun onCallbackGetUserID(uid: Int) {
                currentStaffID = uid
                super.onCallbackGetUserID(uid)
            }
        })

        setupData(object : FbCallback {
            override fun scanHistoryCallback(arr: MutableList<ScanHistoryObj>) {

                var newArr : MutableList<ScanHistoryObj> = mutableListOf()
                var index = 0
                arr.forEach {
                    if(it.staffID == currentStaffID){
                        newArr.add(it)
                    }
                    index++
                }
                arr.reverse()
                if(newArr.isEmpty()){
                    scanHistory_rv.visibility = View.GONE
                    scan_history_items_tv_noresult.visibility = View.VISIBLE
                }else{
                    viewAdapter = ScanHistoryAdapter(newArr)
                    scanHistory_rv.apply {
                        adapter = viewAdapter
                        layoutManager = LinearLayoutManager(applicationContext)
                        addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
                    }
                }
            }
        })

        val itemtouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                viewAdapter.removeItem(viewHolder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if(dX < 0){
                    //Right to Left
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right - iconMargin, itemView.bottom - iconMargin)
                }

                swipeBackground.draw(c)
                c.save()
                if(dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                deleteIcon.draw(c)
                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemtouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(scanHistory_rv)

    }

    private fun setupData(fbCallback: FbCallback){
        var data : MutableList<ScanHistoryObj> = mutableListOf()
        data.clear()
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    dss.children.forEach { jt ->
                        jt.children.forEach {

                            val productName = it.child("product_name").value
                            val time = jt.key
                            val date = dss.key
                            val dbStaffID = it.child("staffID").value.toString().toInt()
                            data.add(ScanHistoryObj(date.toString(), time.toString(), productName.toString(), dbStaffID))
                        }
                    }
                }
                fbCallback.scanHistoryCallback(data)
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}