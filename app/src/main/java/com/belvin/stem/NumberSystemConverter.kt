package com.belvin.stem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlin.math.pow

class NumberSystemConverter : AppCompatActivity() {
    lateinit var decimalValue:EditText
    lateinit var binaryValue:EditText
    lateinit var octalValue:EditText
    lateinit var hexDecValue:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.number_system_converter)

        decimalValue = findViewById(R.id.decimalValue)
        binaryValue = findViewById(R.id.binaryValue)
        octalValue = findViewById(R.id.octalValue)
        hexDecValue = findViewById(R.id.hexDecValue)

        /*decimalValue.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){
                    binaryValue.setText("")
                    octalValue.setText("")
                    hexDecValue.setText("")
                    return
                }
                binaryValue.setText(decimalToBinary(s.toString().toLong()))
                octalValue.setText(decimalToOctal(s.toString().toLong()))
                hexDecValue.setText(decimalToHEX(s.toString().toLong()))

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        binaryValue.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){
                    decimalValue.setText("")
                    octalValue.setText("")
                    hexDecValue.setText("")
                    return
                }

                decimalValue.setText(binaryToDecimal(s.toString()))
                octalValue.setText(decimalToOctal(decimalValue.text.toString().toLong()))
                hexDecValue.setText(decimalToHEX(decimalValue.text.toString().toLong()))

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        octalValue.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){
                    binaryValue.setText("")
                    decimalValue.setText("")
                    hexDecValue.setText("")
                    return
                }
                decimalValue.setText(octalToDecimal(s.toString()))
                binaryValue.setText(decimalToBinary(decimalValue.text.toString().toLong()))
                hexDecValue.setText(decimalToHEX(decimalValue.text.toString().toLong()))

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })*/

        hexDecValue.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){
                    binaryValue.setText("")
                    octalValue.setText("")
                    decimalValue.setText("")
                    return
                }
                decimalValue.setText(hexToDecimal(s.toString()))
                binaryValue.setText(decimalToBinary(decimalValue.text.toString().toLong()))
                octalValue.setText(decimalToOctal(decimalValue.text.toString().toLong()))
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    fun decimalToBinary(num:Long):String{
        var n = num
        val binaryNum = mutableListOf<Long>()
        var binaryAns = ""

        for(i in 0..31){

            if(n == 1L || n == 0L){
                binaryNum.add(n)
                break
            }
            binaryNum.add(n%2)
            n /= 2
        }

        for(i in (binaryNum.size -1) downTo (0)){
            binaryAns+=(binaryNum[i].toString())
        }

        return binaryAns
    }

    fun decimalToOctal(num: Long):String{
        var n = num
        val octalNum = mutableListOf<Long>()
        var octalAns = ""

        for(i in 0..31){

            if(n == 1L || n == 0L){
                octalNum.add(n)
                break
            }
            octalNum.add(n%8)
            n /= 8
        }

        for(i in (octalNum.size -1) downTo (0)){
            octalAns+=(octalNum[i].toString())
        }

        return octalAns

    }

    fun decimalToHEX(num:Long):String{
        var n = num
        val hexNum = mutableListOf<Any>()
        var hexAns = ""

        for(i in 0..31){

            if(n == 1L || n == 0L){
                hexNum.add(n)
                break
            }
            if(n%16 == 10L){
                hexNum.add("A")
            }
            else if(n%16 == 11L){
                hexNum.add("B")
            }
            else if(n%16 == 12L){
                hexNum.add("C")
            }
            else if(n%16 == 13L){
                hexNum.add("D")
            }
            else if(n%16 == 14L){
                hexNum.add("E")
            }
            else if(n%16 == 15L){
                hexNum.add("F")
            }
            else{
                hexNum.add(n%16)
            }
            n /= 16
        }

        for(i in (hexNum.size -1) downTo (0)){
            hexAns+=(hexNum[i].toString())
        }

        return hexAns
    }

    fun binaryToDecimal(num:String):String{
        var sum = 0
        var binaryArray = convertToList(num).asReversed()

        for(i in binaryArray.size-1 downTo 0){
            binaryArray[i] = ((binaryArray[i].toInt()) * (2.toDouble().pow(i)).toInt()).toString()
            sum += (binaryArray[i]).toInt()
        }

        return (sum.toString())
    }

    fun octalToDecimal(num:String):String{
        var sum = 0
        var octalArray = convertToList(num).asReversed()

        for(i in octalArray.size-1 downTo 0){
            octalArray[i] = ((octalArray[i].toInt()) * (8.toDouble().pow(i)).toInt()).toString()
            sum += (octalArray[i]).toInt()
        }

        return (sum.toString())
    }

    fun hexToDecimal(num:String):String{
        var sum = 0
        var hexDecArray = convertToList(num).asReversed()

        for(i in 0 until hexDecArray.size){
            if(hexDecArray[i] == "A" || hexDecArray[i] == "a"){
                hexDecArray[i] = "10"
            }
            else if(hexDecArray[i] == "B" || hexDecArray[i] == "b"){
                hexDecArray[i] = "11"
            }
            else if(hexDecArray[i] == "C" || hexDecArray[i] == "c"){
                hexDecArray[i] = "12"
            }
            else if(hexDecArray[i] == "D" || hexDecArray[i] == "d"){
                hexDecArray[i] = "13"
            }
            else if(hexDecArray[i] == "E" || hexDecArray[i] == "e"){
                hexDecArray[i] = "14"
            }
            else if(hexDecArray[i] == "F" || hexDecArray[i] == "f"){
                hexDecArray[i] = "15"
            }
        }

        for(i in hexDecArray.size-1 downTo 0){
            hexDecArray[i] = ((hexDecArray[i].toInt()) * (16.toDouble().pow(i)).toInt()).toString()
            sum += (hexDecArray[i]).toInt()
        }

        return (sum.toString())
    }

    fun convertToList(chars:String):MutableList<String>{
        var tempArray = mutableListOf<String>()
        for(c in chars){
            tempArray.add(c.toString())
        }
        return  tempArray
    }
}