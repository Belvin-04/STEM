package com.belvin.stem

import com.googlecode.tesseract.android.TessBaseAPI;
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.pow

class ResistorQuiz : AppCompatActivity() {
        lateinit var fBand: ImageView
        lateinit var sBand: ImageView
        lateinit var tBand: ImageView
        lateinit var foBand: ImageView
        lateinit var resistanceValue: EditText
        lateinit var toleranceSpinner: Spinner
        lateinit var unitSpinner: Spinner
        lateinit var nextBtn: Button
        lateinit var questionNumber:TextView
        lateinit var correctAnswers:Triple<String,String,String>
        lateinit var givenAnswers:Triple<String,String,String>
        lateinit var temp:TextView
        var score = 0
        var questionNumberValue = 1
        var fBandNo = 0
        var sBandNo = 0
        var tBandNo = 0
        var foBandNo = 0
        val normalBandArray = arrayOf(R.drawable.black,R.drawable.brown,R.drawable.red,R.drawable.orange,R.drawable.yellow,R.drawable.green,R.drawable.blue,R.drawable.violet,R.drawable.grey,R.drawable.white)
        val multiplierBandArray = arrayOf(R.drawable.black,R.drawable.brown,R.drawable.red,R.drawable.orange,R.drawable.yellow,R.drawable.green,R.drawable.blue,R.drawable.violet,R.drawable.grey,R.drawable.white,R.drawable.gold,R.drawable.silver)
        val toleranceBandArray = arrayOf(R.drawable.brown,R.drawable.red,R.drawable.green,R.drawable.blue,R.drawable.violet,R.drawable.grey,R.drawable.gold,R.drawable.silver)
        val unitArray = arrayOf("Ohms","K Ohms","M Ohms","G Ohms")
        val toleranceArray = arrayOf("1%","2%","0.5%","0.25%","0.10%","0.05%","5%","10%")
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.resistor_quiz)

            

            fBand = findViewById(R.id.f_band)
            sBand = findViewById(R.id.s_band)
            tBand = findViewById(R.id.t_band)
            foBand = findViewById(R.id.fo_band)

            resistanceValue = findViewById<TextInputLayout>(R.id.resistanceValue).editText!!
            toleranceSpinner = findViewById(R.id.toleranceValue)
            unitSpinner = findViewById(R.id.unit)
            nextBtn = findViewById(R.id.nextBtn)
            questionNumber = findViewById(R.id.questionNumber)
            temp = findViewById(R.id.temp)
            unitSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,unitArray)
            toleranceSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,toleranceArray)

            generateRandomNumber()

            nextBtn.setOnClickListener {
                givenAnswers = Triple(resistanceValue.text.toString(),unitSpinner.selectedItem.toString(),toleranceSpinner.selectedItem.toString())
                questionNumberValue++
                if(questionNumberValue <= 10){
                    questionNumber.setText("${questionNumberValue}/10")
                    nextQuestion()
                    if(questionNumberValue == 10){
                        nextBtn.setText("Submit")
                    }
                }
                else{
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Result")
                        .setMessage("Your Score is ${score}/10")
                        .setPositiveButton("OK") { dialog, which ->
                            MaterialAlertDialogBuilder(this)
                                .setTitle("Want to take the test again ?")
                                .setMessage("Click YES to take the test again or click No to discard the message")
                                .setPositiveButton("YES") { dialog1, which1 ->
                                    questionNumberValue = 1
                                    questionNumber.setText("${questionNumberValue}/10")
                                    nextBtn.setText("Next")
                                    nextQuestion()
                                }
                                .setNegativeButton("NO"){ dialog1, which1 ->
                                    startActivity(Intent(this,Home::class.java))
                                }
                                .show()
                        }
                        .show()
                }
            }

        }

        fun calculate(){

            var resistance = ""
            var unit = ""
            var tolerance = ""

            if(tBandNo == 10){
                var resistanceValue = ((fBandNo*10) + sBandNo).toDouble()
                resistanceValue /= 10
                resistance = resistanceValue.toString()
                tolerance = toleranceArray[foBandNo]
                unit = unitArray[0]
            }
            else if(tBandNo == 11){
                var resistanceValue = ((fBandNo*10) + sBandNo).toDouble()
                resistanceValue /= 100
                resistance = resistanceValue.toString()
                tolerance = toleranceArray[foBandNo]
                unit = unitArray[0]

            }
            else{

                var newResistanceValue = 0.0
                var resistanceValue = ((fBandNo*10) + sBandNo).toLong()
                resistanceValue *= (10.toDouble().pow(tBandNo.toDouble())).toLong()
                resistance = resistanceValue.toString()
                tolerance = toleranceArray[foBandNo]
                unit = unitArray[0]
                if(resistanceValue.toString().length > 3) {
                    if (resistanceValue / (10.toDouble().pow(9)) >= 1) {
                        newResistanceValue = resistanceValue/(10.toDouble().pow(9))
                        unit = unitArray[3]
                    } else if (resistanceValue / (10.toDouble().pow(6)) >= 1) {
                        newResistanceValue = resistanceValue/(10.toDouble().pow(6))
                        unit = unitArray[2]
                    }else if(resistanceValue / (10.toDouble().pow(3)) >= 1){
                        newResistanceValue = resistanceValue/(10.toDouble().pow(3))
                        unit = unitArray[1]
                    }
                    resistance = newResistanceValue.toString()
                }
            }

            correctAnswers = Triple(resistance,unit,tolerance)
        }

    fun generateRandomNumber(){
        fBandNo = (normalBandArray.indices).random()
        sBandNo = (normalBandArray.indices).random()
        tBandNo = (multiplierBandArray.indices).random()
        foBandNo = (toleranceBandArray.indices).random()

        fBand.setImageResource(normalBandArray[fBandNo])
        sBand.setImageResource(normalBandArray[sBandNo])
        tBand.setImageResource(multiplierBandArray[tBandNo])
        foBand.setImageResource(toleranceBandArray[foBandNo])

        calculate()
        temp.setText(correctAnswers.toString())


    }

    fun nextQuestion(){
        resistanceValue.setText("")
        unitSpinner.setSelection(0)
        toleranceSpinner.setSelection(0)

        if((correctAnswers == givenAnswers) && (correctAnswers.first.isNotEmpty() && correctAnswers.second.isNotEmpty() && correctAnswers.third.isNotEmpty()) && (givenAnswers.first.isNotEmpty() && givenAnswers.second.isNotEmpty() && givenAnswers.third.isNotEmpty())){
           score++

        }
        generateRandomNumber()
    }
}