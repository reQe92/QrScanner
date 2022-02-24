package com.drvidal.qrscanner.domain

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase

class AnalyticsRepository {

    fun logException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    fun logScannedQr() {
        Firebase.analytics.logEvent("qr_scanned", null)
    }

}