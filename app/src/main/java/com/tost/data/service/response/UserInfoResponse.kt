package com.tost.data.service.response

/**
 * Created By Malibin
 * on 3월 23, 2021
 */

data class UserInfoResponse(
    val userIdx: Int,
    val name: String,
    val nickname: String,
    val email: String,
    val token: String,
    val fcmToken: String,
)
