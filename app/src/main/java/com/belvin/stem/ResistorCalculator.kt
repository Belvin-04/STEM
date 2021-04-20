package com.belvin.stem

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import kotlin.math.pow


class ResistorCalculator : AppCompatActivity() {
    val CAMERA_REQUEST = 1888
    val MY_CAMERA_PERMISSION_CODE = 100
    lateinit var fBand: ImageView
    lateinit var sBand: ImageView
    lateinit var tBand: ImageView
    lateinit var foBand: ImageView
    lateinit var img: ImageView
    lateinit var resistanceValue: EditText
    lateinit var toleranceSpinner: Spinner
    lateinit var calculateButton: Button
    lateinit var unitSpinner: Spinner

    var fBandNo = 0
    var sBandNo = 0
    var tBandNo = 0
    var foBandNo = 0

    val normalBandArray = arrayOf(
        R.drawable.black,
        R.drawable.brown,
        R.drawable.red,
        R.drawable.orange,
        R.drawable.yellow,
        R.drawable.green,
        R.drawable.blue,
        R.drawable.violet,
        R.drawable.grey,
        R.drawable.white
    )
    val multiplierBandArray = arrayOf(
        R.drawable.black,
        R.drawable.brown,
        R.drawable.red,
        R.drawable.orange,
        R.drawable.yellow,
        R.drawable.green,
        R.drawable.blue,
        R.drawable.violet,
        R.drawable.grey,
        R.drawable.white,
        R.drawable.gold,
        R.drawable.silver
    )
    val toleranceBandArray = arrayOf(
        R.drawable.brown,
        R.drawable.red,
        R.drawable.green,
        R.drawable.blue,
        R.drawable.violet,
        R.drawable.grey,
        R.drawable.gold,
        R.drawable.silver
    )
    val unitArray = arrayOf("Ohms", "K Ohms", "M Ohms", "G Ohms")
    val toleranceArray = arrayOf("1%", "2%", "0.5%", "0.25%", "0.10%", "0.05%", "5%", "10%")

    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                SUCCESS -> {
                    Log.i("OpenCV", "OpenCV loaded successfully")

                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Log.d(
                "OpenCV",
                "Internal OpenCV library not found. Using OpenCV Manager for initialization"
            )
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resistor_calculator)

        fBand = findViewById(R.id.f_band)
        sBand = findViewById(R.id.s_band)
        tBand = findViewById(R.id.t_band)
        foBand = findViewById(R.id.fo_band)
        resistanceValue = findViewById<TextInputLayout>(R.id.resistanceValue).editText!!
        toleranceSpinner = findViewById(R.id.toleranceValue)
        calculateButton = findViewById(R.id.calculateBtn)
        unitSpinner = findViewById(R.id.unit)
        img = findViewById(R.id.imgView)

        unitSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            unitArray
        )
        toleranceSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            toleranceArray
        )

        fBand.setOnClickListener {
            fBandNo = changeBand(fBand, fBandNo, normalBandArray)
            calculate()
        }
        sBand.setOnClickListener {
            sBandNo = changeBand(sBand, sBandNo, normalBandArray)
            calculate()
        }
        tBand.setOnClickListener {
            tBandNo = changeBand(tBand, tBandNo, multiplierBandArray)
            calculate()
        }
        foBand.setOnClickListener {
            foBandNo = changeBand(foBand, foBandNo, toleranceBandArray)
            calculate()
        }

        calculateButton.setOnClickListener {
            userCalculate()
        }

    }

    fun userCalculate() {

        if (TextUtils.isEmpty(resistanceValue.text)) {
            resistanceValue.error = "Please enter value"
        } else {
            var resistanceValueUser = resistanceValue.text.toString().toDouble()

            if (unitSpinner.selectedItem == "K Ohms") {
                resistanceValueUser *= 1000
            } else if (unitSpinner.selectedItem == "M Ohms") {
                resistanceValueUser *= 1000000
            } else if (unitSpinner.selectedItem == "G Ohms") {
                resistanceValueUser *= 1000000000
            }

            if (resistanceValueUser > 99000000000) {
                resistanceValue.error = "Value too large"
            } else {
                var fBandValue = (resistanceValueUser.toString()[0]).toString().toInt()
                var sBandValue = (resistanceValueUser.toString()[1]).toString().toInt()
                var multiplierBandValue = resistanceValueUser.toString().length - 4
                var toleranceVal = toleranceSpinner.selectedItemPosition

                fBand.setImageResource(normalBandArray[fBandValue])
                sBand.setImageResource(normalBandArray[sBandValue])
                tBand.setImageResource(multiplierBandArray[multiplierBandValue])
                foBand.setImageResource(toleranceBandArray[toleranceVal]!!)
            }
        }
    }

    fun changeBand(band: ImageView, number: Int, resList: Array<Int>): Int {
        var n = number
        if (n >= (resList.size - 1)) {
            n = 0
        } else {
            n += 1
        }
        band.setImageResource(resList[n])
        return n
    }

    fun calculate() {

        if (tBandNo == 10) {
            var resistanceValue = ((fBandNo * 10) + sBandNo).toDouble()
            resistanceValue /= 10
            this.resistanceValue.setText(resistanceValue.toString())
            toleranceSpinner.setSelection(foBandNo)
            unitSpinner.setSelection(0)
        } else if (tBandNo == 11) {
            var resistanceValue = ((fBandNo * 10) + sBandNo).toDouble()
            resistanceValue /= 100
            this.resistanceValue.setText(resistanceValue.toString())
            toleranceSpinner.setSelection(foBandNo)
            unitSpinner.setSelection(0)

        } else {
            var newResistanceValue = 0.0
            var resistanceValue = ((fBandNo * 10) + sBandNo).toLong()
            resistanceValue *= (10.toDouble().pow(tBandNo.toDouble())).toLong()
            this.resistanceValue.setText(resistanceValue.toString())
            toleranceSpinner.setSelection(foBandNo)
            unitSpinner.setSelection(0)
            if (resistanceValue.toString().length > 3) {
                if (resistanceValue / (10.toDouble().pow(9)) >= 1) {
                    newResistanceValue = resistanceValue / (10.toDouble().pow(9))
                    unitSpinner.setSelection(3)
                } else if (resistanceValue / (10.toDouble().pow(6)) >= 1) {
                    newResistanceValue = resistanceValue / (10.toDouble().pow(6))
                    unitSpinner.setSelection(2)
                } else if (resistanceValue / (10.toDouble().pow(3)) >= 1) {
                    newResistanceValue = resistanceValue / (10.toDouble().pow(3))
                    unitSpinner.setSelection(1)
                }
                this.resistanceValue.setText(newResistanceValue.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun openCamera() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
        } else {
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

        if (requestCode == MY_CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST)
        } else {
            Toast.makeText(this, "Please accept all permissions", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            val resultUri = result.uri
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)

            val bitmap1 = grabcutAlgo(bitmap)

            img.setImageBitmap(bitmap1)
            //val photo = data?.extras?.get("data") as Bitmap

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun grabcutAlgo(bit: Bitmap):Bitmap{
        var b = bit.copy(Bitmap.Config.ARGB_8888, true);
        var tl=Point();
        var br=Point();
//GrabCut part
        var img =Mat();
        Utils.bitmapToMat(b, img);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGBA2RGB);

        var r = img.rows();
        var c = img.cols();
        var p1 = Point((c / 100).toDouble(), (r / 100).toDouble());
        var p2 = Point((c - c / 100).toDouble(), (r - r / 100).toDouble());
        var rect = Rect(p1, p2);
//Rect rect = new Rect(tl, br);
        var background = Mat(img.size(), CvType.CV_8UC3, Scalar(255.0, 255.0, 255.0));
        var firstMask = Mat();
        var bgModel = Mat();
        var fgModel = Mat();
        lateinit var mask:Mat;
        var source = Mat(1, 1, CvType.CV_8U, Scalar(Imgproc.GC_PR_FGD.toDouble()));
        var dst = Mat();


        Imgproc.grabCut(img, firstMask, rect, bgModel, fgModel, 5, Imgproc.GC_INIT_WITH_RECT);
        Core.compare(firstMask, source, firstMask, Core.CMP_EQ);

        var foreground = Mat(img.size(), CvType.CV_8UC3, Scalar(255.0, 255.0, 255.0));

        img.copyTo(foreground, firstMask);

        var color = Scalar(255.0, 0.0, 0.0, 255.0);
        Imgproc.rectangle(img, tl, br, color);

        var tmp = Mat();
        Imgproc.resize(background, tmp, img.size());
        background = tmp;
        mask =  Mat(foreground.size(), CvType.CV_8UC1, Scalar(255.0, 255.0, 255.0));

        Imgproc.cvtColor(foreground, mask, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(mask, mask, 254.0, 255.0, Imgproc.THRESH_BINARY_INV);
        System.out.println();
        var vals = Mat(1, 1, CvType.CV_8UC3, Scalar(0.0));
        background.copyTo(dst);

        background.setTo(vals, mask);

        Core.add(background, foreground, dst, mask);
        var grabCutImage = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        var processedImage = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(dst, grabCutImage);
        //dst.copyTo(sampleImage);
        //imageView.setImageBitmap(grabCutImage);
        firstMask.release();
        source.release();
        bgModel.release();
        fgModel.release();
        return grabCutImage
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
            }
        }
        return super.onOptionsItemSelected(item)
    }

}