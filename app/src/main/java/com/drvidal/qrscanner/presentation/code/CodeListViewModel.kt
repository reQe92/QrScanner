package com.drvidal.qrscanner.presentation.code

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drvidal.qrscanner.data.code.Code
import com.drvidal.qrscanner.domain.CodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalMaterialApi
@HiltViewModel
class CodeListViewModel @Inject constructor(private val codeRepository: CodeRepository) :
    ViewModel() {

    var codes = mutableStateOf<List<Code>>(emptyList())
        private set

    private var recentlyDeletedCode: Code? = null

    var codeDetailDialog = mutableStateOf(false)
        private set

    var codeRenameDialog = mutableStateOf(false)
        private set

    var lastSelectedCode = mutableStateOf<Code?>(null)
        private set

    var newCodeName = mutableStateOf("")
        private set

    var isLoading = mutableStateOf(true)
        private set


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        codeRepository
            .getCodes()
            .onEach {
                codes.value = it
                isLoading.value = false
            }.launchIn(viewModelScope)
    }

    fun onScanClick() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.ScanCode(codeRepository.getScanOptions()))
        }
    }


    fun handleScannedCode(codeContent: String) {
        viewModelScope.launch {
            val code = codeRepository.handleScannedCode(codeContent)
            if (codeRepository.isAutoOpenAfterScanEnabled()) {
                onCodeClick(code)
            }
        }
    }

    fun onCodeClick(code: Code) {
        viewModelScope.launch {
            if (code.isUrl) {
                _uiEvent.send(UiEvent.LaunchWebView(code))
            } else {
                codeDetailDialog.value = true
                lastSelectedCode.value = code
            }
        }
    }

    fun onDetailDialogClose() {
        codeDetailDialog.value = false
        lastSelectedCode.value = null
    }

    fun onRenameDialogClose() {
        codeRenameDialog.value = false
        lastSelectedCode.value = null
    }

    fun onMoreClick(code: Code) {
        viewModelScope.launch {
            lastSelectedCode.value = code
        }
    }

    fun onRenameClick() {
        newCodeName.value = lastSelectedCode.value?.title ?: ""
        codeRenameDialog.value = true
    }

    fun onRenameValueChange(value: String) {
        newCodeName.value = value
    }

    fun renameCode() {
        val code = lastSelectedCode.value
        if (code != null) {
            code.title = newCodeName.value
            code.timestamp = System.currentTimeMillis()
            viewModelScope.launch {
                codeRepository.insertCode(code)
            }
        }
    }

    fun deleteCode(code: Code) {
        viewModelScope.launch {
            codeRepository.deleteCode(code)
            recentlyDeletedCode = code
            _uiEvent.send(UiEvent.ShowSnackbar(code = code))
        }
    }

    fun restoreLastDeletedCode() {
        viewModelScope.launch {
            codeRepository.insertCode(recentlyDeletedCode ?: return@launch)
            recentlyDeletedCode = null
        }
    }

}