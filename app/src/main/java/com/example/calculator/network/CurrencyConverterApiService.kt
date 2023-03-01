package com.example.calculator.network

import androidx.lifecycle.MutableLiveData
import com.example.calculator.network.models.AvailableCurrencies
import com.example.calculator.network.models.Conversion
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.time.temporal.TemporalAmount

private const val API_KEY = "X1ZJ4FRboAdcVx9MOLbqwqHNjYLx0AiG"
private const val BASE_URL = "https://api.apilayer.com/fixer/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CurrencyConverterApiService {
    @Headers("apikey:$API_KEY")
    @GET("symbols")
    suspend fun getAvailableCurrencies(): AvailableCurrencies

    @Headers("apikey: $API_KEY")
    @GET("convert")
    suspend fun getCurrencyConversion(
        @Query("amount") amount: String,
        @Query("from") from: String,
        @Query("to") to: String
    ): Conversion
}

object CurrencyConverterApi {
    val retrofitService: CurrencyConverterApiService by lazy {
        retrofit.create(CurrencyConverterApiService::class.java)
    }
}
