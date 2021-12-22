package com.ahmed_abobakr.musalaweatherapp.base

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer

abstract class BaseActivity: AppCompatActivity() {

    protected abstract val  viewModel: BaseViewModel

    open  fun handleCloudError(viewModel: BaseViewModel) {
        viewModel.mCloudException.observe(this, Observer{ errorResponseDefaultResponseModel ->
            viewModel.isLoading(false)
            try {
                showFailureMessage(errorResponseDefaultResponseModel.responseMessage?: "")
            } catch (e: Exception) {
                e.printStackTrace()
                showFailureMessage("Unexpected error, please try again later")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val loadingDialog = LoadingDialog()
        loadingDialog.isCancelable = false

        viewModel.mIsLoading.observe(this, Observer {
            if (it) {
                if (!loadingDialog.isAdded && !loadingDialog.isVisible)
                    loadingDialog.show(this.supportFragmentManager, "loading_dialog")
            } else {
                if (loadingDialog.isVisible) {
                    loadingDialog.dismiss()
                }
            }
        })
    }

    private fun showFailureMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


}