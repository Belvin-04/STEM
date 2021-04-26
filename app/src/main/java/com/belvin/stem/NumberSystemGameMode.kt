package com.belvin.stem

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import org.opencv.core.Scalar
import kotlin.math.pow


class NumberSystemGameMode : AppCompatActivity() {
    lateinit var decimalValue: EditText
    lateinit var binaryValue: EditText
    lateinit var octalValue: EditText
    lateinit var hexDecValue: EditText
    lateinit var questionNumber:TextView
    lateinit var nextButton:Button
    var correctAnswers = mutableListOf<String>()
    var givenAnswers = mutableListOf<String>()
    var questionNumberValue = 1
    var score = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.number_system_game_mode)

        decimalValue = findViewById<TextInputLayout>(R.id.decimalValue1).editText!!
        binaryValue = findViewById<TextInputLayout>(R.id.binaryValue1).editText!!
        octalValue = findViewById<TextInputLayout>(R.id.octalValue1).editText!!
        hexDecValue = findViewById<TextInputLayout>(R.id.hexDecValue1).editText!!
        questionNumber = findViewById(R.id.questionNumber)
        nextButton = findViewById(R.id.nextButton)
        generateRandomNumber()

        nextButton.setOnClickListener {

            givenAnswers.add(decimalValue.text.toString())
            givenAnswers.add(binaryValue.text.toString())
            givenAnswers.add(octalValue.text.toString())
            givenAnswers.add(hexDecValue.text.toString())

            questionNumberValue++
            if(questionNumberValue <= 10){
                questionNumber.setText("${questionNumberValue}/10")
                nextQuestion()
                if(questionNumberValue == 10){
                    nextButton.setText("Submit")
                }
            }
            else{
                MaterialAlertDialogBuilder(this)
                    .setTitle("Result")
                    .setMessage("Your Score is ${score}/10")
                    .setPositiveButton("OK") { dialog, which ->
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Want to take the test again ?")
                            .setMessage(getMessage(getQuizScore(),score))
                            .setPositiveButton("YES") { dialog1, which1 ->
                                if(score > getQuizScore()){
                                    updateQuizScore(score)
                                }
                                score = 0
                                questionNumberValue = 1
                                questionNumber.setText("${questionNumberValue}/10")
                                nextButton.setText("Next")
                                nextQuestion()
                            }
                            .setNegativeButton("NO"){ dialog1, which1 ->
                                if(score > getQuizScore()){
                                    updateQuizScore(score)
                                }
                                startActivity(Intent(this, Home::class.java))
                            }
                            .show()
                    }
                    .show()
            }
        }
    }

    private fun getMessage(previousScore:Int,currentScore:Int): String {
        var message = ""
        if(currentScore == 10 && previousScore == 10){
            message = "Keep up the 10 streak"
        }
        else if(currentScore == 10){
            message = "A perfect 10"
        }
        else if(currentScore < previousScore){
            message = "Looks like you have not being studying lately..."
        }
        else if(currentScore > previousScore){
            message = "HardWork definitely pays off..."
        }
        else{
            message = "Keep working hard..."
        }
        message = message + "\nPress Yes to retake the quiz or press No to go to the home page."
        return message
    }

    fun nextQuestion(){
        decimalValue.setText("")
        binaryValue.setText("")
        octalValue.setText("")
        hexDecValue.setText("")
        if((correctAnswers.isNotEmpty() && givenAnswers.isNotEmpty()) && correctAnswers == givenAnswers){
            score++
        }
        generateRandomNumber()
    }

    fun generateRandomNumber(){
        var randomChoice = (1..4).random()
        var randomNumber = (0..999999).random()
        correctAnswers.add(randomNumber.toString())
        correctAnswers.add(decimalToBinary(randomNumber.toLong()))
        correctAnswers.add(decimalToOctal(randomNumber.toLong()))
        correctAnswers.add(decimalToHEX(randomNumber.toLong()))
        when(randomChoice){
            1 -> {
                decimalValue.setText(randomNumber.toString())
            }
            2 -> {
                binaryValue.setText(decimalToBinary(randomNumber.toLong()))
            }
            3 -> {
                octalValue.setText(decimalToOctal(randomNumber.toLong()))
            }
            4 -> {
                hexDecValue.setText(decimalToHEX(randomNumber.toLong()))
            }
        }
    }

    fun decimalToBinary(num: Long):String{
        var n = num
        val binaryNum = mutableListOf<Long>()
        var binaryAns = ""

        for(i in 0..31){

            if(n == 1L || n == 0L){
                binaryNum.add(n)
                break
            }
            binaryNum.add(n % 2)
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
            octalNum.add(n % 8)
            n /= 8
        }

        for(i in (octalNum.size -1) downTo (0)){
            octalAns+=(octalNum[i].toString())
        }

        return octalAns

    }

    fun decimalToHEX(num: Long):String{
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
                hexNum.add(n % 16)
            }
            n /= 16
        }

        for(i in (hexNum.size -1) downTo (0)){
            hexAns+=(hexNum[i].toString())
        }

        return hexAns
    }

    fun binaryToDecimal(num: String):String{
        var sum = 0
        var binaryArray = convertToList(num).asReversed()

        for(i in binaryArray.size-1 downTo 0){
            binaryArray[i] = ((binaryArray[i].toInt()) * (2.toDouble().pow(i)).toInt()).toString()
            sum += (binaryArray[i]).toInt()
        }

        return (sum.toString())
    }

    fun octalToDecimal(num: String):String{
        var sum = 0
        var octalArray = convertToList(num).asReversed()

        for(i in octalArray.size-1 downTo 0){
            octalArray[i] = ((octalArray[i].toInt()) * (8.toDouble().pow(i)).toInt()).toString()
            sum += (octalArray[i]).toInt()
        }

        return (sum.toString())
    }

    fun hexToDecimal(num: String):String{
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

    fun convertToList(chars: String):MutableList<String>{
        var tempArray = mutableListOf<String>()
        for(c in chars){
            tempArray.add(c.toString())
        }
        return  tempArray
    }

    fun updateQuizScore(score:Int){
        var db = STEMDatabase(this).readableDatabase
        var cv = ContentValues()
        cv.put("numberSystemScore",score)
        db.update("quizScores",cv,null,null)
    }

    fun getQuizScore():Int{
        var db = STEMDatabase(this).readableDatabase
        var scoreCur = db.rawQuery("SELECT numberSystemScore FROM quizScores",null)
        scoreCur.moveToFirst()
        return scoreCur.getInt(0)
    }
}