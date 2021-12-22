package com.ahmed_abobakr.musalaweatherapp.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer


abstract class BaseFragment: Fragment() {


    protected abstract val  viewModel: BaseViewModel
    private lateinit var loadingDialog: LoadingDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleCloudError(viewModel)

        loadingDialog = LoadingDialog()
        loadingDialog.isCancelable = false

        viewModel.mIsLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (!loadingDialog.isAdded && !loadingDialog.isVisible) {
                    loadingDialog.showNow(requireActivity().supportFragmentManager, "loading_dialog")
                }
            } else {
                if (loadingDialog.isVisible) {
                    loadingDialog.dismiss()
                }
            }
        })
    }

    open  fun handleCloudError(viewModel: BaseViewModel) {
        viewModel.mCloudException.observe(viewLifecycleOwner, Observer{ errorResponseDefaultResponseModel ->
            viewModel.isLoading(false)
            try {
                showFailureMessage(errorResponseDefaultResponseModel.responseMessage?: "")
            } catch (e: Exception) {
                e.printStackTrace()
                showFailureMessage("something went wrong, please try again later")
            }
        })
    }

    private fun showFailureMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


}