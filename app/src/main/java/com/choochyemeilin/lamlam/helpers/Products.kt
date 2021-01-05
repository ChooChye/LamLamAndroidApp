package com.choochyemeilin.lamlam.helpers

import java.sql.Date

class Products {
    var id: String? = null
    var category: String? = null
    var product_name: String? = null
    var desc: String? = null
    var price: String = ""
    var qty: String = ""
    var status: String? = null
    var loanDate: String? = null
    var returnDate: String? = null
    var image:String?=null

    constructor() {

    }

    constructor(
        id: String?,
        category: String?,
        product_name: String?,
        desc: String?,
        price: String,
        qty: String,
        status: String?,
        loanDate: String?,
        returnDate: String?,
        image:String?
    ) {
        this.id = id
        this.category = category
        this.product_name = product_name
        this.desc = desc
        this.price = price
        this.qty = qty
        this.status = status
        this.loanDate = loanDate
        this.returnDate = returnDate
        this.image=image
    }


}