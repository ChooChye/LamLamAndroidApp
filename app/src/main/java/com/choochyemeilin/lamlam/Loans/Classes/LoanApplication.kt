package com.choochyemeilin.lamlam.Loans.Classes

class LoanApplication {
    var loanID : Int? = 0
    var loanDate : String? = null
    var expiryLoanDate : String? = null
    var productName : MutableMap<String, Int> = mutableMapOf()
    var status : String = "pending"
    var dateStatus : String? = null

    constructor(
        loan_id: Int?,
        loanDate: String?,
        expiryLoanDate: String?,
        product_name: MutableMap<String, Int>,
        status: String,
        date_status: String?
    ) {
        this.loanID = loan_id
        this.loanDate = loanDate
        this.expiryLoanDate = expiryLoanDate
        this.productName = product_name
        this.status = status
        this.dateStatus = date_status
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

    constructor(
        loan_id: Int?,
        loanDate: String?,
        status: String
    ) {
        this.loanID = loan_id
        this.loanDate = loanDate
        this.status = status
    }
}
