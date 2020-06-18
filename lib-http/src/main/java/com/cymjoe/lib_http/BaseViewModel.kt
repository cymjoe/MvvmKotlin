package com.cymjoe.lib_http

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import kotlinx.coroutines.*

open class BaseViewModel : ViewModel() {
    data class UiModel(
        val loading: Boolean,//是否显示加载框
        val showErrorView: Boolean,//是否显示错误信息
        val errorMsg: String? = "",//错误信息
        val showNoNetView: Boolean,//是否显示无网络信息
        val showNoDataView: Boolean //是否显示默认空页面
    )

    private val _uiState = MutableLiveData<UiModel>()

    val uiState: LiveData<UiModel> = _uiState

    fun emitUiState(
        loading: Boolean = false,
        showErrorView: Boolean = false,//是否显示错误信息
        errorMsg: String = "",//错误信息
        showNoNetView: Boolean = false,//是否显示无网络信息
        showNoDataView: Boolean = false//是否显示默认空页面
    ) {
        val uiModel =
            UiModel(
                loading,
                showErrorView,
                errorMsg,
                showNoNetView,
                showNoDataView
            )
        _uiState.value = uiModel
    }

    val mException: MutableLiveData<APIException> = MutableLiveData()

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {

        viewModelScope.launch { block() }

    }
    

    suspend fun <T> launchOnIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block
        }
    }

    fun launch(tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, true)
        }
    }


    fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(APIException) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        launchOnUI {
            tryCatch(tryBlock, catchBlock, finallyBlock, handleCancellationExceptionManually)
        }
    }

    fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, handleCancellationExceptionManually)
        }
    }


    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(APIException) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: APIException) {
                emitUiState(loading = false)
                if (handleCancellationExceptionManually) {
                    catchBlock(e)
                } else {
                    throw e
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitUiState(loading = false)
                val apiException = APIException(500, "连接超时")
                mException.value = apiException
                catchBlock(apiException)

            } finally {
                finallyBlock()
            }
        }
    }
}