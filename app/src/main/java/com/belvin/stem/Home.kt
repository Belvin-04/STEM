package com.belvin.stem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home : AppCompatActivity() {
    lateinit var moduleRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        var modulesArray = arrayOf("Resistor Calculator","Number System Converter","K-Map Solver")
        moduleRecyclerView = findViewById(R.id.moduleRecyclerView)

        moduleRecyclerView.layoutManager = LinearLayoutManager(this)
        moduleRecyclerView.adapter = ModuleAdapter(modulesArray)

    }
}