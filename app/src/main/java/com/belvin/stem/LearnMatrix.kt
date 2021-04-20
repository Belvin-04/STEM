package com.belvin.stem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LearnMatrix : AppCompatActivity() {

    lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_activity)

        val learnList = mutableListOf<Pair<String,String>>()

        learnList.add(Pair("Matrix Basics","matrix_basics"))
        learnList.add(Pair("Matrix Addition","matrix_addition"))
        learnList.add(Pair("Matrix Subtraction","matrix_subtraction"))
        learnList.add(Pair("Matrix Multiplication 1","matrix_multiplication_1"))
        learnList.add(Pair("Matrix Multiplication 2","matrix_multiplication_2"))
        learnList.add(Pair("Matrix Transpose","matrix_transpose"))
        learnList.add(Pair("Matrix Determinant 1","matrix_determinant_1"))
        learnList.add(Pair("Matrix Determinant 2","matrix_determinant_2"))
        learnList.add(Pair("Matrix Determinant 3","matrix_determinant_3"))
        learnList.add(Pair("Matrix Adjoint","matrix_adjoint"))
        learnList.add(Pair("Matrix Inverse 1","matrix_inverse_1"))
        learnList.add(Pair("Matrix Inverse 2","matrix_inverse_2"))
        learnList.add(Pair("Matrix Inverse 3","matrix_inverse_3"))
        learnList.add(Pair("Matrix Linear Equation 1","matrix_linear_equation_1"))
        learnList.add(Pair("Matrix Linear Equation 2","matrix_linear_equation_2"))

        recyclerView = findViewById(R.id.learnRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LearnAdapter(learnList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.matrix_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.matrixQuiz -> {
                startActivity(Intent(this,MatrixQuiz::class.java))
            }
            R.id.matrixSolver -> {
                startActivity(Intent(this,MatrixSolver::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}