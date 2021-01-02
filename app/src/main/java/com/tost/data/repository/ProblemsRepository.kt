package com.tost.data.repository

import com.tost.data.entity.MyNote
import com.tost.data.entity.Part
import com.tost.data.entity.Problem
import com.tost.data.service.TostService
import com.tost.data.service.response.toProblem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By Malibin
 * on 12월 27, 2020
 */

class ProblemsRepository @Inject constructor(
    private val tostService: TostService,
) {
    suspend fun getMyNote(token: String, part: Part): MyNote {
        return withContext(Dispatchers.IO) {
            tostService.getMyNote(token, part.number).toMyNote()
        }
    }

    suspend fun getProblemInfo(token: String, part: Part, problemNumber: Int): Problem {
        return withContext(Dispatchers.IO) {
            tostService.getProblemInfo(token, part.number, problemNumber).toProblem()
        }
    }

    suspend fun getTrialProblemInfo(part: Part): Problem {
        return withContext(Dispatchers.IO) {
            tostService.getTrialProblemInfo(part.number).toProblem()
        }
    }
}
