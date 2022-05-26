package com.android.project.ecotrans.api_service

import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.response.ResponseAutoComplete
import com.android.project.ecotrans.response.ResponseLogin
import com.android.project.ecotrans.response.ResponseRegister
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
//    @FormUrlEncoded
    @POST("register")
    fun register(
//        @Field("username") username: String,
//        @Field("password") password: String,
//        @Field("email") email: String,
//        @Field("firstName") firstName: String,
//        @Field("lastName") lastName: String,
//        @Field("birthDate") birthDate: String
        @Body requestBody: RequestBody
    ): Call<ResponseRegister>

    @Headers("Content-Type: application/json")
//    @FormUrlEncoded
    @POST("login")
    fun login(
//        @Field("username") username: String,
//        @Field("password") password: String
        @Body requestBody: RequestBody
    ): Call<ResponseLogin>

    @Headers("Content-Type: application/json")
    @GET("user/{id}")
    fun user(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<User>


//    @FormUrlEncoded
//    @POST("login")
//    fun login(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<PostResponseLogin>
//
//    @FormUrlEncoded
//    @POST("register")
//    fun register(
//        @Field("name") name: String,
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<PostResponseRegister>

    @Headers("Content-Type: application/json")
    @GET("autocomplete")
    fun searchLocation(
        @Body requestBody: RequestBody
    ): Call<ResponseAutoComplete>

}