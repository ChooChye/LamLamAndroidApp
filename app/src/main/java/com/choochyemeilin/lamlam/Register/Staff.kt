package com.choochyemeilin.lamlam.Register

class Staff {

    var staffID= 0
    var staffName=""
    var staffEmail=""
    var phoneNumber=0
    var pw=""
    var retailerID=0
    var role="staff"

    constructor( staffID:Int, staffName: String, staffEmail:String, phoneNumber:Int,pw:String
                 ,retailerID:Int){

        this.staffID=staffID
        this.staffName=staffName
        this.staffEmail=staffEmail
        this.phoneNumber=phoneNumber
        this.pw=pw
        this.retailerID=retailerID

    }

}