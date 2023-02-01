package com.tutelar.core

import android.util.Log

/**
 * Created at: Aug 2022
 * Created by: Gowtham R
 */
internal class Logger {

    companion object {
        var logEnabled: Boolean = false

        fun setMessage(message: String) {
            if (logEnabled)
                Log.e("TUTELAR", message)
        }
    }

}