package com.example.quoteapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

@SuppressLint("Recycle")
class DBhandler(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    private val table_name = "quote_table"



    override fun onCreate(p0: SQLiteDatabase?) {
        val qry1 = "CREATE TABLE $table_name (quote VARCHAR, author VARCHAR)"
        p0!!.execSQL(qry1)
    }




    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $table_name")
        onCreate(p0)
    }




    fun insertQuote(quoteString: String, authorString: String, context: Context?) {
        val db = this.writableDatabase

        val qry2 = "SELECT * FROM $table_name WHERE quote = ? AND author = ?"
        val cur = db.rawQuery(qry2, arrayOf(quoteString, authorString))

        if (cur.count == 0)
        {
            val record = ContentValues()
            record.put("quote", quoteString)
            record.put("author", authorString)
            db.insert(table_name, null, record)

            cur.close()
            db.close()
        }
        else
        {
            Toast.makeText(context, "Quote already saved", Toast.LENGTH_SHORT).show()
        }
    }




    fun retrieveQuotes(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $table_name", null)
    }




    fun deleteQuote(quote: Quote) {

        val clause = "quote = ? AND author = ?"
        val args = arrayOf(quote.q, quote.a)

        val db = this.writableDatabase
        db.delete(table_name, clause, args)
    }
}