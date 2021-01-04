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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 12월 27, 2020
 */

class Part1ViewModel @ViewModelInject constructor(
    recordsRepository: RecordsRepository,
    private val userRepository: UserRepository,
    private val problemsRepository: ProblemsRepository,
) : AudioViewModel(recordsRepository) {

    override val part: Part = Part.ONE

    private val _problem = MutableLiveData<Problem>()
    val problem: LiveData<Problem> = _problem

    private val _problemState = MutableLiveData(ProblemState.PREPARE)
    val problemState: LiveData<ProblemState> = _problemState

    fun loadProblem(problemNumber: Int) = viewModelScope.launch {
        changeLoadingTo(true)
        val tostToken = userRepository.getTostToken()
        _problem.value = if (tostToken == null) {
            problemsRepository.getTrialProblemInfo(part)
        } else {
            problemsRepository.getProblemInfo(tostToken, part, problemNumber)
        }
        changeLoadingTo(false)
    }

    fun changeState(state: ProblemState) {
        _problemState.value = state
    }

    fun saveSolvedProblem() = CoroutineScope(Dispatchers.IO).launch {
        val tostToken = userRepository.getTostToken() ?: return@launch
        val currentProblemNumber = _problem.value?.questionNumber ?: error("problem is null")
        problemsRepository.saveSolvedProblem(tostToken, part, currentProblemNumber)
    }
}
