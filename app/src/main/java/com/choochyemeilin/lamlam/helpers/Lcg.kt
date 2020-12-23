package com.choochyemeilin.lamlam.helpers

import android.util.Log
import kotlin.experimental.xor

class Lcg {
    private var modulus : Double       = 0.0
    private var multiplier : Double    = 0.0
    private var increment : Double     = 0.0
    private var seed: Double           = 0.0

    constructor(){
        modulus     = 256.0
        multiplier  = 11.0
        increment   = 1.0
        seed        = 0.0
    }

    fun next(): Double{
        // Y = (a.X + c) mod m

        val value : Double = (this.multiplier * this.seed) + this.increment
        this.seed = value % this.modulus
        return this.seed
    }

    private fun decrypt(encryptedValue: String){

    }

    fun unpack(plaintext : String): MutableList<Byte> {
        var arrayChars:MutableList<Byte> = mutableListOf()

        val splitChar = plaintext.toCharArray()
        var i = 0
        while(i < splitChar.size){
            try {
                val charToInt = splitChar[i].toInt()
                val binary = String.format("%8s", Integer.toBinaryString(charToInt)).replace(' ', '0')
                arrayChars.add(i, splitChar[i].toByte() xor this.next().toInt().toByte()) // Shift Bytes and place them into a new array
                i++
            }catch (e: ArrayIndexOutOfBoundsException){
                Log.d("ERROR", "${e.message}")
            }
        }
        return arrayChars
    }

    fun toBinary(splitChar: CharArray): Int {
        val i = 0
        var sum = 0
        while(i < splitChar.size){
            val charToInt = splitChar[i].toInt()
            val binary = String.format("%8s", Integer.toBinaryString(charToInt)).replace(' ', '0')
            sum += binary.length
        }
        return sum
    }
}