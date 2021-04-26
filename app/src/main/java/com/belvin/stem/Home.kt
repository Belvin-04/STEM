package com.belvin.stem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home : AppCompatActivity() {
    lateinit var moduleRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        var modulesArray = arrayOf("Resistor","Number System","K-Map","Matrix")
        moduleRecyclerView = findViewById(R.id.moduleRecyclerView)

        moduleRecyclerView.layoutManager = LinearLayoutManager(this)
        moduleRecyclerView.adapter = ModuleAdapter(modulesArray)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.quiz_score,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.quizScoreMenu -> {
                var i = Intent(this,QuizScores::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}