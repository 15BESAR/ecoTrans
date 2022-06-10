package com.android.project.ecotrans.api_service

import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.response.*
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
    ): Call<ResponseGetUser>

    @Headers("Content-Type: application/json")
    @PUT("user/{id}")
    fun putuserdata(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Call<ResponseUpdateUser>


    @Headers("Content-Type: application/json")
    @POST("autocomplete")
    fun searchLocation(
        @Body requestBody: RequestBody
    ): Call<ResponseAutoComplete>

    @Headers("Content-Type: application/json")
    @POST("routes")
    fun getRoutes(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): Call<ResponseRoute>

    @Headers("Content-Type: application/json")
    @POST("forecast")
    fun getForecast(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): Call<ResponseForecast>

    @Headers("Content-Type: application/json")
    @GET("vouchers")
    fun getAllVoucher(
        @Header("Authorization") token: String
    ): Call<ResponseGetAllVoucher>

    @Headers("Content-Type: application/json")
    @POST("purchase")
    fun purchase(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): Call<ResponsePurchaseVoucher>

    @Headers("Content-Type: application/json")
    @GET("purchases/{user}")
    fun getBoughtVoucher(
        @Header("Authorization") token: String,
        @Path("user") id: String
    ): Call<ResponseGetAllPurchaseHistory>

    @Headers("Content-Type: application/json")
    @GET("voucher/{voucherId}")
    fun getVoucherById(
        @Header("Authorization") token: String,
        @Path("voucherId") id: String
    ): Call<ResponseGetVoucherById>

    @Headers("Content-Type: application/json")
    @POST("journey")
    fun finishJourney(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): Call<ResponseFinish>
}