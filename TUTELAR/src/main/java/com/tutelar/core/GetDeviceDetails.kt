package com.tutelar.core
/**
 * Created at: Aug 2022
 * Created by: Gowtham R
 */
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import com.google.gson.GsonBuilder
import com.tutelar.utils.*
import com.tutelar.utils.getDeviceId
import com.tutelar.utils.getIPAddress
import com.tutelar.utils.getImeiNumber
import com.tutelar.utils.isEmulator
import java.util.TimeZone

@SuppressLint("NewApi")
internal fun getDetails(activity: Activity): String {
     val device = mutableMapOf(
        "deviceId" to getDeviceId(activity),
        "platform" to Build.DEVICE,
        "is_real_device" to !isEmulator(),
        "deviceCharging" to isDeviceCharging(activity),
        "batteryLevel" to getBatteryLevel(activity),
        "devicePixelRatio" to getScreenResolution(activity),
        "screenWidth" to getScreenWidth(activity),
        "screenHeight" to getScreenHeight(activity),
        "touchSupport" to isTouchScreen(activity),
        "deviceMemory" to getTotalMemory(activity),
        "os" to getOsName(activity),
        "deviceTypes" to mapOf(
           "isMobile" to true,
           "isDesktop" to false,
           "isLinux" to false,
           "isLinux64" to false,
           "isMac" to false,
           "isSmartTV" to false,
           "isTablet" to false,
           "isWindows" to false,
           "isiPad" to false,
           "isiPhone" to false,
           "isiPod" to false
        ),
        "displayResolution" to getScreenResolution(activity),
        "audioHardware" to mapOf("hasSpeakers" to isSpeakerAvailable(activity)),
        "microPhoneHardware" to mapOf("hasMicrophone" to true),
        "videoHardware" to mapOf("hasWebCam" to true),
        "android_id" to Build.ID,
        "board" to Build.BOARD,
        "brand" to Build.BRAND,
        "display" to Build.DISPLAY,
        "boot_loader" to Build.BOOTLOADER,
        "finger_print" to Build.FINGERPRINT,
        "hardware" to Build.HARDWARE,
        "host" to Build.HOST,
        "manufacturer" to Build.MANUFACTURER,
        "model" to Build.MODEL,
        "product" to Build.PRODUCT,
        "user" to Build.USER,
        "type" to Build.TYPE,
        "is_emulator" to isEmulator(),
        "ip_address" to getIPAddress(),
        "imei_number" to getImeiNumber(activity),
        "data_enabled" to isDataEnabled(activity),
        "serial_number" to getSimSerialNumber(activity),
        "network_type" to getNetworkType(activity),
        "data_roaming" to isRoamingEnabled(activity),
        "root_status" to isRooted(),
        "memory_information" to getMemoryInfo(activity),
        "kernel_version" to getKernelVersion(),
        "proximity_sensor" to getProximitySensor(activity),
        "language_info" to getDeviceLanguage(),
        "country_info" to getDeviceCountry(),
        "location_info" to getDeviceLocation(),
        "system_storage" to getStorageInfo(activity),
        "external_storage" to getSdCardInfo(activity),
        "screen_brightness" to getScreenBrightness(activity),
        "screen_size" to getScreenSize(activity),
        "developer_mode_enabled" to isDevMode(activity),
        "timestamp" to System.currentTimeMillis(),
        "timezone" to TimeZone.getDefault().id
    )
    device["deviceHash"] = getHashValue(device)
    val data = mutableMapOf(
       "status" to true,
       "ipAddress" to Constants.ipAddress,
       "device" to device,
       "time" to mapOf("timestamp" to System.currentTimeMillis(), "timezone" to TimeZone.getDefault().id))
    return GsonBuilder().setPrettyPrinting().create().toJson(data)
}
