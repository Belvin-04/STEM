package com.belvin.stem

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputLayout

class KMapSolver : AppCompatActivity() {

    val CAMERA_REQUEST = 1888
    val MY_CAMERA_PERMISSION_CODE = 100

    lateinit var aaText:TextView
    lateinit var abText:TextView
    lateinit var baText:TextView
    lateinit var bbText:TextView
    lateinit var answer:TextView

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
        answer = findViewById(R.id.answer)

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
        var aa = aaText.text.toString()
        var ab = abText.text.toString()
        var ba = baText.text.toString()
        var bb = bbText.text.toString()

        resetColour()

        if(aa == "0" && ab == "0" && ba == "0" && bb == "0"){
            answer.text = "0"
        }
        else if(aa == "0" && ab == "0" && ba == "0" && bb == "1"){
            answer.text = "AB"
            bbText.setBackgroundColor(Color.GRAY)

        }
        else if(aa == "0" && ab == "0" && ba == "1" && bb == "0"){
            answer.text = "AB'"
            baText.setBackgroundColor(Color.GRAY)
        }
        else if(aa == "0" && ab == "0" && ba == "1" && bb == "1"){
            answer.text = "A"
            baText.setBackgroundColor(Color.GRAY)
            bbText.setBackgroundColor(Color.GRAY)
        }
        else if(aa == "0" && ab == "1" && ba == "0" && bb == "0"){
            answer.text = "A'B"
            abText.setBackgroundColor(Color.GRAY)
        }
        else if(aa == "0" && ab == "1" && ba == "0" && bb == "1"){
            answer.text = "B"
            abText.setBackgroundColor(Color.GRAY)
            bbText.setBackgroundColor(Color.GRAY)

        }
        else if(aa == "0" && ab == "1" && ba == "1" && bb == "0"){
            answer.text = "A'B + AB'"
            abText.setBackgroundColor(Color.GRAY)
            baText.setBackgroundColor(Color.CYAN)
        }
        else if(aa == "0" && ab == "1" && ba == "1" && bb == "1"){
            answer.text = "A + B"

            abText.setBackgroundColor(Color.GRAY)
            baText.setBackgroundColor(Color.GRAY)
            bbText.setBackgroundColor(Color.GRAY)

        }
        else if(aa == "1" && ab == "0" && ba == "0" && bb == "0"){
            answer.text = "A'B'"
            aaText.setBackgroundColor(Color.GRAY)

        }
        else if(aa == "1" && ab == "0" && ba == "0" && bb == "1"){
            answer.text = "A'B' + AB"
            aaText.setBackgroundColor(Color.GRAY)
            bbText.setBackgroundColor(Color.CYAN)
        }
        else if(aa == "1" && ab == "0" && ba == "1" && bb == "0"){
            answer.text = "B'"
            aaText.setBackgroundColor(Color.GRAY)
            baText.setBackgroundColor(Color.GRAY)

        }
        else if(aa == "1" && ab == "0" && ba == "1" && bb == "1"){
            answer.text = "B' + A"
            aaText.setBackgroundColor(Color.GRAY)
            baText.setBackgroundColor(Color.GRAY)
            bbText.setBackgroundColor(Color.GRAY)
        }
        else if(aa == "1" && ab == "1" && ba == "0" && bb == "0"){
            answer.text = "A'"
            aaText.setBackgroundColor(Color.GRAY)
            abText.setBackgroundColor(Color.GRAY)
        }
        else if(aa == "1" && ab == "1" && ba == "0" && bb == "1"){
            answer.text = "A' + B"
            aaText.setBackgroundColor(Color.GRAY)
            abText.setBackgroundColor(Color.GRAY)
            bbText.setBackgroundColor(Color.GRAY)
        }
        else if(aa == "1" && ab == "1" && ba == "1" && bb == "0"){
            answer.text = "A' + B'"
            aaText.setBackgroundColor(Color.GRAY)
            abText.setBackgroundColor(Color.GRAY)
            baText.setBackgroundColor(Color.GRAY)
        }
        else{
            answer.text = "1"
            aaText.setBackgroundColor(Color.GRAY)
            abText.setBackgroundColor(Color.GRAY)
            baText.setBackgroundColor(Color.GRAY)
            bbText.setBackgroundColor(Color.GRAY)
        }

    }

    fun resetColour(){
        aaText.setBackgroundColor(Color.WHITE)
        abText.setBackgroundColor(Color.WHITE)
        baText.setBackgroundColor(Color.WHITE)
        bbText.setBackgroundColor(Color.WHITE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun openCamera() {

        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.CAMERA),MY_CAMERA_PERMISSION_CODE)
        }
        else{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,CAMERA_REQUEST)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == MY_CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,CAMERA_REQUEST)
        }
        else{
            Toast.makeText(this, "Please accept all permissions", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            val photo = data?.extras?.get("data") as Bitmap

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.camera_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.scanQues -> {
                openCamera()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}