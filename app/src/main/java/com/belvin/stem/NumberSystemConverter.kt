package com.belvin.stem

import android.Manifest
import android.R.attr
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.belvin.stem.TessOCR.DATA_PATH
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.google.android.material.textfield.TextInputLayout
import com.googlecode.tesseract.android.TessBaseAPI
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.math.pow


class NumberSystemConverter : AppCompatActivity() {
    var REQUEST_CAMERA = 1111
    val CAMERA_REQUEST = 1888
    lateinit var imageUri:Uri;
    val MY_CAMERA_PERMISSION_CODE = 100
    lateinit var decimalValue:EditText
    lateinit var binaryValue:EditText
    lateinit var octalValue:EditText
    lateinit var hexDecValue:EditText
    lateinit var decimalTextWatcher: TextWatcher
    lateinit var binaryTextWatcher: TextWatcher
    lateinit var octalTextWatcher: TextWatcher
    lateinit var hexTextWatcher: TextWatcher
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.number_system_converter)

        decimalValue = findViewById<TextInputLayout>(R.id.decimalValue).editText!!
        binaryValue = findViewById<TextInputLayout>(R.id.binaryValue).editText!!
        octalValue = findViewById<TextInputLayout>(R.id.octalValue).editText!!
        hexDecValue = findViewById<TextInputLayout>(R.id.hexDecValue).editText!!

        decimalTextWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                removeListeners("Decimal")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){
                    binaryValue.setText("")
                    octalValue.setText("")
                    hexDecValue.setText("")
                    return
                }
                binaryValue.setText(decimalToBinary(s.toString().toLong()))
                octalValue.setText(decimalToOctal(s.toString().toLong()))
                hexDecValue.setText(decimalToHEX(s.toString().toLong()))

            }

            override fun afterTextChanged(s: Editable?) {
                addListeners("Decimal")
            }
        }

        binaryTextWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                removeListeners("Binary")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){
                    decimalValue.setText("")
                    octalValue.setText("")
                    hexDecValue.setText("")
                    return
                }

                decimalValue.setText(binaryToDecimal(s.toString()))
                octalValue.setText(decimalToOctal(decimalValue.text.toString().toLong()))
                hexDecValue.setText(decimalToHEX(decimalValue.text.toString().toLong()))

            }

            override fun afterTextChanged(s: Editable?) {
                addListeners("Binary")
            }

        }

        octalTextWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                removeListeners("Octal")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){
                    binaryValue.setText("")
                    decimalValue.setText("")
                    hexDecValue.setText("")
                    return
                }
                decimalValue.setText(octalToDecimal(s.toString()))
                binaryValue.setText(decimalToBinary(decimalValue.text.toString().toLong()))
                hexDecValue.setText(decimalToHEX(decimalValue.text.toString().toLong()))

            }

            override fun afterTextChanged(s: Editable?) {
                addListeners("Octal")
            }
        }

        hexTextWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                removeListeners("HEX")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){
                    binaryValue.setText("")
                    octalValue.setText("")
                    decimalValue.setText("")
                    return
                }
                decimalValue.setText(hexToDecimal(s.toString()))
                binaryValue.setText(decimalToBinary(decimalValue.text.toString().toLong()))
                octalValue.setText(decimalToOctal(decimalValue.text.toString().toLong()))
            }

            override fun afterTextChanged(s: Editable?) {
                addListeners("HEX")
            }
        }

        decimalValue.addTextChangedListener(decimalTextWatcher)
        binaryValue.addTextChangedListener(binaryTextWatcher)
        octalValue.addTextChangedListener(octalTextWatcher)
        hexDecValue.addTextChangedListener(hexTextWatcher)
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

    fun removeListeners(s: String){

        if(s == "Decimal"){
            binaryValue.removeTextChangedListener(binaryTextWatcher)
            octalValue.removeTextChangedListener(octalTextWatcher)
            hexDecValue.removeTextChangedListener(hexTextWatcher)
        }
        else if(s == "Binary"){
            decimalValue.removeTextChangedListener(decimalTextWatcher)
            octalValue.removeTextChangedListener(octalTextWatcher)
            hexDecValue.removeTextChangedListener(hexTextWatcher)
        }
        else if(s == "Octal"){
            binaryValue.removeTextChangedListener(binaryTextWatcher)
            decimalValue.removeTextChangedListener(decimalTextWatcher)
            hexDecValue.removeTextChangedListener(hexTextWatcher)
        }
        else{
            binaryValue.removeTextChangedListener(binaryTextWatcher)
            octalValue.removeTextChangedListener(octalTextWatcher)
            decimalValue.removeTextChangedListener(decimalTextWatcher)
        }
    }

    fun addListeners(s: String){
        if(s == "Decimal"){
            binaryValue.addTextChangedListener(binaryTextWatcher)
            octalValue.addTextChangedListener(octalTextWatcher)
            hexDecValue.addTextChangedListener(hexTextWatcher)
        }
        else if(s == "Binary"){
            decimalValue.addTextChangedListener(decimalTextWatcher)
            octalValue.addTextChangedListener(octalTextWatcher)
            hexDecValue.addTextChangedListener(hexTextWatcher)
        }
        else if(s == "Octal"){
            binaryValue.addTextChangedListener(binaryTextWatcher)
            decimalValue.addTextChangedListener(decimalTextWatcher)
            hexDecValue.addTextChangedListener(hexTextWatcher)
        }
        else{
            binaryValue.addTextChangedListener(binaryTextWatcher)
            octalValue.addTextChangedListener(octalTextWatcher)
            decimalValue.addTextChangedListener(decimalTextWatcher)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun openCamera() {

        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
        }
        else{
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == MY_CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST)
        }
        else{
            Toast.makeText(this, "Please accept all permissions", Toast.LENGTH_SHORT).show()
        }
    }

    fun extractText(bitmap: Bitmap):String{
        var tessBaseAPI = TessBaseAPI()
        tessBaseAPI.init(DATA_PATH, "eng")
        tessBaseAPI.setImage(bitmap)
        var extractedText = tessBaseAPI.utF8Text
        tessBaseAPI.end()
        return extractedText
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CAMERA -> if (resultCode == RESULT_OK) {
                if (imageUri != null) {
                    //inspect(imageUri)

                }
            }
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
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.camera_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.scanQues -> {

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)

                /*val filename = System.currentTimeMillis().toString() + ".jpg"

                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, filename)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                imageUri =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

                val intent = Intent()
                intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(
                    intent,
                    REQUEST_CAMERA
                )*/
            }
        }

        return super.onOptionsItemSelected(item)
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

    fun processText(extractedText: String){
        var text = extractedText.trim()
        var st = (text.split("(")[1]).split(")")
        Toast.makeText(this, "${st[0]}  ${st[1]}", Toast.LENGTH_SHORT).show()
        when(st[1].trim()){
            "2" -> {
                binaryValue.setText(st[0].trim())
            }
            "8" -> {
                octalValue.setText(st[0].trim())
            }
            "10" -> {
                decimalValue.setText(st[0].trim())
            }
            "16" -> {
                hexDecValue.setText(st[0].trim())
            }
            else -> {
                Toast.makeText(this, "Incorrect base value ${st[1].trim()}", Toast.LENGTH_SHORT).show()}
        }
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
}