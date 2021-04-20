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
        learnList.add(Pair("Binary To Octal","binary_to_octal"))
        learnList.add(Pair("Binary To Decimal","binary_to_decimal"))
        learnList.add(Pair("Binary To HexaDecimal","binary_to_hex"))

        learnList.add(Pair("Octal To Binary","octal_to_binary"))
        learnList.add(Pair("Octal To Decimal","octal_to_decimal"))
        learnList.add(Pair("Octal To HexaDecimal","octal_to_hex"))

        learnList.add(Pair("Decimal To Binary","decimal_to_binary"))
        learnList.add(Pair("Decimal To Octal","decimal_to_octal"))
        learnList.add(Pair("Decimal To HexaDecimal","decimal_to_hex"))

        learnList.add(Pair("HexaDecimal To Binary","hex_to_binary"))
        learnList.add(Pair("HexaDecimal To Decimal","hex_to_decimal"))
        learnList.add(Pair("HexaDecimal To Octal","hex_to_octal"))

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