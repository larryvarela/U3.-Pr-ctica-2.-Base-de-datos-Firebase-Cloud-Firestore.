package mx.edu.tepic.ladm_u3_ejercicio3_firestore

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBase (
    contex: Context?,
    name:String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int)
    : SQLiteOpenHelper(contex, name, factory, version) {
    override fun onCreate(p: SQLiteDatabase) {
        p.execSQL("CREATE TABLE NOTAS(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITULO VARCHAR(100), CONTENIDO VARCHAR(100), HORA VARCHAR(100), TELEFONO VARCHAR(100), FECHA VARCHAR(100));")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}