package com.shashankmunda.pawpics.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel:ViewModel() {
    protected val ioScope = CoroutineScope(Dispatchers.IO)

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val defaultScope = CoroutineScope(Dispatchers.Default)
    override fun onCleared() {
        super.onCleared()
        ioScope.cancel()
        mainScope.cancel()
        defaultScope.cancel()
    }
}