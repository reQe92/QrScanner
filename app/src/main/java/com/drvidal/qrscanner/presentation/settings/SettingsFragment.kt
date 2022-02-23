package com.drvidal.qrscanner.presentation.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.drvidal.qrscanner.BuildConfig
import com.drvidal.qrscanner.R
import com.drvidal.qrscanner.util.Constants
import com.drvidal.qrscanner.util.DeviceUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_screen, rootKey)

        findPreference<Preference>(Constants.PREFERENCE_VERSION)?.summary = getVersion()
        findPreference<Preference>(Constants.PREFERENCE_SHARE_APP)?.setOnPreferenceClickListener {
            shareApplication()
            true
        }

        val cameras = DeviceUtils.getCameras(requireContext())
        val cameraIdListPreference = findPreference<ListPreference>(Constants.PREFERENCE_CAMERA_ID)
        val entryValues = cameras.map { it.id }.toTypedArray()
        val entries = cameras.map { it.title }.toTypedArray()
        cameraIdListPreference?.entries = entries
        cameraIdListPreference?.entryValues = entryValues
    }


    private fun shareApplication() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        val appUrl = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            String.format(getString(R.string.checkout_app), appUrl)
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    private fun getVersion(): String? {
        try {
            val pInfo = requireContext().packageManager.getPackageInfo(
                requireContext().packageName, 0
            )
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}