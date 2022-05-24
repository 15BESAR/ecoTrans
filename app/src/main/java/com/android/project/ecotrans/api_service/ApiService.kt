package com.android.project.ecotrans.api_service

import com.android.project.ecotrans.response.PostResponseLogin
import com.android.project.ecotrans.response.PostResponseRegister
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

//    @FormUrlEncoded
//    @POST("register")
//    fun register(
//        @Field("username") username: String,
//        @Field("password") password: String,
//        @Field("email") email: String,
//        @Field("firstName") firstName: String,
//        @Field("lastName") lastName: String,
//        @Field("birthDate") birthDate: String
//    ): Call<ResponseRegister>
//
//    @FormUrlEncoded
//    @POST("login")
//    fun login(
//        @Field("username") username: String,
//        @Field("password") password: String
//    ): Call<ResponseLogin>


    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<PostResponseLogin>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<PostResponseRegister>

}