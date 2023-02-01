package com.tutelar

import android.app.Activity
import com.google.gson.GsonBuilder
import com.tutelar.core.Logger
import com.tutelar.core.getDetails
import com.tutelar.network.ApiClient
import com.tutelar.utils.Constants
import com.tutelar.utils.Location
import com.tutelar.utils.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created at: Aug 2022
 * Created by: Gowtham R
 */
object Tutelar {

    private val scope = CoroutineScope(Dispatchers.Main)

    fun init(activity: Activity, key: String) {
        if (!PermissionManager().checkPermission(activity)) PermissionManager().askPermission(activity) else Location(activity).getCurrentLocation()
        scope.launch {
            val response = ApiClient.getClient.isValidApiKey(key)
            Constants.isValidClient = response.body()?.status == true
            Constants.ipAddress = response.body()?.data?.ip_address.toString()
        }
    }

    fun getDeviceDetails(activity: Activity): String {
        return if (Constants.isValidClient) getDetails(activity)
        else {
            GsonBuilder().create().toJson(mutableMapOf(
                "status" to false,
                "error" to "Invalid Api Key"
            ))
        }
    }

}