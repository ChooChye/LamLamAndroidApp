package com.choochyemeilin.lamlam.Loans.Classes

class LoanApplication {
    var loanID : Int? = 0
    var loanDate : String? = null
    var expiryLoanDate : String? = null
    var productName : MutableMap<String, Int> = mutableMapOf()
    var status : String = "pending"
    var dateStatus : String? = null
    var staffID : Int? = null
    var retailerID : Int? = 0

    constructor(
        loan_id: Int?,
        loanDate: String?,
        status: String,
        product_name: MutableMap<String, Int>,
        staffID : Int,
        retailerID : Int
    ) {
        this.loanID = loan_id
        this.loanDate = loanDate
        this.status = status
        this.productName = product_name
        this.staffID = staffID
        this.retailerID = retailerID
    }

    constructor(
        loan_id: Int?,
        loanDate: String?,
        status: String
    ) {
        this.loanID = loan_id
        this.loanDate = loanDate
        this.status = status

    }

    constructor(
        loan_id: Int?,
        loanDate: String?,
        status: String,
        product_name: MutableMap<String, Int>
    ) {
        this.loanID = loan_id
        this.loanDate = loanDate
        this.status = status
        this.productName = product_name
    }
}
