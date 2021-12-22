package com.ahmed_abobakr.musalaweatherapp.base.network

import android.accounts.NetworkErrorException
import com.ahmed_abobakr.musalaweatherapp.base.exceptions.ApiException
import com.ahmed_abobakr.musalaweatherapp.base.exceptions.AuthException
import com.ahmed_abobakr.musalaweatherapp.base.exceptions.NetowrkException
import com.ahmed_abobakr.musalaweatherapp.base.exceptions.UnknownException
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


object MusalaWeatherService{

    private fun createOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
            requestBuilder.addHeader(
                "pappid",
                "Android"
            )
            chain.proceed(requestBuilder.build())
        }
            .addInterceptor(logger)
            .retryOnConnectionFailure(true)
            .build()
    }

    fun getClient(): MusalaWeatherAPI {
        return Retrofit.Builder()
            .client(createOkHttpClient())
            .baseUrl("http://161.97.160.164:8889/ords/main/epp/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MusalaWeatherAPI::class.java)
    }

    private fun convertBodyToString(result: Response): String{
        var reader: BufferedReader? = null
        val sb = StringBuilder()
        try {
            reader = BufferedReader(InputStreamReader(result.body!!.byteStream()))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }


        val result = sb.toString()
        return result
    }

    fun mapException(throwable: Throwable): Throwable {
        if(throwable is HttpException){
            val response = throwable.response()
            return try {
                val errorBody = convertErrorBody(throwable)
                if(throwable.code() == 401) AuthException(errorBody!!.error, throwable)
                else{
                    if(errorBody != null){
                        ApiException(errorBody.error, errorBody.code.toInt())
                    }else {
                        UnknownException(throwable)
                    }
                }
            }catch (exception: Exception){
                UnknownException(throwable)
            }
        }else if(throwable is IOException ||
                throwable is SocketException ||
                throwable is SocketTimeoutException ||
                throwable is UnknownHostException ||
                throwable is NetworkErrorException
        ){
            return NetowrkException("please, check your internet connection")
        }else {
            return UnknownException(throwable)
        }
    }


    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.string()?.let {
                val moshi = Moshi.Builder().build()
                val moshiAdapter = moshi.adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}