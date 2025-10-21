package com.jessevandenberghe.milkyway.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.provider.Settings
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log

private var currentActivity: Activity? = null

@Composable
actual fun InitializeBrightnessControl() {
    currentActivity = LocalContext.current as Activity
}

actual fun setBrightness(brightness: Float) {
    currentActivity?.let { activity ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(activity)) {
                val brightnessValue = (brightness * 255).toInt().coerceIn(0, 255)
                Settings.System.putInt(
                    activity.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightnessValue
                )
            } else {
                Log.d("BrightnessControl", "WRITE_SETTINGS permission not granted, opening settings")
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + activity.packageName)
                activity.startActivity(intent)
            }
        } else {
            val brightnessValue = (brightness * 255).toInt().coerceIn(0, 255)
            Settings.System.putInt(
                activity.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                brightnessValue
            )
        }
    }
}
