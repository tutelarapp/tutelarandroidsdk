package com.tutelar.utils
/**
 * Created at: Aug 2022
 * Created by: Gowtham R
 */

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Context.SENSOR_SERVICE
import android.content.pm.PackageManager
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorManager
import android.media.AudioManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.tutelar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.math.BigInteger
import java.net.InetAddress
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * Get current id address
 */
internal fun getIPAddress(): String {
    try {
        val interfaces: List<NetworkInterface> =
            Collections.list(NetworkInterface.getNetworkInterfaces())
        for (inter in interfaces) {
            val addresses: List<InetAddress> = Collections.list(inter.inetAddresses)
            for (address in addresses) {
                if (!address.isLoopbackAddress) {
                    val sAddress: String? = address.hostAddress
                    if (sAddress != null) {
                        val isIPv4 = sAddress.indexOf(':') < 0
                        try {
                            if (isIPv4) {
                                return sAddress
                            } else {
                                val data = sAddress.indexOf('%')
                                if (data < 0) sAddress.uppercase() else sAddress.substring(0, data)
                                    .uppercase()
                            }
                        }catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    } catch (e: Exception) { }
    return ""
}

/**
 * Get the device status is real or emulator
 */
internal fun isEmulator(): Boolean {
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

/**
 * Get device unique id
 */
@SuppressLint("HardwareIds")
internal fun getDeviceId(activity: Activity): String {
    return try {
        Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
    }catch (e: Exception) {
        ""
    }
}

/**
 * Get IMEI Number
 */
@SuppressLint("MissingPermission", "NewApi")
internal fun getImeiNumber(activity: Activity): String {
    return try {
        getTelephonyManager(activity).imei.toString()
    }catch (e: Exception) {
        ""
    }
}

/**
 * Get the status of network enabled or not
 */
@SuppressLint("NewApi", "MissingPermission")
internal fun isDataEnabled(activity: Activity): String {
    return try {
        getTelephonyManager(activity).isDataEnabled.toString()
    }catch (e: Exception) {
        ""
    }
}

/**
 * Get the sim card serial number
 */
@SuppressLint("NewApi", "MissingPermission", "HardwareIds")
internal fun getSimSerialNumber(activity: Activity): String {
    return try {
        getTelephonyManager(activity).simSerialNumber.toString()
    }catch (e: Exception) {
        ""
    }
}

@SuppressLint("NewApi", "MissingPermission")
internal fun getAllCellInfo(activity: Activity): String {
    return try {
        getTelephonyManager(activity).allCellInfo.toString()
    }catch (e: Exception) {
        ""
    }
}

@SuppressLint("NewApi", "MissingPermission")
internal fun getNetworkType(activity: Activity): String {
    return try {
        getTelephonyManager(activity).dataNetworkType.toString()
    }catch (e: Exception) {
        ""
    }
}

internal fun isRooted(): Boolean {
    val isEmulator = isEmulator()
    val buildTags = Build.TAGS
    return if (!isEmulator && buildTags != null && buildTags.contains("test-keys")) {
        true
    } else {
        var file = File("/system/app/Superuser.apk")
        if (file.exists()) {
            true
        } else {
            file = File("/system/xbin/su")
            !isEmulator && file.exists()
        }
    }
}

internal fun getMemoryInfo(activity: Activity): Map<String, Any> {
    return try {
        val memoryInfo = ActivityManager.MemoryInfo()
        (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        mapOf(
            "total_memory" to Formatter.formatFileSize(activity, memoryInfo.totalMem),
            "available_memory" to Formatter.formatFileSize(activity, memoryInfo.availMem),
            "used_memory" to Formatter.formatFileSize(activity, memoryInfo.totalMem - memoryInfo.availMem)
        )
    }catch (e: Exception) {
        emptyMap()
    }
}

internal fun getTotalMemory(activity: Activity): String {
    return try {
        val memoryInfo = ActivityManager.MemoryInfo()
        (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        Formatter.formatFileSize(activity, memoryInfo.totalMem)
    }catch (e: Exception) {
        ""
    }
}

internal fun getOsName(activity: Activity): String {
    var codeName = "UNKNOWN"
    try {
        val fields = Build.VERSION_CODES::class.java.fields
        fields.filter { it.getInt(Build.VERSION_CODES::class) == Build.VERSION.SDK_INT }
            .forEach { codeName = "Android " + it.name }
    }catch (_: Exception) {}
    return codeName
}

internal  fun getKernelVersion(): String {
    return try {
        System.getProperty("os.version")!!.toString()
    }catch (e: Exception) {
        ""
    }
}

@SuppressLint("NewApi")
internal fun getProximitySensor(activity: Activity): Map<String, Any> {
    val mSensorManager = activity.getSystemService(SENSOR_SERVICE) as SensorManager
    val mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    return try {
       mapOf(
           "id" to mSensor.id,
           "is_dynamic_sensor" to mSensor.isDynamicSensor,
           "is_wakeup_sensor" to mSensor.isWakeUpSensor,
           "name" to mSensor.name,
           "resolution" to mSensor.resolution,
           "type" to mSensor.type,
           "string_type" to mSensor.stringType,
           "reporting_mode" to mSensor.reportingMode,
           "vendor" to mSensor.vendor,
           "version" to mSensor.version,
           "power" to mSensor.power,
           "max_delay" to mSensor.maxDelay,
           "min_delay" to mSensor.minDelay,
           "maximum_range" to mSensor.maximumRange
       )
    }catch (e: Exception) {
        emptyMap()
    }
}

@SuppressLint("NewApi", "MissingPermission")
internal fun isRoamingEnabled(activity: Activity): String {
    return try {
        getTelephonyManager(activity).isDataRoamingEnabled.toString()
    }catch (e: Exception) {
        ""
    }
}

internal fun getBatteryLevel(activity: Activity): String {
    val batteryManager = activity.getSystemService(BATTERY_SERVICE) as BatteryManager
    return try {
        batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY).toString()
    }catch (e: Exception) {
        ""
    }
}

@RequiresApi(Build.VERSION_CODES.M)
internal fun isDeviceCharging(activity: Activity): Boolean {
    return try {
        val batteryManager = activity.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        batteryManager.isCharging
    }catch (e: Exception) {
        false
    }
}

internal fun getTelephonyManager(activity: Activity): TelephonyManager {
    return activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
}

internal fun getDeviceLanguage(): Map<String, String> {
    val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.locales[0]
    }else {
        Resources.getSystem().configuration.locale
    }
    return mapOf(
        "language" to locale.language,
        "display_language" to locale.displayLanguage,
        "iso_3_language" to locale.isO3Language
    )
}

internal fun getDeviceCountry(): Map<String, String> {
    val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.locales[0]
    }else {
        Resources.getSystem().configuration.locale
    }
    return mapOf(
        "country" to locale.country,
        "display_country" to locale.displayCountry,
        "iso_3_country" to locale.isO3Country
    )
}

internal fun getDeviceLocation(): Map<String, String> {
    return mapOf(
        "latitude" to Constants.latitude,
        "longitude" to Constants.longitude,
        "address" to Constants.address
    )
}

internal fun getStorageInfo(activity: Activity): Map<String, Any> {
    return mapOf(
        "total" to Formatter.formatFileSize(activity, getTotalStorageInfo(Environment.getRootDirectory().path)),
        "used" to Formatter.formatFileSize(activity, getUsedStorageInfo(Environment.getRootDirectory().path))
    )
}

internal fun getSdCardInfo(activity: Activity): Map<String, Any> {
    val files = ContextCompat.getExternalFilesDirs(activity, null)
    if (files.size > 1 && files[0] != null && files[1] != null) {
        return mapOf(
            "total" to Formatter.formatFileSize(activity, getTotalStorageInfo(files[1]!!.path)),
            "used" to Formatter.formatFileSize(activity, getUsedStorageInfo(files[1]!!.path))
        )
    }
    return emptyMap()
}

internal fun getTotalStorageInfo(path: String?): Long {
    return StatFs(path).totalBytes
}

internal fun getUsedStorageInfo(path: String?): Long {
    val statFs = StatFs(path)
    return statFs.totalBytes - statFs.availableBytes
}

internal fun getScreenBrightness(activity: Activity): String {
    return try {
        Settings.System.getInt(activity.contentResolver, Settings.System.SCREEN_BRIGHTNESS).toString()
    }catch (e: Exception) {
        ""
    }
}

internal fun getScreenSize(activity: Activity): String {
    return try {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val x = (dm.widthPixels / dm.xdpi).toDouble().pow(2.0)
        val y = (dm.heightPixels / dm.ydpi).toDouble().pow(2.0)
        sqrt(x + y).toString()
    }catch (e: Exception) {
        ""
    }
}

internal fun getScreenResolution(activity: Activity): String {
    return try {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        "${dm.widthPixels} x ${dm.heightPixels}"
    }catch (e: Exception) {
        ""
    }
}

internal fun getScreenWidth(activity: Activity): String {
    return try {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        dm.widthPixels.toString()
    }catch (e: Exception) {
        ""
    }
}

internal fun getScreenHeight(activity: Activity): String {
    return try {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        dm.heightPixels.toString()
    }catch (e: Exception) {
        ""
    }
}

internal fun isSpeakerAvailable(activity: Activity): Boolean {
    return try {
        val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        val packageManager: PackageManager = activity.packageManager
        if (audioManager!!.isBluetoothA2dpOn) {
            true
        } else if (audioManager.isBluetoothScoOn) {
            true
        } else if (audioManager.isWiredHeadsetOn) {
            true
        } else if (audioManager.isSpeakerphoneOn) {
            true
        } else packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)
    }catch (e: Exception) {
        false
    }
}

internal fun isTouchScreen(activity: Activity): Boolean {
    return try {
        activity.packageManager.hasSystemFeature("android.hardware.touchscreen")
    }catch (e: Exception) {
        false
    }
}

/**
 * Get developer mode enabled or not
 */
internal fun isDevMode(context: Activity): Boolean {
    return Settings.Secure.getInt(context.contentResolver,
        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0
}

internal fun getHashValue(data: MutableMap<String, Any>): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(data.toString().toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}