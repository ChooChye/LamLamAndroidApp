package com.choochyemeilin.lamlam.helpers

class Products {
    var id : String? = null
    var product_name : String? = null
    var desc : String? = null
    var price : String = "0.00"
    var qty : String = "0"

    constructor(){

    }

    constructor(id: String?, product_name: String?, desc: String?, price: String, qty: String) {
        this.id = id
        this.product_name = product_name
        this.desc = desc
        this.price = price
        this.qty = qty
    }




}