package com.belvin.stem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LearnKmap : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_activity)

        val learnList = mutableListOf<Pair<String,String>>()

        learnList.add(Pair("2 Variable K-Map","kmap_two"))

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
                startActivity(Intent(this,KMapSolver::class.java))
            }
            R.id.quiz -> {
                startActivity(Intent(this,KmapQuiz::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }
}