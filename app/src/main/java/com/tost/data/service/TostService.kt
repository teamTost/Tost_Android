package com.tost.data.service

import com.tost.data.service.request.TostLoginRequestParams
import com.tost.data.service.response.TostLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created By Malibin
 * on 12월 11, 2020
 */

interface TostService {

    @POST("/login/google")
    suspend fun login(
        @Body loginRequestParams: TostLoginRequestParams
    ): TostLoginResponse

    companion object {
        const val BASE_URL = "http://13.124.234.226:8080"
    }
}