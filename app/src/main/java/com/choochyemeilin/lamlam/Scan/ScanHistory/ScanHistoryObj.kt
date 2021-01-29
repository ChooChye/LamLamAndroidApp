package com.choochyemeilin.lamlam.Scan.ScanHistory

class ScanHistoryObj {
    var date: String = ""
    var time: String = ""
    var prodName: String = ""
    var staffID: Int = 0

    constructor(date: String, time: String, prodName: String, staffID: Int) {
        this.date = date
        this.time = time
        this.prodName = prodName
        this.staffID = staffID
    }

}