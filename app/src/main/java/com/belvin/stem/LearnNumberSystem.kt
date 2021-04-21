package com.belvin.stem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LearnNumberSystem : AppCompatActivity() {

    lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_activity)

        var learnList = mutableListOf<Pair<String,String>>()
        learnList.add(Pair("Binary To Octal","https://stem-hosting-bucket.s3.amazonaws.com/binary_to_octal.mkv"))
        learnList.add(Pair("Binary To Decimal","https://stem-hosting-bucket.s3.amazonaws.com/binary_to_decimal.mkv"))
        learnList.add(Pair("Binary To HexaDecimal","https://stem-hosting-bucket.s3.amazonaws.com/binary_to_hex.mkv"))

        learnList.add(Pair("Octal To Binary","https://stem-hosting-bucket.s3.amazonaws.com/octal_to_binary.mkv"))
        learnList.add(Pair("Octal To Decimal","https://stem-hosting-bucket.s3.amazonaws.com/octal_to_decimal.mkv"))
        learnList.add(Pair("Octal To HexaDecimal","https://stem-hosting-bucket.s3.amazonaws.com/octal_to_hex.mkv"))

        learnList.add(Pair("Decimal To Binary","https://stem-hosting-bucket.s3.amazonaws.com/decimal_to_binary.mkv"))
        learnList.add(Pair("Decimal To Octal","https://stem-hosting-bucket.s3.amazonaws.com/decimal_to_octal.mkv"))
        learnList.add(Pair("Decimal To HexaDecimal","https://stem-hosting-bucket.s3.amazonaws.com/decimal_to_hex.mkv"))

        learnList.add(Pair("HexaDecimal To Binary","https://stem-hosting-bucket.s3.amazonaws.com/hex_to_binary.mkv"))
        learnList.add(Pair("HexaDecimal To Decimal","https://stem-hosting-bucket.s3.amazonaws.com/hex_to_decimal.mkv"))
        learnList.add(Pair("HexaDecimal To Octal","https://stem-hosting-bucket.s3.amazonaws.com/hex_to_octal.mkv"))

        recyclerView = findViewById(R.id.learnRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LearnAdapter(learnList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu_final,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.calculator -> {
                startActivity(Intent(this,NumberSystemConverter::class.java))
            }
            R.id.quiz -> {
                startActivity(Intent(this,NumberSystemGameMode::class.java))
            }
        }
        return super.onOptionsItemSelected(item)

    }
}