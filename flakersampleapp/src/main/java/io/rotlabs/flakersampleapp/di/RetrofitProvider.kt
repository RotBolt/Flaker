package io.rotlabs.flakersampleapp.di

import io.rotlabs.flakerokhttpcore.FlakerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitProvider {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(FlakerInterceptor.Builder().build())
        .build()

    private val baseUrl = "https://reqres.in"

    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}
