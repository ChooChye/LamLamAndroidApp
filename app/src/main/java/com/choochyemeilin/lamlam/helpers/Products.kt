package com.choochyemeilin.lamlam.helpers

import java.sql.Date

class Products {
    var id : String? = null
    var product_name : String? = null
    var desc : String? = null
    var price : String = "0.00"
    var qty : String = "0"
    var status:String?=null
    var loanDate:Date?=null
    var returnDate:Date?=null

    constructor(){

    }

    constructor(id: String?, product_name: String?, desc: String?, price: String, qty: String, status: String, loanDate: Date,returnDate: Date) {
        this.id = id
        this.product_name = product_name
        this.desc = desc
        this.price = price
        this.qty = qty
        this.status=status
        this.loanDate=loanDate
        this.returnDate=returnDate
    }




}