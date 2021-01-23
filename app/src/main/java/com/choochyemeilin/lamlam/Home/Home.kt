package com.choochyemeilin.lamlam.Home


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.Loans.Loans
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Reports.Reports
import com.choochyemeilin.lamlam.ReturnItems.MyStocks
import com.choochyemeilin.lamlam.ReturnItems.ReturnItems
import com.choochyemeilin.lamlam.Scan.Scan
import com.choochyemeilin.lamlam.Search.Search
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header.*
import java.text.DecimalFormat


class Home : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var arrayList: ArrayList<HomeItem>? = null
    private var gridView: GridView? = null
    private var languageAdapter: HomeAdapter? = null
    //private var lcg : Lcg = Lcg()

    lateinit var toggle: ActionBarDrawerToggle

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val uid = currentUser?.uid

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //Init Var

        //Navigation Bar
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)
        supportActionBar?.elevation = 0f

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mItem1 -> Toast.makeText(
                    applicationContext,
                    "My Profile",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.mItem2 -> startActivity(Intent(this, MyStocks::class.java))
                R.id.mItem3 -> logout()
            }
            true
        }

        gridView = findViewById(R.id.homeGridLayout)
        arrayList = ArrayList()
        arrayList = setDataList()
        languageAdapter = HomeAdapter(applicationContext, arrayList!!)
        gridView?.adapter = languageAdapter
        gridView?.onItemClickListener = this

        if (currentUser != null) {
            changeName()
        }
    }

    //Logout Methods
    private fun logout() {

        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun setDataList(): ArrayList<HomeItem> {
        val arrayList: ArrayList<HomeItem> = ArrayList()

        arrayList.add(HomeItem(R.drawable.qr_code, "SCAN"))
        arrayList.add(HomeItem(R.drawable.magnifier, "SEARCH"))
        arrayList.add(HomeItem(R.drawable.loan, "LOANS"))
        arrayList.add(HomeItem(R.drawable.business_report, "REPORTS"))
        arrayList.add(HomeItem(R.drawable.exchange, "RETURN ITEMS"))

        return arrayList
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> startActivity(Intent(this, Scan::class.java))
            1 -> startActivity(Intent(this, Search::class.java))
            2 -> startActivity(Intent(this, Loans::class.java))
            3 -> startActivity(Intent(this, Reports::class.java))
            4 -> startActivity(Intent(this, ReturnItems::class.java))
        }
    }


    //Navigation Drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeName() {
        //textView_drawer_name.text = "TEST"
        var userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("User")
        var query: Query = userRef.orderByChild("staffName")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dss in snapshot.children) {
                    //var name=dss.getValue().toString()
                    var cu = currentUser?.email.toString()
                    if (cu == dss.child("staffEmail").value.toString()) {

                        var role1 = dss.child("role").value.toString()
                        var name = dss.child("staffName").value.toString()


                        try {
                            if (role1 == "admin") {
                                welcome_user.text = "Welcome, " + name + "(" + role1.toUpperCase() + ")"
                                textView_drawer_name.text = name
                            } else {
                                welcome_user.text = "Welcome, " + name
                                textView_drawer_name.text = name
                            }
                        } catch (e: Exception) {
                            Utils.log("${e.message}")
                        }

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.log("${error.message}")
            }
        })
    }
}