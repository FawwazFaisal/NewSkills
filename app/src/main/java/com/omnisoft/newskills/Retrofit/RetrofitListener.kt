package com.omnisoft.newskills.Retrofit

import android.widget.Toast
import com.google.gson.JsonElement
import com.omnisoft.newskills.App
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RetrofitListener : Callback<JsonElement>{
    lateinit var tag:String
    lateinit var retrofitCallback: RetrofitCallback

    fun enqueue(call: Call<JsonElement>, tag:String, retrofitCallback: RetrofitCallback){
        this.retrofitCallback = retrofitCallback
        this.tag = tag
        call.enqueue(this)
    }

    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
        retrofitCallback.onResponse(call, response, tag)
    }

    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
        retrofitCallback.onFailure(call, t, tag)
//        Toast.makeText(App.context, t.message.toString(), Toast.LENGTH_SHORT).show()
    }
}