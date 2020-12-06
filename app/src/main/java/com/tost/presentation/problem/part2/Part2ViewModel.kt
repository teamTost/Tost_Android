package com.tost.presentation.problem.part2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tost.data.repository.RecordsRepository
import com.tost.presentation.problem.ProblemState

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

class Part2ViewModel(
    private val recordsRepository: RecordsRepository,
) : ViewModel() {
    private val _problemState = MutableLiveData(ProblemState.PREPARE)
    val problemState: LiveData<ProblemState>
        get() = _problemState

    fun changeState(state: ProblemState) {
        _problemState.value = state
    }

    companion object {
        private const val PART = "part2"
    }
}