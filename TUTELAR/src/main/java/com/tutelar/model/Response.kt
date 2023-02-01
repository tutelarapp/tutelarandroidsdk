/**
 *  Created by Gowtham
 */

package com.tutelar.model

data class Response(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: Data
)

data class Data(
    val ip_address: String
)
