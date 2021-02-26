package com.belvin.stem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class KMapSolver : AppCompatActivity() {

    lateinit var aaText:TextView
    lateinit var abText:TextView
    lateinit var baText:TextView
    lateinit var bbText:TextView

    lateinit var aaValue:EditText
    lateinit var abValue:EditText
    lateinit var baValue:EditText
    lateinit var bbValue:EditText

    lateinit var solveBtn:Button

    var firstRow = mutableListOf<String>()
    var secondRow = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.k_map_solver)

        aaText = findViewById(R.id.aaText)
        abText = findViewById(R.id.abText)
        baText = findViewById(R.id.baText)
        bbText = findViewById(R.id.bbText)

        aaValue = findViewById<TextInputLayout>(R.id.aa).editText!!
        abValue = findViewById<TextInputLayout>(R.id.ab).editText!!
        baValue = findViewById<TextInputLayout>(R.id.ba).editText!!
        bbValue = findViewById<TextInputLayout>(R.id.bb).editText!!

        solveBtn = findViewById(R.id.kMapSolveBtn)

        solveBtn.setOnClickListener {

            if(aaValue.text.toString().isEmpty() || abValue.text.toString().isEmpty() || baValue.text.toString().isEmpty() || bbValue.text.toString().isEmpty()){
                Toast.makeText(this, "Please fill all the input fields", Toast.LENGTH_SHORT).show()
            }
            else{
                aaText.text = aaValue.text
                abText.text = abValue.text
                baText.text = baValue.text
                bbText.text = bbValue.text

                firstRow.add(aaText.text.toString())
                firstRow.add(abText.text.toString())
                secondRow.add(baText.text.toString())
                secondRow.add(bbText.text.toString())
            }
            solve()
        }
    }
    fun solve(){

    }
}