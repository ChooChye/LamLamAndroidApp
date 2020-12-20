package com.choochyemeilin.lamlam.helpers

class Products {
    var id : String? = null
    var category : String? = null
    var product_name : String? = null
    var desc : String? = null
    var price : String = ""
    var qty : String = ""

    constructor(){

    }

    constructor(
        category: String?,
        id: String?,
        product_name: String?,
        desc: String?,
        price: String,
        qty: String
    ) {
        this.category = category
        this.id = id
        this.product_name = product_name
        this.desc = desc
        this.price = price
        this.qty = qty
    }


}