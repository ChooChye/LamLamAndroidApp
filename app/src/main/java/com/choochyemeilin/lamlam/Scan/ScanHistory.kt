package com.choochyemeilin.lamlam.Scan

class ScanHistory{
    var id : String ?= null
    private var Category : String ?= null
    private var Product : String ?= null


    override fun toString(): String {
        return "$id | $Category | $Product"
    }

    fun getID(): String{
        return "$id"
    }
    fun getCategory(): String{
        return "$Category"
    }
    fun getProduct(): String{
        return "$Product"
    }



}