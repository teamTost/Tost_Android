package com.tost.presentation.problem.part1

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tost.data.entity.Part
import com.tost.data.entity.Problem
import com.tost.data.entity.ProblemState
import com.tost.data.repository.ProblemsRepository
import com.tost.data.repository.RecordsRepository
import com.tost.data.repository.UserRepository
import com.tost.presentation.problem.base.AudioViewModel
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 12월 27, 2020
 */

class Part1ViewModel @ViewModelInject constructor(
    recordsRepository: RecordsRepository,
    private val userRepository: UserRepository,
    private val problemsRepository: ProblemsRepository
) : AudioViewModel(recordsRepository) {

    override val part: String = "Part1"

    private val _problem = MutableLiveData<Problem>()
    val problem: LiveData<Problem>
        get() = _problem

    private val _problemState = MutableLiveData(ProblemState.PREPARE)
    val problemState: LiveData<ProblemState>
        get() = _problemState

    fun loadProblem(problemNumber: Int) = viewModelScope.launch {
        changeLoadingTo(true)
        val tostToken = getTostToken()
        _problem.value = problemsRepository.getProblemInfo(tostToken, Part.ONE, problemNumber)
        changeLoadingTo(false)
    }

    fun changeState(state: ProblemState) {
        _problemState.value = state
    }
    //TODO 토큰이 없으면 체험하기니까 체험하기로 문제를 따로 뽑아오는게 맞는거같다.
    private suspend fun getTostToken(): String = userRepository.getTostToken()
        ?: throw IllegalStateException("Login Requested")
}
