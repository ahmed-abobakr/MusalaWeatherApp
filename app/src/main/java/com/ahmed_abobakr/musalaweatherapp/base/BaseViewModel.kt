package com.ahmed_abobakr.musalaweatherapp.base

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmed_abobakr.musalaweatherapp.base.di.DaggerAppComponent
import com.ahmed_abobakr.musalaweatherapp.base.exceptions.ApiException
import com.ahmed_abobakr.musalaweatherapp.base.exceptions.AuthException
import com.ahmed_abobakr.musalaweatherapp.base.network.DefaultResponseModel
import com.ahmed_abobakr.musalaweatherapp.base.network.ErrorResponse
import com.ahmed_abobakr.musalaweatherapp.base.network.MusalaWeatherService
import com.ahmed_abobakr.musalaweatherapp.base.network.Status
import com.squareup.moshi.Moshi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

open class BaseViewModel: ViewModel() {

    @Inject
    protected lateinit var mDataManager: DataManager
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO )

    init {
        DaggerAppComponent.create().inject(this)
    }

    protected val _mIsLoading = MutableLiveData<Boolean>(false)
    val mIsLoading: LiveData<Boolean>
        get() = _mIsLoading
    protected val _mCloudException = MutableLiveData<DefaultResponseModel<ErrorResponse>>()
    val mCloudException: LiveData<DefaultResponseModel<ErrorResponse>>
        get() = _mCloudException


    public fun isLoading(loading: Boolean){
        _mIsLoading.value = loading
    }

    protected fun handleCloudHttpErrors(throwable: Throwable){
        _mIsLoading.postValue(false)
        val th = MusalaWeatherService.mapException(throwable)
        _mCloudException.value = when(th){
            is IOException -> DefaultResponseModel(Status.NETWORK_EXCEPTION)
            is AuthException -> {
                DefaultResponseModel<ErrorResponse>(Status.API_EXCEPTION, null, (th as AuthException).message, 401)
            }
            is ApiException -> {
                DefaultResponseModel<ErrorResponse>(Status.API_EXCEPTION, null, th.message, th.errorCode)
            }
            else ->{
                th.printStackTrace()
                _mIsLoading.postValue(false)
                DefaultResponseModel(Status.UNKONWN_EXCEPTION, responseMessage = th.message)
            }
        }

    }

    fun hideLoading(){
        _mIsLoading.postValue(false)
    }

}