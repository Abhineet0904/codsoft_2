package com.example.quoteapp

import retrofit2.http.GET
import retrofit2.Response

interface API_Interface {
    
    @GET("random")
    suspend fun getRandomQuote() : Response<List<Quote>>

}
