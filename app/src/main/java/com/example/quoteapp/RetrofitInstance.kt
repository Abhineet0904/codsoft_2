package com.example.quoteapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*"object" KEYWORD ENSURES THAT THERE IS ONLY ONE INSTANCE OF RetrofitInstance
FOR THE ENTIRE DURATION THE APP RUNS. */
object RetrofitInstance {

    //THIS URL WILL BE THE STARTING POINT OF ALL API REQUESTS.
    private const val BASE_URL = "https://zenquotes.io/api/"

    /*THIS METHOD BUILDS AND RETURNS A "Retrofit" INSTANCE CONFIGURED WITH THE "BASE_URL"
    AND A GSON CONVERTOR.
    THE GSON CONVERTOR IS USED TO HANDLE THE AUTOMATIC CONVERSION OF JSON RESPONSES TO KOTLIN OBJECTS. */
    private fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /*"getInstance()" METHOD CONFIGURES A "Retrofit" INSTANCE.
    ".create(API_Interface::class.java)" GENERATES AN IMPLEMENTATION OF THE "API_Instance"
    The "Retrofit" INSTANCE USES THIS IMPLEMENTATION TO MAKE API CALLS. */
    val quoteApi: API_Interface = getInstance().create(API_Interface::class.java)
}


/*RETROFIT IS HTTP CLIENT LIBRARY FOR ANDROID, WHICH SIMPLIFIES THE PROCESS OF MAKING NETWORK REQUESTS,
PARSING RESPONSES AND HANDLING API INTERACTIONS.
REQUIRES THE "com.squareup.retrofit2:retrofit:<VERSION>" AND
"com.squareup.retrofit2:converter-gson:<VERSION>" DEPENDENCIES IN THE GRADLE FILE. */
