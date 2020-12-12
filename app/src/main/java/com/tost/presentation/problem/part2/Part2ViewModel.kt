package com.tost.presentation.problem.part2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tost.data.repository.RecordsRepository
import com.tost.presentation.problem.ProblemState
import com.tost.presentation.problem.base.AudioViewModel

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

class Part2ViewModel(
    private val recordsRepository: RecordsRepository,
) : AudioViewModel(recordsRepository) {

    override val part: String = "part2"

    private val _problemState = MutableLiveData(ProblemState.PREPARE)
    val problemState: LiveData<ProblemState>
        get() = _problemState

    private val _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    fun changeState(state: ProblemState) {
        _problemState.value = state
    }
}
