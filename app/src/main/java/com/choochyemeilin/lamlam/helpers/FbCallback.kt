package com.choochyemeilin.lamlam.helpers


import com.choochyemeilin.lamlam.Loans.Classes.LoanApplication
import com.choochyemeilin.lamlam.Scan.ScanHistory.ScanHistoryObj


interface FbCallback {
    fun onCallbackString(arr : ArrayList<String>){}
    fun onCallbackRetailer(arr : ArrayList<Retailers>){}
    fun onCallbackGetUserEmail(user : String){}
    fun onCallbackGetUserID(uid : Int){}
    fun onCallback(arr : ArrayList<LoanApplication>){}
    fun push(arr : MutableMap<String, Int>){}
    fun pushForLoanForm(arr : MutableMap<String, Int>){}
    fun profileCallback(arr : MutableMap<String, String>){}
    fun scanHistoryCallback(arr : MutableList<ScanHistoryObj>){}

    fun pushLoanDate(arr : MutableMap<String, Int>, loanDate: ArrayList<String>, oldestDate : String){}
    fun scanHistoryArrCallback(arr : ArrayList<ScanHistoryObj>){}
    fun stockCountCallback(map : MutableMap<String, Int>, arr : ArrayList<ScanHistoryObj>){}
    fun storeArr(arr : ArrayList<ScanHistoryObj>){}
    fun onCallbackGetNotificationInfo(msg : String){}
}