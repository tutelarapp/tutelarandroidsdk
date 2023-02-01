package com.tutelar.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tutelar.Tutelar

/**
 * Created at: Aug 2022
 * Created by: Gowtham R
 */
internal class PermissionManager : AppCompatActivity() {

    var permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun checkPermission(activity: Activity): Boolean {
        return ActivityCompat.checkSelfPermission(activity, permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, permissions[2]) == PackageManager.PERMISSION_GRANTED
    }

    fun askPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, permissions, 123)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Location(this).getCurrentLocation()
            }
        }
    }
}