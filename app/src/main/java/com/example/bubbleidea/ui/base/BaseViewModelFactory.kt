package com.example.bubbleidea.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bubbleidea.helpers.VIEW_MODEL_EXCEPTION_MESSAGE

abstract class BaseViewModelFactory<T : ViewModel>(private val classVM: Class<T>) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        validateClass(modelClass)
        return createViewModel() as T
    }

    private fun <T: ViewModel?> validateClass(modelClass: Class<T>) {
        require(modelClass.isAssignableFrom(classVM)) {VIEW_MODEL_EXCEPTION_MESSAGE}
    }
    abstract fun createViewModel(): T
}