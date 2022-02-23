package com.drvidal.qrscanner.presentation.code

import com.drvidal.qrscanner.data.code.Code
import com.journeyapps.barcodescanner.ScanOptions

sealed class UiEvent {
    data class ScanCode(val options: ScanOptions): UiEvent()
    data class LaunchWebView(val code: Code): UiEvent()
    data class ShowSnackbar(val code: Code): UiEvent()
}