package com.choochyemeilin.lamlam.Register

class Staff {

    var staffID= 0
    var staffName=""
    var staffEmail=""
    var phoneNumber=0
    var pw=""
    var retailerName=""
    var retailerAddress=""

    constructor( staffID:Int, staffName: String, staffEmail:String, phoneNumber:Int,pw:String){

        this.staffID=staffID
        this.staffName=staffName
        this.staffEmail=staffEmail
        this.phoneNumber=phoneNumber
        this.pw=pw

    }

    constructor(retailerName:String,retailerAddress:String){
        this.retailerName=retailerName

        this.retailerAddress=retailerAddress
    }
}