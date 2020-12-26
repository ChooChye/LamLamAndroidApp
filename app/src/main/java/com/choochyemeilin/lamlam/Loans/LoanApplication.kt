package com.choochyemeilin.lamlam.Loans

class LoanApplication {
    var loanID : Int? = 0
    var loanDate : String? = null
    var expiryLoanDate : String? = null
    var productName : ArrayList<String> = ArrayList()
    var status : String = "pending"
    var dateStatus : String? = null

    constructor(
        loan_id: Int?,
        loanDate: String?,
        expiryLoanDate: String?,
        product_name: ArrayList<String>,
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
        product_name: ArrayList<String>
    ) {
        this.loanID = loan_id
        this.loanDate = loanDate
        this.productName = product_name
    }

}
