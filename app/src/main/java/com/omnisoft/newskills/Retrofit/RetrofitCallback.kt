package com.omnisoft.newskills.Retrofit

import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

interface RetrofitCallback {
    fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>, tag: String)
    fun onFailure(call: Call<JsonElement>, t: Throwable, tag: String)
}