package com.belvin.stem

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class STEMDatabase(context: Context):SQLiteOpenHelper(context,"STEMDatabase",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE quizScores (resistorScore INTEGER,numberSystemScore INTEGER,kMapScore INTEGER,matrixScore INTEGER)")
        db!!.execSQL("INSERT INTO quizScores VALUES(0,0,0,0)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}