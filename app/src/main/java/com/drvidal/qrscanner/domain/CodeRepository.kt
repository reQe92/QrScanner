package com.drvidal.qrscanner.domain

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import com.drvidal.qrscanner.R
import com.drvidal.qrscanner.data.code.Code
import com.drvidal.qrscanner.data.code.CodeDao
import com.drvidal.qrscanner.presentation.WebViewActivity
import com.drvidal.qrscanner.util.Constants
import com.journeyapps.barcodescanner.ScanOptions

class CodeRepository constructor(private val dao: CodeDao,
                                 private val context: Context,
                                 private val preferences: SharedPreferences,
                                 private val analyticsRepository: AnalyticsRepository) {

    fun getCodes() = dao.getCodes()

    suspend fun getCodeById(id: Int) = dao.getCodeById(id)

    suspend fun insertCode(code: Code) = dao.insertCode(code)

    suspend fun deleteCode(code: Code) = dao.deleteCode(code)

    suspend fun handleScannedCode(codeContent: String) : Code {
        val id: Int  = (dao.getLastCodeInserted() ?: 0) + 1
        val isUrl =  Patterns.WEB_URL.matcher(codeContent).matches()
        val millis = System.currentTimeMillis()
        val title = String.format(
            context.getString(R.string.scanned_number),
            id,
        )
        val code = Code(
            title,
            codeContent,
            millis,
            isUrl
        )
        dao.insertCode(code)
        analyticsRepository.logScannedQr()
        return code
    }

    fun isAutoOpenAfterScanEnabled() : Boolean {
        return preferences.getBoolean(
            Constants.PREFERENCE_AUTO_OPEN_AFTER_SCAN, true
        )
    }

    fun getScanOptions() : ScanOptions {
        val options = ScanOptions()
        options.setOrientationLocked(false)
        val beepEnabled = preferences.getBoolean(
            Constants.PREFERENCE_QR_BEEP_ENABLED, false
        )
        val cameraId = preferences.getString(Constants.PREFERENCE_CAMERA_ID, null)?.toIntOrNull()
        if (cameraId != null) {
            options.setCameraId(cameraId)
        }
        options.setBeepEnabled(beepEnabled)
        return options
    }
}