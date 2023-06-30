package com.example.userstory

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email")email: String,
        @Field("password")password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name")name: String,
        @Field("email")email: String,
        @Field("password")password: String
    ): Call<RegisterResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("authorization")authorization: String,
        @Query("page")page: Int? = null,
        @Query("size")size: Int? = null,
        @Query("location")location: Int? = null
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("authorization")authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat")lat: Float? = null,
        @Part("lon")lon: Float? = null
    ): Call<FileUploadResponse>

    @GET("stories")
    fun getLocation(
        @Header("authorization")authorization: String,
        @Query("page")page: Int? = null,
        @Query("size")size: Int? = null,
        @Query("location")location: Int = 1
    ): Call<StoriesResponse>
}