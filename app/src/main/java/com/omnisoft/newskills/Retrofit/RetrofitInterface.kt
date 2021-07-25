package com.omnisoft.newskills.Retrofit

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.omnisoft.newskills.Others.APIs
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap

interface RetrofitInterface {
    @GET
    fun getRequest(@Url url: String): Call<JsonElement>

    @POST
    fun postRequest(@HeaderMap headers: HashMap<String,String>, @Url url: String, @Body requestBody: JsonObject):Call<JsonElement>
}