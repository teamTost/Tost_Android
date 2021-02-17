package com.tost.presentation.problem.part3

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
 * on 1월 08, 2021
 */

class Part3ViewModel @ViewModelInject constructor(
    recordsRepository: RecordsRepository,
    private val userRepository: UserRepository,
    private val problemsRepository: ProblemsRepository,
) : AudioViewModel(recordsRepository) {

    override val part: Part = Part.THREE

    private val _problem = MutableLiveData<Problem>()
    val problem: LiveData<Problem> = _problem

    private val _problemState = MutableLiveData(ProblemState.PREPARE)
    val problemState: LiveData<ProblemState> = _problemState

    private val _subProblemNumber = MutableLiveData(Problem.SubNumber.ONE)
    var subProblemNumber: LiveData<Problem.SubNumber> = _subProblemNumber

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

    fun isFinishSubProblems(): Boolean = currentSubProblemNumber() == Problem.SubNumber.FINISH

    fun getProblemSoundUrl(): String {
        return _problem.value?.audioUrl ?: error("audioUrl Not Exist")
    }

    fun getSubProblemSoundUrl(): String {
        return _problem.value?.getSubProblem(currentSubProblemNumber())?.audioUrl
            ?: error("audioUrl Not Exist")
    }

    fun changeState(state: ProblemState) {
        _problemState.value = state
    }

    fun saveSolvedProblem() = CoroutineScope(Dispatchers.IO).launch {
        val tostToken = userRepository.getTostToken() ?: return@launch
        val currentProblemNumber = _problem.value?.questionNumber ?: error("problem is null")
        problemsRepository.saveSolvedProblem(tostToken, part, currentProblemNumber)
    }

    fun prepareRecorder(baseFilePath: String) {
        val currentSubProblemNumber = currentSubProblemNumber()
        prepareRecorder(baseFilePath, currentSubProblemNumber)
    }

    fun saveRecord(){
        val currentSubProblemNumber = currentSubProblemNumber()
        saveRecord(currentSubProblemNumber)
        _subProblemNumber.value = currentSubProblemNumber.next()
    }

    private fun currentSubProblemNumber(): Problem.SubNumber = _subProblemNumber.value
        ?: error("subProblemNumber cannot be null")
}
