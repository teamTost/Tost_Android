package com.tost.presentation.problem.part6

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
import com.tost.presentation.utils.printLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 1월 05, 2021
 */

class Part6ViewModel @ViewModelInject constructor(
    recordsRepository: RecordsRepository,
    private val userRepository: UserRepository,
    private val problemsRepository: ProblemsRepository,
) : AudioViewModel(recordsRepository) {

    override val part: Part = Part.SIX

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
        printLog(_problem.value.toString())
        changeLoadingTo(false)
    }

    fun getProblemSoundUrl(): String {
        return _problem.value?.audioUrl ?: error("audioUrl Not Exist")
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
