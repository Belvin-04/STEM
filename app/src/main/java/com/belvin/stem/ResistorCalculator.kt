package com.belvin.stem

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.pow

class ResistorCalculator : AppCompatActivity() {
    val CAMERA_REQUEST = 1888
    val MY_CAMERA_PERMISSION_CODE = 100
    lateinit var fBand:ImageView
    lateinit var sBand:ImageView
    lateinit var tBand:ImageView
    lateinit var foBand:ImageView
    lateinit var cameraImg:ImageView
    lateinit var resistanceValue:EditText
    lateinit var toleranceSpinner:Spinner
    lateinit var calculateButton: Button
    lateinit var unitSpinner:Spinner
    lateinit var cameraBtn:Button
    var fBandNo = 0
    var sBandNo = 0
    var tBandNo = 0
    var foBandNo = 0
    var numberBand = 2
    val normalBandArray = arrayOf(R.drawable.black,R.drawable.brown,R.drawable.red,R.drawable.orange,R.drawable.yellow,R.drawable.green,R.drawable.blue,R.drawable.violet,R.drawable.grey,R.drawable.white)
    val multiplierBandArray = arrayOf(R.drawable.black,R.drawable.brown,R.drawable.red,R.drawable.orange,R.drawable.yellow,R.drawable.green,R.drawable.blue,R.drawable.violet,R.drawable.grey,R.drawable.white,R.drawable.gold,R.drawable.silver)
    val toleranceBandArray = arrayOf(R.drawable.brown,R.drawable.red,R.drawable.green,R.drawable.blue,R.drawable.violet,R.drawable.grey,R.drawable.gold,R.drawable.silver)
    val unitArray = arrayOf("Ohms","K Ohms","M Ohms","G Ohms")
    val toleranceArray = arrayOf("1%","2%","0.5%","0.25%","0.10%","0.05%","5%","10%")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resistor_calculator)

        fBand = findViewById(R.id.f_band)
        sBand = findViewById(R.id.s_band)
        tBand = findViewById(R.id.t_band)
        foBand = findViewById(R.id.fo_band)
        resistanceValue = findViewById<TextInputLayout>(R.id.resistanceValue).editText!!
        toleranceSpinner = findViewById(R.id.toleranceValue)
        calculateButton = findViewById(R.id.calculateBtn)
        unitSpinner = findViewById(R.id.unit)
        cameraBtn = findViewById(R.id.cameraBtn)
        cameraImg = findViewById(R.id.cameraImg)

        unitSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,unitArray)
        toleranceSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,toleranceArray)

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

        cameraBtn.setOnClickListener {
            openCamera()
        }

    }

    fun userCalculate(){

        if(TextUtils.isEmpty(resistanceValue.text)){
            resistanceValue.error = "Please enter value"
        }

        else{
            var resistanceValueUser = resistanceValue.text.toString().toDouble()

            if(unitSpinner.selectedItem == "K Ohms"){
                resistanceValueUser *= 1000
            }
            else if(unitSpinner.selectedItem == "M Ohms"){
                resistanceValueUser *= 1000000
            }
            else if(unitSpinner.selectedItem == "G Ohms"){
                resistanceValueUser *= 1000000000
            }

            if(resistanceValueUser > 99000000000){
                resistanceValue.error = "Value too large"
            }


            else{
                var fBandValue = (resistanceValueUser.toString()[0]).toString().toInt()
                var sBandValue = (resistanceValueUser.toString()[1]).toString().toInt()
                var multiplierBandValue = resistanceValueUser.toString().length-4
                var toleranceVal = toleranceSpinner.selectedItemPosition

                fBand.setImageResource(normalBandArray[fBandValue])
                sBand.setImageResource(normalBandArray[sBandValue])
                tBand.setImageResource(multiplierBandArray[multiplierBandValue])
                foBand.setImageResource(toleranceBandArray[toleranceVal]!!)
            }
        }
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

        if(tBandNo == 10){
            var resistanceValue = ((fBandNo*10) + sBandNo).toDouble()
            resistanceValue /= 10
            this.resistanceValue.setText(resistanceValue.toString())
            toleranceSpinner.setSelection(foBandNo)
            unitSpinner.setSelection(0)
        }
        else if(tBandNo == 11){
            var resistanceValue = ((fBandNo*10) + sBandNo).toDouble()
            resistanceValue /= 100
            this.resistanceValue.setText(resistanceValue.toString())
            toleranceSpinner.setSelection(foBandNo)
            unitSpinner.setSelection(0)

        }
        else{
            var newResistanceValue = 0.0
            var resistanceValue = ((fBandNo*10) + sBandNo).toLong()
            resistanceValue *= (10.toDouble().pow(tBandNo.toDouble())).toLong()
            this.resistanceValue.setText(resistanceValue.toString())
            toleranceSpinner.setSelection(foBandNo)
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun openCamera() {

        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.CAMERA),MY_CAMERA_PERMISSION_CODE)
        }
        else{
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,CAMERA_REQUEST)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == MY_CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,CAMERA_REQUEST)
        }
        else{
            Toast.makeText(this, "Please accept all permissions", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            val photo = data?.extras?.get("data") as Bitmap
            cameraImg.setImageBitmap(photo)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}