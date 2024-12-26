package com.example.quoteapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("NotifyDataSetChanged")
class SavedQuotes : AppCompatActivity() {
    private val quotes = mutableListOf<Quote>()
    private lateinit var quoteList: RecyclerView
    private lateinit var quoteAdapter: QuoteAdapter

    private val db = DBhandler(this, "quote_db", null, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_saved_quotes)

        quoteList = findViewById(R.id.quoteList)
        quoteAdapter = QuoteAdapter(quotes)
        quoteList.layoutManager = LinearLayoutManager(this)
        quoteList.adapter = quoteAdapter


        val itemTouchHelperCallbackShare = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT)
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                shareQuote(quotes[position])

                quoteAdapter.notifyItemChanged(position)
            }
        }
        val itemTouchHelperShare = ItemTouchHelper(itemTouchHelperCallbackShare)
        itemTouchHelperShare.attachToRecyclerView(quoteList)



        val itemTouchHelperCallbackDelete = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT)
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                db.deleteQuote(quotes[position])
            }
        }
        val itemTouchHelperDelete = ItemTouchHelper(itemTouchHelperCallbackDelete)
        itemTouchHelperDelete.attachToRecyclerView(quoteList)


        displaySavedQuotes()
    }




    private fun displaySavedQuotes() {

        val cur = db.retrieveQuotes()

        if (cur.moveToNext())
        {
            do
            {
                val quote = cur.getString(cur.getColumnIndexOrThrow("quote"))
                val author = cur.getString(cur.getColumnIndexOrThrow("author"))

                quotes.add(Quote(quote,author))
            }
            while (cur.moveToNext())
            cur.close()

            quoteAdapter.notifyDataSetChanged()
        }
    }




    private fun shareQuote(quote: Quote): Boolean {

        val text = quote.q + "\n ~ "+ quote.a
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(shareIntent)

        return true
    }
}