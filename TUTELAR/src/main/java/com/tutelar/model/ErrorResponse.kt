package com.tutelar.model

import com.google.gson.annotations.SerializedName

/**
 * Created at Mar-2022
 */
data class ErrorResponse(
    @SerializedName("status")
    var status: Boolean,
    @SerializedName("message")
    val message: String
)