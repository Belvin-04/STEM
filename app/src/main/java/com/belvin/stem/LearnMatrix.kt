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

        learnList.add(Pair("Matrix Basics","https://stem-hosting-bucket.s3.amazonaws.com/matrix_basics.mkv"))
        learnList.add(Pair("Matrix Addition","https://stem-hosting-bucket.s3.amazonaws.com/matrix_addition.mkv"))
        learnList.add(Pair("Matrix Subtraction","https://stem-hosting-bucket.s3.amazonaws.com/matrix_subtraction.mkv"))
        learnList.add(Pair("Matrix Multiplication 1","https://stem-hosting-bucket.s3.amazonaws.com/matrix_multiplication_1.mkv"))
        learnList.add(Pair("Matrix Multiplication 2","https://stem-hosting-bucket.s3.amazonaws.com/matrix_multiplication_2.mkv"))
        learnList.add(Pair("Matrix Transpose","https://stem-hosting-bucket.s3.amazonaws.com/matrix_transpose.mkv"))
        learnList.add(Pair("Matrix Determinant 1","https://stem-hosting-bucket.s3.amazonaws.com/matrix_determinant_1.mkv"))
        learnList.add(Pair("Matrix Determinant 2","https://stem-hosting-bucket.s3.amazonaws.com/matrix_determinant_2.mkv"))
        learnList.add(Pair("Matrix Determinant 3","https://stem-hosting-bucket.s3.amazonaws.com/matrix_determinant_3.mkv"))
        learnList.add(Pair("Matrix Adjoint","https://stem-hosting-bucket.s3.amazonaws.com/matrix_adjoint.mkv"))
        learnList.add(Pair("Matrix Inverse 1","https://stem-hosting-bucket.s3.amazonaws.com/matrix_inverse_1.mkv"))
        learnList.add(Pair("Matrix Inverse 2","https://stem-hosting-bucket.s3.amazonaws.com/matrix_inverse_2.mkv"))
        learnList.add(Pair("Matrix Inverse 3","https://stem-hosting-bucket.s3.amazonaws.com/matrix_inverse_3.mkv"))
        learnList.add(Pair("Matrix Linear Equation 1","https://stem-hosting-bucket.s3.amazonaws.com/matrix_linear_equation_1.mkv"))
        learnList.add(Pair("Matrix Linear Equation 2","https://stem-hosting-bucket.s3.amazonaws.com/matrix_linear_equation_2.mkv"))

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