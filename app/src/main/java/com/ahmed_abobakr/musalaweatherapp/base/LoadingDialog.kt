package com.ahmed_abobakr.musalaweatherapp.base

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ahmed_abobakr.musalaweatherapp.R


class LoadingDialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onResume() {
        super.onResume()
        dialog?.let {
            it.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.window?.setBackgroundDrawable(ColorDrawable(requireContext().resources.getColor(android.R.color.transparent)))
        }
    }
}