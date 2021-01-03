package com.tost.presentation.problem

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tost.data.entity.MyNote
import com.tost.data.entity.Part
import com.tost.data.repository.ProblemsRepository
import com.tost.data.repository.UserRepository
import com.tost.presentation.utils.BaseViewModel
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 12월 27, 2020
 */

class ProblemEntryViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val problemsRepository: ProblemsRepository,
) : BaseViewModel() {

    private val _myNote = MutableLiveData<MyNote>()
    val myNote: LiveData<MyNote> = _myNote

    fun loadMyNote(part: Part) = viewModelScope.launch {
        val tostToken = userRepository.getTostToken() ?: return@launch
        val myNote = problemsRepository.getMyNote(tostToken, part)
        _myNote.value = myNote
    }

    fun getNextProblemNumber(): Int {
        return _myNote.value?.nextProblemIndex ?: 1
    }

    fun isMyNoteNotNull(): Boolean = _myNote.value != null
}
