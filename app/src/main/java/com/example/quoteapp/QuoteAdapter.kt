package com.example.quoteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuoteAdapter(
    private val quotes: MutableList<Quote>
) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quoteTitle = itemView.findViewById<TextView>(R.id.quoteTitle)
        private val authorTitle = itemView.findViewById<TextView>(R.id.authorTitle)

        fun bind(quote: Quote) {
            quoteTitle.text = quote.q
            authorTitle.text = quote.a
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }



    override fun getItemCount(): Int {
        return quotes.size
    }



    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quotes[position]
        holder.bind(quote)
    }
}