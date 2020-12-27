package com.tost.presentation.problem.part1

import androidx.hilt.lifecycle.ViewModelInject
import com.tost.data.repository.RecordsRepository
import com.tost.presentation.problem.base.AudioViewModel

/**
 * Created By Malibin
 * on 12월 27, 2020
 */

class Part1ViewModel @ViewModelInject constructor(
    recordsRepository: RecordsRepository
) : AudioViewModel(recordsRepository) {

    override val part: String = "Part1"

}