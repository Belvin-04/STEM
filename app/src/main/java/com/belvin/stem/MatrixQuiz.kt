package com.belvin.stem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class MatrixQuiz : AppCompatActivity() {

    var quesMatrix1 = Array(3) { arrayOfNulls<EditText>(3) }
    var quesMatrix2 = Array(3) { arrayOfNulls<EditText>(3) }
    var quesMatrix2T = Array(3) { arrayOfNulls<TextInputLayout>(3) }
    var ansMatrix = Array(3) { arrayOfNulls<EditText>(3) }

    var questionNumber = 0
    var ques1 = Array(3) { IntArray(3) }
    var ques2 = Array(3) { IntArray(3) }
    var ans = Array(3){arrayOfNulls<String>(3)}
    lateinit var operationSpinner: Spinner
    lateinit var nextBtn: Button
    lateinit var questionNumberText: TextView
    var score = 0

    var correctAnswers = Array(3){arrayOfNulls<String>(3)}
    var givenAnswers = Array(3){arrayOfNulls<String>(3)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.matrix_quiz)

        nextBtn = findViewById(R.id.calculateMatrix)
        operationSpinner = findViewById(R.id.operationSpinner)
        questionNumberText = findViewById(R.id.questionNumberText)


        operationSpinner.isEnabled = false
        operationSpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            arrayOf(
                "Addition",
                "Subtraction",
                "Multiplication",
                "Transpose",
                "Adjoint",
            )
        )

        (0..2).forEach { i ->
            (0..2).forEach { j ->
                val quesMatrixId1 = "M$i$j"
                val resId1 = resources.getIdentifier(quesMatrixId1, "id", packageName)
                quesMatrix1[i][j] = findViewById<TextInputLayout>(resId1).editText

                val quesMatrixId2 = "M1$i$j"
                val resId2 = resources.getIdentifier(quesMatrixId2, "id", packageName)
                quesMatrix2[i][j] = findViewById<TextInputLayout>(resId2).editText

                val ansMatrixId = "M2$i$j"
                val resId = resources.getIdentifier(ansMatrixId, "id", packageName)
                ansMatrix[i][j] = findViewById<TextInputLayout>(resId).editText
            }
        }

        (0..2).forEach { i ->
            (0..2).forEach { j ->
                val quesMatrixId2 = "M1$i$j"
                val resId2 = resources.getIdentifier(quesMatrixId2, "id", packageName)
                quesMatrix2T[i][j] = findViewById(resId2)
            }
        }


        operationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0 || position == 1 || position == 2) {
                    quesMatrix2T.forEach { arrayOfEditText ->
                        arrayOfEditText.forEach {
                            it!!.visibility = View.VISIBLE
                        }
                    }
                } else {
                    quesMatrix2T.forEach { arrayOfEditText ->
                        arrayOfEditText.forEach {
                            it!!.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        generateRandomNumber()

        nextBtn.setOnClickListener {

            for(i in 0..2){
                for(j in 0..2){
                    givenAnswers[i][j] = ansMatrix[i][j]!!.text.toString()
                }
            }


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
                            .setMessage("Click YES to take the test again or click No to discard the message")
                            .setPositiveButton("YES") { dialog1, which1 ->
                                questionNumber = 1
                                questionNumberText.setText("${questionNumber}/10")
                                nextBtn.setText("Next")
                                nextQuestion()
                            }
                            .setNegativeButton("NO") { dialog1, which1 ->
                                startActivity(Intent(this, Home::class.java))
                            }
                            .show()
                    }
                    .show()
            }
        }

    }

    fun nextQuestion() {
        for (i in 0..2) {
            for (j in 0..2) {
                quesMatrix1[i][j]!!.setText("")
                quesMatrix2[i][j]!!.setText("")
                ansMatrix[i][j]!!.setText("")
            }
        }

        if (isEqual(givenAnswers,correctAnswers)) {
            score++
        }

        generateRandomNumber()

    }

    fun generateRandomNumber() {

        var operation = (0..4).random()

        for (i in 0..2) {
            for (j in 0..2) {
                quesMatrix1[i][j]!!.setText(((-30..30).random()).toString())
                quesMatrix2[i][j]!!.setText(((-30..30).random()).toString())
                ques1[i][j] = quesMatrix1[i][j]!!.text.toString().toInt()
                ques2[i][j] = quesMatrix2[i][j]!!.text.toString().toInt()
            }
        }

        var ans1 = Array(3){IntArray(3)}
        when (operation) {
            0 -> {
                ans1 = MatrixSolver().addOperation(ques1,ques2)
            }
            1 -> {
                ans1 = MatrixSolver().subOperation(ques1,ques2)
            }
            2 -> {
                ans1 = MatrixSolver().mulOperation(ques1,ques2)
            }
            3 -> {
                ans1 = MatrixSolver().traOperation(ques1)
            }
            4 -> {
                ans1 = MatrixSolver().adjOperation(ques1)
            }
        }

        for(i in 0..2){
            for(j in 0..2){
                ans[i][j] = ans1[i][j].toString()
                correctAnswers[i][j] = ans[i][j]
            }
        }

        operationSpinner.setSelection(operation)
    }
    fun<T> isEqual(first: Array<Array<T>>, second: Array<Array<T>>): Boolean {
        if (first == second) return true
        if (first == null || second == null) return false
        if (first.size != second.size) return false
        for (i in first.indices) {
            if (first[i].size != second[i].size) return false
            for (j in first[i].indices) {
                if (!first[i][j]?.equals(second[i][j])!!) return false
            }
        }
        return true
    }
}