package com.choochyemeilin.lamlam.helpers

import com.choochyemeilin.lamlam.Loans.LoanApplication

interface FbCallback {
    fun onCallback(arr : ArrayList<LoanApplication>){

    }
}