package com.omnisoft.newskills.Retrofit

import com.omnisoft.newskills.Others.APIs
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(){
    private val retrofitClient: Retrofit = Retrofit.Builder().baseUrl(APIs.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    var retrofitInterface : RetrofitInterface = retrofitClient.create(RetrofitInterface::class.java)

    companion object{
        private var client:RetrofitClient? = null
        fun getInstance():RetrofitClient{
            if(client==null){
                client = RetrofitClient()
            }
            return client!!
        }
    }
}