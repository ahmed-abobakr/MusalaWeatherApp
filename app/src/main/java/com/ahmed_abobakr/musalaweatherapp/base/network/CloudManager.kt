package com.ahmed_abobakr.musalaweatherapp.base.network

import android.accounts.NetworkErrorException
import com.ahmed_abobakr.musalaweatherapp.base.exceptions.ApiException
import com.ahmed_abobakr.musalaweatherapp.base.exceptions.AuthException
import com.ahmed_abobakr.musalaweatherapp.base.exceptions.UnknownException

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

open class CloudManager: ApiManager {

    @Inject
    lateinit var api: MusalaWeatherAPI

    override fun mapException(throwable: Throwable): Throwable {
        if(throwable is HttpException){
            val response = throwable.response()
            return try {
                val errorBodyString = response?.errorBody()?.string()
                if(throwable.code() == 401) AuthException(errorBodyString!!, throwable)
                else{
                    if(errorBodyString != null){
                        ApiException(errorBodyString)
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
            return NetworkErrorException(throwable)
        }else {
            return UnknownException(throwable)
        }
    }



    override fun <T> execute(apiCall: () -> T): DefaultResponseModel<T> {
        return try {
            DefaultResponseModel(Status.SUCCESS, apiCall.invoke())
        }catch (throwable: Throwable){
            when(throwable){
                is IOException -> DefaultResponseModel(Status.NETWORK_EXCEPTION)
                is HttpException -> {
                    val code = throwable.code()
                    if(code == 401)
                        DefaultResponseModel(Status.AUTH_EXCEPTION)
                    else{
                        val message = throwable.message()
                        DefaultResponseModel(Status.API_EXCEPTION,null, message, code)
                    }
                }
                else ->{
                    throwable.printStackTrace()
                    DefaultResponseModel(Status.UNKONWN_EXCEPTION)
                }
            }
        }
        /*return observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<T> {
                try {
                    DefaultResponseModel(Status.SUCCESS, apiCall.invoke())
                }catch (throwable: Throwable){
                    when(throwable){
                        is IOException -> DefaultResponseModel(Status.NETWORK_EXCEPTION)
                        is HttpException -> {
                            val code = throwable.code()
                            if(code == 401)
                                DefaultResponseModel(Status.AUTH_EXCEPTION)
                            else{
                                val message = throwable.message()
                                DefaultResponseModel(Status.API_EXCEPTION,null, message, code)
                            }
                        }
                        else ->{
                            throwable.printStackTrace()
                            DefaultResponseModel(Status.UNKONWN_EXCEPTION)
                        }
                    }
                }
            },
                Consumer<Throwable> {throwable ->
                    when(throwable){
                        is IOException -> DefaultResponseModel(Status.NETWORK_EXCEPTION)
                        is HttpException -> {
                            val code = throwable.code()
                            if(code == 401)
                                DefaultResponseModel(Status.AUTH_EXCEPTION)
                            else{
                                val message = throwable.message()
                                DefaultResponseModel(Status.API_EXCEPTION,null, message, code)
                            }
                        }
                        else ->{
                            throwable.printStackTrace()
                            DefaultResponseModel(Status.UNKONWN_EXCEPTION)
                        }
                    }
                })*/
    }

    /*override fun getPlaceByCategories(categoryID: Int): Observable<CategoryPlaceModel> {
        return execute { api.getPlaceByCategories(categoryID) }
    }*/

}