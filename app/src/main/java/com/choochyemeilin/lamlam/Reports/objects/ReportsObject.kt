package com.choochyemeilin.lamlam.Reports.objects

class ReportsObject {
    private var productName: String ? = null
    private var qty : Int ? = null

    constructor(productName: String?, qty: Int?) {
        this.productName = productName
        this.qty = qty
    }
}