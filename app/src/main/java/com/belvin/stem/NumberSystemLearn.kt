package com.belvin.stem

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.PathInterpolator
import android.widget.Button
import android.widget.TextView

class NumberSystemLearn : AppCompatActivity() {
    lateinit var btn:Button
    lateinit var t1:TextView
    lateinit var t2:TextView
    lateinit var t3:TextView
    lateinit var t4:TextView
    lateinit var t5:TextView
    var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.number_system_learn)

        t1 = findViewById(R.id.t1)
        t2 = findViewById(R.id.t2)
        t3 = findViewById(R.id.t3)
        t4 = findViewById(R.id.t4)
        t5 = findViewById(R.id.t5)
        btn = findViewById(R.id.nextStep)

        var animation = AnimationUtils.loadAnimation(this,R.anim.move)

        btn.setOnClickListener {
            if(counter < 5){
                counter++
            }
            else{
                counter = 0
            }

            when(counter){

                0 -> {
                    t1.visibility = View.GONE
                    t2.visibility = View.GONE
                    t3.visibility = View.GONE
                    t4.visibility = View.GONE
                    t5.visibility = View.GONE
                }
                1 -> {
                    t1.visibility = View.VISIBLE
                    t1.startAnimation(animation)
                    
                }
                2 -> {
                    t2.visibility = View.VISIBLE
                    t2.startAnimation(animation)
                }
                3 -> {
                    t3.visibility = View.VISIBLE
                    t3.startAnimation(animation)
                }
                4 -> {
                    t4.visibility = View.VISIBLE
                    t4.startAnimation(animation)
                }
                5 -> {
                    t5.visibility = View.VISIBLE
                    t5.startAnimation(animation)
                }

            }



        }

        /*val path1 = Path().apply {
            arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true)
        }
        val animator = ObjectAnimator.ofFloat(text, View.X, View.Y, path1).apply {
            duration = 2000
            start()
        }*/

        /*var animation = AnimationUtils.loadAnimation(this,R.anim.move)
        text.startAnimation(animation)*/
    }
}