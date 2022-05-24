package com.android.project.ecotrans.api_service

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("birthDate") birthDate: String
    ): Call<ResponseRegister>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

//    @GET("search/users")
//    fun searchUser(
//        @Query("q") q: String
//    ): Call<ResponseQ>
//
//    @GET("users/{username}")
//    fun detailUser(
//        @Path("username") username: String
//    ): Call<ResponseU>
//
//    @GET("users/{username}/followers")
//    fun followerUser(
//        @Path("username") username: String
//    ): Call<List<ResponseFItem>>
//
//    @GET("users/{username}/following")
//    fun followingUser(
//        @Path("username") username: String
//    ): Call<List<ResponseFItem>>

}