package com.tost.data.repository

import com.tost.data.entity.Part
import com.tost.data.entity.Problem
import com.tost.data.service.TostService
import com.tost.data.service.response.toProblem
import javax.inject.Inject

/**
 * Created By Malibin
 * on 12월 27, 2020
 */

class ProblemsRepository @Inject constructor(
    private val tostService: TostService,
) {
    suspend fun getProblemInfo(token: String, part: Part, problemNumber: Int): Problem {
        return tostService.getProblemInfo(token, part.number, problemNumber).toProblem()
    }
}
