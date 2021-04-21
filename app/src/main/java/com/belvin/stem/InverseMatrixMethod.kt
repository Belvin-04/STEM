package com.belvin.stem

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.*

class InverseMatrixMethod : AppCompatActivity() {

    lateinit var firstEq:EditText
    lateinit var secondEq:EditText
    lateinit var thirdEq:EditText
    val MY_CAMERA_PERMISSION_CODE = 100
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
            fE.clear()
            sE.clear()
            tE.clear()
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
                    var ans2 = multiply(ans1,matrixB)
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Inverse Matrix Method")
                        //.setMessage("${ans1[0]}, ${ans1[1]}, ${ans1[2]}")
                        .setMessage("x = ${ans2[0]}\ny = ${ans2[1]}\nz = ${ans2[2]}")
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == MY_CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)
        }
        else{
            Toast.makeText(this, "Please accept all permissions", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode === RESULT_OK) {
                    val resultUri = result.uri

                    inspect(resultUri)
                    //From here you can load the image however you need to, I recommend using the Glide library
                } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.camera_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.scanQues -> {
                if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(arrayOf(Manifest.permission.CAMERA),MY_CAMERA_PERMISSION_CODE)
                }
                else{
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun inspect(uri: Uri) {
        var `is`: InputStream? = null
        var bitmap: Bitmap? = null
        try {
            `is` = contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            options.inSampleSize = 2
            options.inScreenDensity = DisplayMetrics.DENSITY_LOW
            bitmap = BitmapFactory.decodeStream(`is`, null, options)
            inspectFromBitmap(bitmap)
        } catch (e: FileNotFoundException) {
            Log.w(
                "LOG",
                "Failed to find the file: $uri", e
            )
        } finally {
            bitmap?.recycle()
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    Log.w(
                        "LOG",
                        "Failed to close InputStream",
                        e
                    )
                }
            }
        }
    }

    private fun inspectFromBitmap(bitmap: Bitmap?) {
        val textRecognizer = TextRecognizer.Builder(this).build()
        try {
            if (!textRecognizer.isOperational) {
                AlertDialog.Builder(this)
                    .setMessage("Text recognizer could not be set up on your device").show()
                return
            }
            val frame = Frame.Builder().setBitmap(bitmap).build()
            val origTextBlocks = textRecognizer.detect(frame)
            val textBlocks: MutableList<TextBlock> = ArrayList()
            for (i in 0 until origTextBlocks.size()) {
                val textBlock = origTextBlocks.valueAt(i)
                textBlocks.add(textBlock)
            }
            Collections.sort(textBlocks, object : Comparator<TextBlock?> {
                override fun compare(o1: TextBlock?, o2: TextBlock?): Int {
                    val diffOfTops = o1!!.boundingBox.top - o2!!.boundingBox.top
                    val diffOfLefts = o1.boundingBox.left - o2.boundingBox.left
                    return if (diffOfTops != 0) {
                        diffOfTops
                    } else diffOfLefts
                }
            })
            val detectedText = StringBuilder()
            for (textBlock in textBlocks) {
                if (textBlock != null && textBlock.value != null) {
                    detectedText.append(textBlock.value)
                    detectedText.append("\n")
                }
            }
            processText(detectedText.toString())
        } finally {
            textRecognizer.release()
        }
    }

    private fun processText(extractedText: String) {
        var text = extractedText.trim()
        try{
            var ques = text.split("\n")
            firstEq.setText(ques[0].replace(" ",""))
            secondEq.setText(ques[1].replace(" ",""))
            thirdEq.setText(ques[2].replace(" ",""))
        }
        catch(e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }
}