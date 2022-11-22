package com.swipe.mobile.data.remote

import com.swipe.mobile.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/loginUser")
    suspend fun login(
        @Field(value = "email", encoded = true) email: String,
        @Field("password") password: String,
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("auth/loginCommunity")
    suspend fun loginCommunity(
        @Field(value = "email", encoded = true) email: String,
        @Field("password") password: String,
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("users/register")
    suspend fun register(
        @Field("name") name:String,
        @Field(value = "email", encoded = true) email: String,
        @Field("password") password: String,
    ): Response<AuthResponse>

    @POST("users/me")
    suspend fun getProfile(): Response<BaseResponse<User>>

    @POST("communities/me")
    suspend fun getCommunityProfile(): Response<BaseResponse<User>>

    @POST("auth/logout")
    suspend fun logout(): Response<BaseResponse<String>>

    @GET("reports")
    suspend fun getReports(
        @Query("page") page: Int
    ): Response<BaseResponse<List<Report>>>

    @GET("reports/{id}")
    suspend fun getReport(
        @Path("id") id: Int
    ): Response<BaseResponse<Report>>

    @FormUrlEncoded
    @POST("reports")
    suspend fun createReport(
        @Field("title") title: String,
        @Field("description") desc: String,
        @Field("category") category: String,
        @Field("location") location: String
    ): Response<BaseResponse<Report>>

    @FormUrlEncoded
    @PATCH("reports/takeReport/{id}")
    suspend fun takeReport(
        @Path("id") id: Int,
        @Field("pic_name") picName: String,
        @Field("target_donation") targetDonation: Int
    ): Response<BaseResponse<Report>>

    @FormUrlEncoded
    @POST("donations/donate/{id}")
    suspend fun donate(
        @Path("id") id: Int,
        @Field("amount") amount: Int
    ): Response<BaseResponse<Donation>>

    @POST("donations/withdraw/{id}")
    suspend fun withdraw(
        @Path("id") id:Int
    ): Response<BaseResponse<String>>
}