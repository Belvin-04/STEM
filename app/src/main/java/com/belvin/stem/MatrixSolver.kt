package com.belvin.stem

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class MatrixSolver : AppCompatActivity() {

    var quesMatrix1 = Array(3) { arrayOfNulls<EditText>(3) }
    var quesMatrix2 = Array(3) { arrayOfNulls<EditText>(3) }
    var quesMatrix2T = Array(3) { arrayOfNulls<TextInputLayout>(3) }
    var ansMatrix = Array(3){ arrayOfNulls<EditText>(3)}

    var ques1 = Array(3){IntArray(3)}
    var ques2 = Array(3){IntArray(3)}
    var ans = Array(3){IntArray(3)}
    lateinit var operationSpinner: Spinner
    lateinit var calculateBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix_solver)

        calculateBtn = findViewById(R.id.calculateMatrix)
        operationSpinner = findViewById(R.id.operationSpinner)

        operationSpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            arrayOf(
                "Addition",
                "Subtraction",
                "Multiplication",
                "Transpose",
                "Adjoint",
                "Determinant",
                "Inverse"
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


        operationSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 0 || position == 1 || position== 2 ){
                    quesMatrix2T.forEach { arrayOfEditText -> arrayOfEditText.forEach { it!!.visibility = View.VISIBLE } }
                } else{
                    quesMatrix2T.forEach { arrayOfEditText -> arrayOfEditText.forEach { it!!.visibility = View.GONE } }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        calculateBtn.setOnClickListener {
            var operation = operationSpinner.selectedItem.toString()

            (0..2).forEach { i ->
                (0..2).forEach { j ->
                    if(quesMatrix1[i][j]!!.text.toString().isEmpty()){
                        quesMatrix1[i][j]!!.setText("0")
                    }
                }
            }

            (0..2).forEach { i ->
                (0..2).forEach { j ->
                    if(quesMatrix2[i][j]!!.text.toString().isEmpty()){
                        quesMatrix2[i][j]!!.setText("0")
                    }
                }
            }

                for(i in 0..2){
                    for(j in 0..2){
                        ques1[i][j] = quesMatrix1[i][j]!!.text.toString().toInt()
                        ques2[i][j] = quesMatrix2[i][j]!!.text.toString().toInt()
                    }
                }

                when(operation){
                    "Addition" -> {
                        var ans1 = addOperation(ques1, ques2)
                        setMatrixAnsValues(ansMatrix, ans1)
                    }
                    "Subtraction" -> {
                        var ans1 = subOperation(ques1, ques2)
                        setMatrixAnsValues(ansMatrix, ans1)
                    }
                    "Multiplication" -> {
                        var ans1 = mulOperation(ques1, ques2)
                        setMatrixAnsValues(ansMatrix, ans1)
                    }
                    "Transpose" -> {
                        var ans1 = traOperation(ques1)
                        setMatrixAnsValues(ansMatrix, ans1)
                    }
                    "Adjoint" -> {
                        var ans1 = adjOperation(ques1)
                        setMatrixAnsValues(ansMatrix, ans1)
                    }
                    "Determinant" -> {
                        var ans1 = detOperation(ques1)
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Determinant Of Matrix")
                            .setMessage(ans1.toString())
                            .setNegativeButton("Ok", { dialog, which -> })
                            .show()

                    }
                    "Inverse" -> {
                        var ans1 = invOperation(ques1)
                        if (ans1 == null) {
                            MaterialAlertDialogBuilder(this).setTitle("Inverse of Matrix")
                                .setMessage("Since Determinant of the matrix is 0.The Inverse of the given Matrix does not exist")
                                .setNegativeButton("Ok") { _, _ -> }
                                .show()
                        } else {
                            setMatrixAnsValues(ansMatrix, ans1)
                        }

                    }
                }
        }
    }

    fun addOperation(ques1: Array<IntArray>, ques2: Array<IntArray>):Array<IntArray>{

        for(i in 0..2){
            for(j in 0..2){
                ans[i][j] = ques1[i][j] + ques2[i][j]
            }
        }
        return ans
    }

    fun subOperation(ques1: Array<IntArray>, ques2: Array<IntArray>): Array<IntArray> {

        for(i in 0..2){
            for(j in 0..2){
                ans[i][j] = ques1[i][j] - ques2[i][j]
            }
        }
        return ans
    }

    fun mulOperation(ques1: Array<IntArray>, ques2: Array<IntArray>): Array<IntArray> {

        for(i in 0..2){
            for(j in 0..2){
                ans[j][i] = (ques1[j][0] * ques2[0][i]) + (ques1[j][1] * ques2[1][i]) + (ques1[j][2] * ques2[2][i])
            }
        }
        return ans
    }

    fun traOperation(ques1: Array<IntArray>): Array<IntArray> {

        var ans1 = Array(3){IntArray(3)}
        for(i in 0..2){
            for(j in 0..2){
                ans1[j][i] = ques1[i][j]
            }
        }
        return ans1

    }

    fun adjOperation(ques1: Array<IntArray>):Array<IntArray>{

        ans[0][0] = ((ques1[1][1]*ques1[2][2]) - (ques1[1][2]*ques1[2][1]))
        ans[0][1] = -1*((ques1[1][0]*ques1[2][2]) - (ques1[1][2]*ques1[2][0]))
        ans[0][2] = ((ques1[1][0]*ques1[2][1]) - (ques1[1][1]*ques1[2][0]))

        ans[1][0] = -1*((ques1[0][1]*ques1[2][2]) - (ques1[0][2]*ques1[2][1]))
        ans[1][1] = ((ques1[0][0]*ques1[2][2]) - (ques1[0][2]*ques1[2][0]))
        ans[1][2] = -1*((ques1[0][0]*ques1[2][1]) - (ques1[0][1]*ques1[2][0]))

        ans[2][0] = ((ques1[0][1]*ques1[1][2]) - (ques1[0][2]*ques1[1][1]))
        ans[2][1] = -1*((ques1[0][0]*ques1[1][2]) - (ques1[0][2]*ques1[1][0]))
        ans[2][2] = ((ques1[0][0]*ques1[1][1]) - (ques1[0][1]*ques1[1][0]))

        var ans1 = traOperation(ans)

        return ans1
    }

    fun detOperation(ques1: Array<IntArray>):Int{

        var det  = (ques1[0][0] * ( (ques1[1][1]*ques1[2][2]) - (ques1[1][2]*ques1[2][1]) )) - (ques1[0][1] * ( (ques1[1][0]*ques1[2][2]) - (ques1[1][2]*ques1[2][0]) )) + (ques1[0][2] * ( (ques1[1][0]*ques1[2][1]) - (ques1[1][1]*ques1[2][0]) ))
        return det
    }

    fun invOperation(ques1: Array<IntArray>):Array<Array<String?>>?{

        var det = detOperation(ques1)
        if(det == 0){
            return null
        }

        var adj = adjOperation(ques1)

        //var ans2 = Array(3){Array<String>(3)}

        val ans2 = Array(3) { arrayOfNulls<String>(3) }

        (0..2).forEach { i ->
            (0..2).forEach { j ->
                ans2[i][j] = "${adj[i][j]}/${det}".toRational().toString()
            }
        }
        return ans2
    }

    fun setMatrixAnsValues(ansMatrix: Array<Array<EditText?>>, ans: Array<IntArray>){
        for(i in 0..2){
            for(j in 0..2){
                ansMatrix[i][j]!!.setText(ans[i][j].toString())
            }
        }
    }
    fun setMatrixAnsValues(ansMatrix: Array<Array<EditText?>>, ans: Array<Array<String?>>){
        for(i in 0..2){
            for(j in 0..2){
                ansMatrix[i][j]!!.setText(ans[i][j].toString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.matrix_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.matrixMethod -> {
                startActivity(Intent(this,InverseMatrixMethod::class.java))
            }
            R.id.matrixQuiz -> {}
        }

        return super.onOptionsItemSelected(item)
    }
}