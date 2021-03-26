package com.belvin.stem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class InverseMatrixMethod : AppCompatActivity() {

    lateinit var firstEq:EditText
    lateinit var secondEq:EditText
    lateinit var thirdEq:EditText
    lateinit var matrixSolveBtn: Button
    var fE = mutableListOf<String>()
    var sE = mutableListOf<String>()
    var tE = mutableListOf<String>()
    var matrixA = Array(3){IntArray(3)}
    //var matrixB = IntArray(3)
    var matrixB = arrayOfNulls<String>(3)
    var matrixX = Array(3){IntArray(3) }
    var pattern = """^((((\-\d+|\d+))[x])(\+|\-)(\d+[y])(\+|\-)(\d+[z])=(\-\d+|\d+))${'$'}"""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inverse_matrix_method)

        firstEq = findViewById<TextInputLayout>(R.id.firstEq).editText!!
        secondEq = findViewById<TextInputLayout>(R.id.secondEq).editText!!
        thirdEq = findViewById<TextInputLayout>(R.id.thirdEq).editText!!
        matrixSolveBtn = findViewById(R.id.calculateEquation)


        matrixSolveBtn.setOnClickListener {

            if(firstEq.text.toString().matches(Regex(pattern)) && secondEq.text.toString().matches(Regex(pattern)) && thirdEq.text.toString().matches(Regex(pattern))){
                fE.add((firstEq.text.toString().substringBefore("x")))
                fE.add((firstEq.text.toString().substringBefore("y")).substringAfter("x"))
                fE.add((firstEq.text.toString().substringBefore("z")).substringAfter("y"))
                fE.add((firstEq.text.toString().substringAfter("=")))

                sE.add((secondEq.text.toString().substringBefore("x")))
                sE.add((secondEq.text.toString().substringBefore("y")).substringAfter("x"))
                sE.add((secondEq.text.toString().substringBefore("z")).substringAfter("y"))
                sE.add((secondEq.text.toString().substringAfter("=")))

                tE.add((thirdEq.text.toString().substringBefore("x")))
                tE.add((thirdEq.text.toString().substringBefore("y")).substringAfter("x"))
                tE.add((thirdEq.text.toString().substringBefore("z")).substringAfter("y"))
                tE.add((thirdEq.text.toString().substringAfter("=")))

                matrixA[0][0] = fE[0].toInt()
                matrixA[0][1] = fE[1].toInt()
                matrixA[0][2] = fE[2].toInt()

                matrixA[1][0] = sE[0].toInt()
                matrixA[1][1] = sE[1].toInt()
                matrixA[1][2] = sE[2].toInt()

                matrixA[2][0] = tE[0].toInt()
                matrixA[2][1] = tE[1].toInt()
                matrixA[2][2] = tE[2].toInt()

                matrixB[0] = fE[3]
                matrixB[1] = sE[3]
                matrixB[2] = tE[3]

                var ans1 = MatrixSolver().invOperation(matrixA)

                if(ans1 == null){

                    MaterialAlertDialogBuilder(this)
                        .setTitle("Inverse Matrix Method")
                        .setMessage("Solution of the current equation does not exist")
                        .setNegativeButton("Ok", { dialog, which -> })
                        .show()
                }
                else{
                    var ans1 = multiply(ans1,matrixB)
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Inverse Matrix Method")
                        .setMessage("${ans1[0]}, ${ans1[1]}, ${ans1[2]}")
                        .setNegativeButton("Ok", { dialog, which -> })
                        .show()
                }
            }
            else{
                Toast.makeText(this, "Incorrect question format", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun multiply(ques1:Array<Array<String?>>, ques2: Array<String?>): Array<String?> {

        var ans1 = arrayOfNulls<String>(3)

        ans1[0] = ((ques1[0][0].toRational()*ques2[0].toRational()) + (ques1[0][1].toRational()*ques2[1].toRational()) + (ques1[0][2].toRational()*ques2[2].toRational())).toString()
        ans1[1] = (((ques1[1][0].toRational()*ques2[0].toRational()) + (ques1[1][1].toRational()*ques2[1].toRational()) + (ques1[1][2].toRational()*ques2[2].toRational()))).toString()
        ans1[2] = ((ques1[2][0].toRational()*ques2[0].toRational()) + (ques1[2][1].toRational()*ques2[1].toRational()) + (ques1[2][2].toRational()*ques2[2].toRational())).toString()

        return ans1

    }
}