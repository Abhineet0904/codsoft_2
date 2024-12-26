package com.example.quoteapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("SetTextI18n", "ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {
    private lateinit var quoteText: TextView
    private lateinit var authorText: TextView
    private lateinit var layout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        quoteText = findViewById(R.id.quoteView)
        authorText = findViewById(R.id.authorView)

        getQuote()

        layout = findViewById(R.id.main)
        layout.setOnLongClickListener {
            val popUp = PopupMenu(this@MainActivity, quoteText)
            popUp.menuInflater.inflate(R.menu.menu_popup, popUp.menu)

            popUp.setOnMenuItemClickListener {
                when (it.title)
                {
                    "Share" -> shareQuote()
                    "Save" -> saveQuote(quoteText.text.toString(), authorText.text.toString())
                    "Favourites" -> viewSavedQuotes()
                    else -> Toast.makeText(this@MainActivity,
                        "No option selected", Toast.LENGTH_SHORT).show()
                }
                true
            }
            popUp.show()

            true
        }

    }




    private fun getQuote() {

        GlobalScope.launch {
            try
            {
                val result = RetrofitInstance.quoteApi.getRandomQuote()
                if (result.isSuccessful && result.body() != null)
                {
                    runOnUiThread {
                        result.body()!!.first().let {
                            quoteText.text = it.q
                            authorText.text = it.a
                        }
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(this@MainActivity, "No quote displayed",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            catch (e : Exception)
            {
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(this@MainActivity,"Some error has occurred",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    private fun shareQuote(): Boolean {

        val text = quoteText.text.toString() + "\n ~ "+ authorText.text.toString()
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(shareIntent)
        return true
    }




    private fun saveQuote(quoteText: String, authorText: String): Boolean {
        val db = DBhandler(this, "quote_db", null, 1)
        db.insertQuote(quoteText, authorText, this)
        return true
    }




    private fun viewSavedQuotes(): Boolean {
        val viewIntent = Intent(this, SavedQuotes::class.java)
        startActivity(viewIntent)
        return true
    }
}