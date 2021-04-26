package com.belvin.stem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class QuizScores : AppCompatActivity() {

    lateinit var resistorScore: TextView
    lateinit var numberSystemScore: TextView
    lateinit var kMapScore: TextView
    lateinit var matrixScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_scores)

        var db = STEMDatabase(this).readableDatabase

        var scoreCursor = db.rawQuery("SELECT * FROM quizScores",null)

        resistorScore = findViewById(R.id.resistorScore)
        numberSystemScore = findViewById(R.id.numberSystemScore)
        kMapScore = findViewById(R.id.kMapScore)
        matrixScore = findViewById(R.id.matrixScore)

        while(scoreCursor.moveToNext()){
            resistorScore.text = "Resistor                     : ${scoreCursor.getString(0)}"
            numberSystemScore.text = "Number System       : ${scoreCursor.getString(1)}"
            kMapScore.text = "K-Map                        : ${scoreCursor.getString(2)}"
            matrixScore.text = "Matrix                        : ${scoreCursor.getString(3)}"

        }



    }
}