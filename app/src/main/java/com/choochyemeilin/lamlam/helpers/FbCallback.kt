package com.choochyemeilin.lamlam.helpers

import com.choochyemeilin.lamlam.Loans.Classes.LoanApplication
import com.choochyemeilin.lamlam.Loans.Classes.SelectedProducts
import com.choochyemeilin.lamlam.Reports.objects.ReportsObject

interface FbCallback {
    fun onCallbackString(arr : ArrayList<String>){}
    fun onCallbackSelectedProducts(arr : ArrayList<SelectedProducts>){}
    fun onCallbackReports(arr : ArrayList<ReportsObject>){}
    fun onCallback(arr : ArrayList<LoanApplication>){}
    fun push(arr : MutableMap<String, Int>){}
}