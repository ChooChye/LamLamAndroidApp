package com.choochyemeilin.lamlam.Scan.ScanHistory

class ScanHistoryObj {
    var date: String = ""
    var time: String = ""
    var product_name: String = ""
    var staffID: Int = 0
    var scannedQty : Int = 0
    var image : String = ""

    constructor()

    constructor(date: String, time: String, prodName: String, staffID: Int, scannedQty : Int, image : String) {
        this.date = date
        this.time = time
        this.product_name = prodName
        this.staffID = staffID
        this.scannedQty = scannedQty
        this.image = image
    }

    override fun toString(): String {
        return "[${date}  ${time}, ${product_name},  ${scannedQty},  ${staffID}]"
    }

}