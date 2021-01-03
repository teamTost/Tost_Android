package com.tost.data.repository

import com.tost.data.entity.MyNote
import com.tost.data.entity.Part
import com.tost.data.entity.Problem
import com.tost.data.service.TostService
import com.tost.data.service.request.BookmarkParams
import com.tost.data.service.request.SaveSolvedProblemParams
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

    suspend fun saveSolvedProblem(token: String, part: Part, problemNumber: Int) {
        return withContext(Dispatchers.IO) {
            val params = SaveSolvedProblemParams(part.number.toString(), problemNumber.toString())
            tostService.saveSolvedProblem(token, params)
        }
    }

    suspend fun saveBookmark(token: String, part: Part, problemNumber: Int) {
        return withContext(Dispatchers.IO) {
            val params = BookmarkParams(part.number, problemNumber)
            tostService.saveBookmark(token, params)
        }
    }

    suspend fun deleteBookmark(token: String, part: Part, problemNumber: Int) {
        return withContext(Dispatchers.IO) {
            val params = BookmarkParams(part.number, problemNumber)
            tostService.deleteBookmark(token, params)
        }
    }
}
