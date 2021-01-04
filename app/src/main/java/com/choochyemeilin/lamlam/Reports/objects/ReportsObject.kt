package com.choochyemeilin.lamlam.Reports.objects

class ReportsObject {
    var productName: String ? = null
    var qty : Int ? = null

    constructor(productName: String?, qty: Int?) {
        this.productName = productName
        this.qty = qty
    }
}