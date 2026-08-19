package com.runtracker.app.viewmodel

import androidx.lifecycle.ViewModel
import com.runtracker.app.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class IntervalViewModel @Inject constructor(
    private val prefs: PreferencesManager
) : ViewModel() {

    private val _runTime = MutableStateFlow(prefs.intervalRun)
    val runTime: StateFlow<Int> = _runTime.asStateFlow()

    private val _walkTime = MutableStateFlow(prefs.intervalWalk)
    val walkTime: StateFlow<Int> = _walkTime.asStateFlow()

    private val _sets = MutableStateFlow(prefs.intervalSets)
    val sets: StateFlow<Int> = _sets.asStateFlow()

    fun save(run: Int, walk: Int, sets: Int) {
        prefs.intervalRun = run
        prefs.intervalWalk = walk
        prefs.intervalSets = sets
        _runTime.value = run
        _walkTime.value = walk
        _sets.value = sets
    }
}
