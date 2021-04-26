package com.belvin.stem

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class KmapQuiz : AppCompatActivity() {

    lateinit var answer: TextView

    lateinit var aaValue: EditText
    lateinit var abValue: EditText
    lateinit var baValue: EditText
    lateinit var bbValue: EditText
    lateinit var answerEt: EditText


    lateinit var nextBtn: Button
    var correctAnswer = ""
    var givenAnswer = ""

    var score = 0
    var questionNumber = 1
    lateinit var questionNumberText:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kmap_quiz)

        answer = findViewById(R.id.answer)

        aaValue = findViewById<TextInputLayout>(R.id.aa).editText!!
        abValue = findViewById<TextInputLayout>(R.id.ab).editText!!
        baValue = findViewById<TextInputLayout>(R.id.ba).editText!!
        bbValue = findViewById<TextInputLayout>(R.id.bb).editText!!
        answerEt = findViewById<TextInputLayout>(R.id.answerEt).editText!!

        nextBtn = findViewById(R.id.nextBtn)

        questionNumberText = findViewById(R.id.questionNumberText)

        generateRandomNumber()

        nextBtn.setOnClickListener {

            givenAnswer = answerEt.text.toString()

            questionNumber++
            if (questionNumber <= 10) {
                questionNumberText.setText("${questionNumber}/10")
                nextQuestion()
                if (questionNumber == 10) {
                    nextBtn.setText("Submit")
                }
            } else {
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
                                questionNumber = 1
                                questionNumberText.setText("${questionNumber}/10")
                                nextBtn.setText("Next")
                                nextQuestion()
                            }
                            .setNegativeButton("NO") { dialog1, which1 ->
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

    fun solve():String{
        var aa = aaValue.text.toString()
        var ab = abValue.text.toString()
        var ba = baValue.text.toString()
        var bb = bbValue.text.toString()

        var ans = ""

        if(aa == "0" && ab == "0" && ba == "0" && bb == "0"){
            ans = "0"
        }
        else if(aa == "0" && ab == "0" && ba == "0" && bb == "1"){
            ans = "AB"


        }
        else if(aa == "0" && ab == "0" && ba == "1" && bb == "0"){
            ans = "AB'"

        }
        else if(aa == "0" && ab == "0" && ba == "1" && bb == "1"){
            ans = "A"

        }
        else if(aa == "0" && ab == "1" && ba == "0" && bb == "0"){
            ans = "A'B"

        }
        else if(aa == "0" && ab == "1" && ba == "0" && bb == "1"){
            ans = "B"


        }
        else if(aa == "0" && ab == "1" && ba == "1" && bb == "0"){
            ans = "A'B + AB'"

        }
        else if(aa == "0" && ab == "1" && ba == "1" && bb == "1"){
            ans = "A + B"



        }
        else if(aa == "1" && ab == "0" && ba == "0" && bb == "0"){
            ans = "A'B'"


        }
        else if(aa == "1" && ab == "0" && ba == "0" && bb == "1"){
            ans = "A'B' + AB"

        }
        else if(aa == "1" && ab == "0" && ba == "1" && bb == "0"){
            ans = "B'"


        }
        else if(aa == "1" && ab == "0" && ba == "1" && bb == "1"){
            ans = "B' + A"

        }
        else if(aa == "1" && ab == "1" && ba == "0" && bb == "0"){
            ans = "A'"

        }
        else if(aa == "1" && ab == "1" && ba == "0" && bb == "1"){
            ans = "A' + B"

        }
        else if(aa == "1" && ab == "1" && ba == "1" && bb == "0"){
            ans = "A' + B'"

        }
        else{
            ans = "1"

        }

        return ans

    }

    fun generateRandomNumber(){
        aaValue.setText((0..1).random().toString())
        abValue.setText((0..1).random().toString())
        baValue.setText((0..1).random().toString())
        bbValue.setText((0..1).random().toString())

        correctAnswer = solve()
        answer.setText(correctAnswer)
    }

    fun nextQuestion(){
        answerEt.setText("")

        if((givenAnswer.isNotEmpty() && correctAnswer.isNotEmpty()) && (givenAnswer == correctAnswer)){
            score++
        }

        generateRandomNumber()
    }

    fun updateQuizScore(score:Int){
        var db = STEMDatabase(this).readableDatabase
        var cv = ContentValues()
        cv.put("kMapScore",score)
        db.update("quizScores",cv,null,null)
    }

    fun getQuizScore():Int{
        var db = STEMDatabase(this).readableDatabase
        var scoreCur = db.rawQuery("SELECT kMapScore FROM quizScores",null)
        scoreCur.moveToFirst()
        return scoreCur.getInt(0)
    }
}