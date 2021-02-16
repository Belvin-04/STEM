package com.belvin.stem

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    lateinit var fBand:ImageView
    lateinit var sBand:ImageView
    lateinit var tBand:ImageView
    lateinit var foBand:ImageView
    lateinit var resistanceValue:EditText
    lateinit var toleranceValue:EditText
    lateinit var calculateButton: Button
    lateinit var unitSpinner:Spinner
    var fBandNo = 0
    var sBandNo = 0
    var tBandNo = 0
    var foBandNo = 0
    var numberBand = 2
    val normalBandArray = arrayOf(R.drawable.black,R.drawable.brown,R.drawable.red,R.drawable.orange,R.drawable.yellow,R.drawable.green,R.drawable.blue,R.drawable.violet,R.drawable.grey,R.drawable.white)
    val multiplierBandArray = arrayOf(R.drawable.black,R.drawable.brown,R.drawable.red,R.drawable.orange,R.drawable.yellow,R.drawable.green,R.drawable.blue,R.drawable.violet,R.drawable.grey,R.drawable.white,R.drawable.gold,R.drawable.silver)
    val toleranceBandArray = arrayOf(R.drawable.brown,R.drawable.red,R.drawable.green,R.drawable.blue,R.drawable.violet,R.drawable.grey,R.drawable.gold,R.drawable.silver)
    val unitArray = arrayOf("Ohms","K Ohms","M Ohms","G Ohms")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fBand = findViewById(R.id.f_band)
        sBand = findViewById(R.id.s_band)
        tBand = findViewById(R.id.t_band)
        foBand = findViewById(R.id.fo_band)
        resistanceValue = findViewById(R.id.resistanceValue)
        toleranceValue = findViewById(R.id.toleranceValue)
        calculateButton = findViewById(R.id.calculateBtn)
        unitSpinner = findViewById(R.id.unit)

        unitSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,unitArray)

        fBand.setOnClickListener {
            fBandNo = changeBand(fBand,fBandNo,normalBandArray)
            calculate()
        }
        sBand.setOnClickListener {
            sBandNo = changeBand(sBand,sBandNo,normalBandArray)
            calculate()
        }
        tBand.setOnClickListener {
            tBandNo = changeBand(tBand,tBandNo,multiplierBandArray)
            calculate()
        }
        foBand.setOnClickListener {
            foBandNo = changeBand(foBand,foBandNo,toleranceBandArray)
            calculate()
        }

        calculateButton.setOnClickListener {
            userCalculate()
        }

    }

    fun userCalculate(){

        var resistanceValueUser = resistanceValue.text.toString().toLong()

        if(unitSpinner.selectedItem == "K Ohms"){
            resistanceValueUser *= 1000
        }
        else if(unitSpinner.selectedItem == "M Ohms"){
            resistanceValueUser *= 1000000
        }
        else if(unitSpinner.selectedItem == "G Ohms"){
            resistanceValueUser *= 1000000000
        }

        var fBandValue = (resistanceValueUser.toString()[0]).toString().toInt()
        var sBandValue = (resistanceValueUser.toString()[1]).toString().toInt()
        var multiplierBandValue = resistanceValueUser.toString().length-2
        var toleranceVal = toleranceValue.text.toString()
        

        val toleranceMap = mapOf<String,Int>(Pair("1%",0),Pair("2%",1),Pair("0.5%",2),Pair("0.25%",3),Pair("0.10%",4),Pair("0.05%",5),Pair("5%",6),Pair("10%",7))

        fBand.setImageResource(normalBandArray[fBandValue])
        sBand.setImageResource(normalBandArray[sBandValue])
        tBand.setImageResource(multiplierBandArray[multiplierBandValue])
        foBand.setImageResource(toleranceBandArray[toleranceMap[toleranceVal]!!])
    }

    fun changeBand(band:ImageView, number:Int, resList:Array<Int>):Int{
        var n = number
        if(n >= (resList.size-1)){
            n = 0
        }
        else{
            n += 1
        }
        band.setImageResource(resList[n])
        return n
    }
    fun calculate(){

        val toleranceMap = mapOf(Pair(0,"1%"),Pair(1,"2%"),Pair(2,"0.5%"),Pair(3,"0.25%"),Pair(4,"0.10%"),Pair(5,"0.05%"),Pair(6,"5%"),Pair(7,"10%"))
        if(tBandNo == 10){
            var resistanceValue = ((fBandNo*10) + sBandNo).toDouble()
            resistanceValue /= 10
            this.resistanceValue.setText(resistanceValue.toString())
            toleranceValue.setText(toleranceMap[foBandNo])
            unitSpinner.setSelection(0)
        }
        else if(tBandNo == 11){
            var resistanceValue = ((fBandNo*10) + sBandNo).toDouble()
            resistanceValue /= 100
            this.resistanceValue.setText(resistanceValue.toString())
            toleranceValue.setText(toleranceMap[foBandNo])
            unitSpinner.setSelection(0)

        }
        else{
            var newResistanceValue = 0.0
            var resistanceValue = ((fBandNo*10) + sBandNo).toLong()
            resistanceValue *= (10.toDouble().pow(tBandNo.toDouble())).toLong()
            this.resistanceValue.setText(resistanceValue.toString())
            toleranceValue.setText(toleranceMap[foBandNo])
            unitSpinner.setSelection(0)
            if(resistanceValue.toString().length > 3) {
                if (resistanceValue / (10.toDouble().pow(9)) >= 1) {
                    newResistanceValue = resistanceValue/(10.toDouble().pow(9))
                    unitSpinner.setSelection(3)
                } else if (resistanceValue / (10.toDouble().pow(6)) >= 1) {
                    newResistanceValue = resistanceValue/(10.toDouble().pow(6))
                    unitSpinner.setSelection(2)
                }else if(resistanceValue / (10.toDouble().pow(3)) >= 1){
                    newResistanceValue = resistanceValue/(10.toDouble().pow(3))
                    unitSpinner.setSelection(1)
                }
                this.resistanceValue.setText(newResistanceValue.toString())
            }
        }
    }
}