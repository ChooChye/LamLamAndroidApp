package com.choochyemeilin.lamlam.helpers

import java.sql.Date

class Products {
    var id : String? = null
    var category : String? = null
    var product_name : String? = null
    var desc : String? = null
    var price : String = ""
    var qty : String = ""
    var status:String?=null
    var loanDate:Date?=null
    var returnDate:Date?=null

    constructor(){

    }

    constructor(
        category: String?,
        id: String?,
        product_name: String?,
        desc: String?,
        price: String,
        qty: String,
        status: String,
        loanDate: Date,
        returnDate: Date) {

        this.category = category
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