package com.tost.data.service

import com.tost.data.service.request.*
import com.tost.data.service.response.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created By Malibin
 * on 12월 11, 2020
 */

interface TostService {

    @POST("/login/google")
    suspend fun login(
        @Body loginRequestParams: TostLoginRequestParams
    ): TostLoginResponse

    @POST("/target")
    suspend fun saveGoal(
        @Header("token") token: String,
        @Body saveGoalRequestParams: SaveGoalRequestParams
    )

    @PUT("/target/solve")
    suspend fun saveWeeklyGoal(
        @Header("token") token: String,
        @Body saveWeeklyGoalParams: SaveWeeklyGoalParams,
    )

    @GET("/target")
    suspend fun getGoals(
        @Header("token") token: String
    ): Response<GoalsResponse>

    @GET("/part/{part}")
    suspend fun getMyNote(
        @Header("token") token: String,
        @Path("part") part: Int,
    ): MyNoteResponse

    @GET("/part/{part}/question/{questionNum}")
    suspend fun getProblemInfo(
        @Header("token") token: String,
        @Path("part") part: Int,
        @Path("questionNum") questionNum: Int,
    ): List<ProblemResponse>

    @GET("/experience/part/{part}")
    suspend fun getTrialProblemInfo(
        @Path("part") part: Int,
    ): List<ProblemResponse>

    @POST("/solved")
    suspend fun saveSolvedProblem(
        @Header("token") token: String,
        @Body saveSolvedProblemParams: SaveSolvedProblemParams,
    )

    @POST("/addBookmark")
    suspend fun saveBookmark(
        @Header("token") token: String,
        @Body bookmarkParams: BookmarkParams,
    )

    @DELETE("deleteBookmark")
    suspend fun deleteBookmark(
        @Header("token") token: String,
        @Body bookmarkParams: BookmarkParams,
    )

    companion object {
        const val BASE_URL = "https://teamtost.com"
    }
}
