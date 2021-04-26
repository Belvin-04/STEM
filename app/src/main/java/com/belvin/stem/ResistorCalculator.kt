package com.belvin.stem

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
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

    lateinit var colorCheck:TextView

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

    //Red changed
    val RED1_MIN = Scalar(0.toDouble(), 115.toDouble(), 230.toDouble())
    val RED1_MAX = Scalar(6.toDouble(), 255.toDouble(), 255.toDouble())

    val ORANGE_MIN = Scalar(7.toDouble(), 150.toDouble(), 150.toDouble())
    val ORANGE_MAX = Scalar(18.toDouble(), 250.toDouble(), 250.toDouble())

    //Yellow changed
    val YELLOW_MIN = Scalar(25.toDouble(), 130.toDouble(), 100.toDouble())
    val YELLOW_MAX = Scalar(34.toDouble(), 255.toDouble(), 255.toDouble())

    val GREEN_MIN = Scalar(35.toDouble(), 60.toDouble(), 60.toDouble())
    val GREEN_MAX = Scalar(75.toDouble(), 250.toDouble(), 150.toDouble())

    val BLUE_MIN = Scalar(82.toDouble(), 60.toDouble(), 49.toDouble())
    val BLUE_MAX = Scalar(128.toDouble(), 255.toDouble(), 255.toDouble())

    val VIOLET_MIN = Scalar(129.toDouble(), 60.toDouble(), 50.toDouble())
    val VIOLET_MAX = Scalar(165.toDouble(), 250.toDouble(), 150.toDouble())

    /**
     * Red wraps around and is therefore defined twice
     */
    //Red changed
    val RED2_MIN = Scalar(175.toDouble(), 235.toDouble(), 240.toDouble())
    val RED2_MAX = Scalar(180.toDouble(), 255.toDouble(), 255.toDouble())

    val BLACK_MIN = Scalar(0.toDouble(), 0.toDouble(), 0.toDouble())
    val BLACK_MAX = Scalar(180.toDouble(), 250.toDouble(), 40.toDouble())

    //Brown changed
    val BROWN_MIN = Scalar(0.toDouble(), 31.toDouble(), 41.toDouble())
    val BROWN_MAX = Scalar(25.toDouble(), 250.toDouble(), 165.toDouble())

    val GREY_MIN = Scalar(0.toDouble(), 0.toDouble(), 41.toDouble())
    val GREY_MAX = Scalar(180.toDouble(), 30.toDouble(), 130.toDouble())

    val WHITE_MIN = Scalar(0.toDouble(), 0.toDouble(), 150.toDouble())
    val WHITE_MAX = Scalar(180.toDouble(), 30.toDouble(), 255.toDouble())

    val GOLD_MIN = Scalar(0.toDouble(), 0.toDouble(), 0.toDouble()) //// TODO:

    val GOLD_MAX = Scalar(0.toDouble(), 0.toDouble(), 0.toDouble())

    val SILVER_MIN = Scalar(0.toDouble(), 0.toDouble(), 0.toDouble()) //// TODO:

    val SILVER_MAX = Scalar(0.toDouble(), 0.toDouble(), 0.toDouble())

    /**
     * This color (black) is used for unknown color values
     * when converting from color names to colors
     */
    val UNKNOWN = Scalar(0.toDouble(), 0.toDouble(), 0.toDouble())

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
        colorCheck = findViewById(R.id.colorCheck)

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
            }
            else{
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)
            }
        } else {
            Toast.makeText(this, "Please accept all permissions", Toast.LENGTH_SHORT).show()
        }
    }

    

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            val resultUri = result.uri
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
            val bitmap1 = grabcutAlgo(bitmap)
            var resizedBitmap = Bitmap.createScaledBitmap(
                bitmap1, 1000, 600, false
            );
            img.setImageBitmap(resizedBitmap)

            img.setOnTouchListener{ _, event ->
                var x = event.x.toInt()
                var y = event.y.toInt()

                if(x < 0){
                    x = 0;
                }else if(x > resizedBitmap.width -1){
                    x = resizedBitmap.width-1
                }

                if(y < 0){
                    y = 0;
                }else if(y > resizedBitmap.height -1){
                    y = resizedBitmap.height-1
                }

                val pixel: Int = resizedBitmap.getPixel(x, y)

                val redValue: Int = Color.red(pixel)
                val blueValue: Int = Color.blue(pixel)
                val greenValue: Int = Color.green(pixel)


                colorCheck.setTextColor(pixel)

                var hsv = FloatArray(3)
                //var currentColor = Color.rgb(redValue,greenValue,blueValue)
                //Color.colorToHSV(currentColor,hsv)
                Color.RGBToHSV(redValue,greenValue,blueValue,hsv)

                colorCheck.setText("RGB($redValue, $greenValue, $blueValue)\nHSV(${hsv[0]/2}, ${hsv[1]*255}, ${hsv[2]*255})\n" +
                        "HSV(${hsv[0]}, ${hsv[1]}, ${hsv[2]})")
                var colorName = getColorName(Scalar(hsv[0].toDouble()/2,hsv[1].toDouble()*255,hsv[2].toDouble()*255))
                Toast.makeText(this, colorName, Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, "$redValue, $blueValue, $greenValue", Toast.LENGTH_SHORT).show()

                false
            }

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
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        MY_CAMERA_PERMISSION_CODE
                    )
                } else {
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun isScalarBetweenBounds(
        scalar: Scalar?,
        lowerBound: Scalar?,
        upperBound: Scalar?
    ): Boolean {
        require(!(scalar == null || lowerBound == null || upperBound == null)) { "scalars must not be null!" }
        if (lowerBound.`val`.size != upperBound.`val`.size) System.err.println("upper and lower bound size mismatch")
        if (scalar.`val`.size != lowerBound.`val`.size) System.err.println("scalar and bounds size mismatch")
        for (i in scalar.`val`.indices) {
            if (scalar.`val`[i] < lowerBound.`val`[i] || scalar.`val`[i] > upperBound.`val`[i]) {
                return false
            }
        }
        return true
    }

    fun getColorName(colorHsv: Scalar?): String? {
        requireNotNull(colorHsv) { "colorHsv must not be null!" }
        var name = ""
        if (isScalarBetweenBounds(colorHsv, RED1_MIN, RED1_MAX) ||
            isScalarBetweenBounds(colorHsv, RED2_MIN, RED2_MAX)
        ) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "Red" + ")!",Toast.LENGTH_SHORT).show()
            name = "Red"
        }
        if (isScalarBetweenBounds(colorHsv, ORANGE_MIN, ORANGE_MAX)) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "Orange" + ")!",Toast.LENGTH_SHORT).show()
            name = "Orange"
        }
        if (isScalarBetweenBounds(colorHsv, YELLOW_MIN, YELLOW_MAX)) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "Yellow" + ")!",Toast.LENGTH_SHORT).show()
            name = "Yellow"
        }
        if (isScalarBetweenBounds(colorHsv, GREEN_MIN, GREEN_MAX)) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "Green" + ")!",Toast.LENGTH_SHORT).show()
            name = "Green"
        }
        if (isScalarBetweenBounds(colorHsv, BLUE_MIN, BLUE_MAX)) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "Blue" + ")!",Toast.LENGTH_SHORT).show()
            name = "Blue"
        }
        if (isScalarBetweenBounds(colorHsv, VIOLET_MIN, VIOLET_MAX)) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "Violet" + ")!",Toast.LENGTH_SHORT).show()
            name = "Violet"
        }
        if (isScalarBetweenBounds(colorHsv, BROWN_MIN, BROWN_MAX)) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "Brown" + ")!",Toast.LENGTH_SHORT).show()
            name = "Brown"
        }
        if (isScalarBetweenBounds(colorHsv, BLACK_MIN, BLACK_MAX)) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "Black" + ")!",Toast.LENGTH_SHORT).show()
            name = "Black"
        }
        if (isScalarBetweenBounds(colorHsv, GREY_MIN, GREY_MAX)) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "Grey" + ")!",Toast.LENGTH_SHORT).show()
            name = "Grey"
        }
        if (isScalarBetweenBounds(colorHsv, WHITE_MIN, WHITE_MAX)) {
            if (name != "") Toast.makeText(this,"overlapping colorHsv name definitions (" + name + " and " + "White" + ")!",Toast.LENGTH_SHORT).show()
            name = "White"
        }

        /*
        if(isScalarBetweenBounds(colorHsv, GOLD_MIN, GOLD_MAX)){
            if(name != ColorName.Unknown)
                System.err.println("overlapping colorHsv name definitions (" + name + " and " + ColorName.Gold + ")!");
            name = ColorName.Gold;
        }
        if(isScalarBetweenBounds(colorHsv, SILVER_MIN, SILVER_MAX)){
            if(name != ColorName.Unknown)
                System.err.println("overlapping colorHsv name definitions (" + name + " and " + ColorName.Silver + ")!");
            name = ColorName.Silver;
        }
        */return name
    }


}