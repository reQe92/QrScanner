package com.drvidal.qrscanner.util

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import com.drvidal.qrscanner.R
import com.drvidal.qrscanner.data.camera.Camera

object DeviceUtils {

    fun isEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("sdk_gphone64_arm64")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator"))
    }

    fun getCameras(context: Context): List<Camera> {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        val cameras = mutableListOf<Camera>()
        try {
            val cameraIds = cameraManager?.cameraIdList
            cameraIds?.map {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(it)
                when {
                    cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT -> {
                        val camera = Camera(it, context.getString(R.string.front_camera))
                        cameras.add(camera)
                    }
                    cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK -> {
                        val camera = Camera(it, context.getString(R.string.back_camera))
                        cameras.add(camera)
                    }
                    cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_EXTERNAL -> {
                        val camera = Camera(it, context.getString(R.string.external_camera))
                        cameras.add(camera)
                    }
                    else -> {}
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return cameras.toList()
    }

}