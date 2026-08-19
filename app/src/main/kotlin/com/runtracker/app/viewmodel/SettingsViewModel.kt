package com.runtracker.app.viewmodel

import androidx.lifecycle.ViewModel
import com.runtracker.app.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferencesManager
) : ViewModel() {

    private val _userWeight = MutableStateFlow(prefs.userWeight)
    val userWeight: StateFlow<Double> = _userWeight.asStateFlow()

    private val _isMetric = MutableStateFlow(prefs.isMetric)
    val isMetric: StateFlow<Boolean> = _isMetric.asStateFlow()

    private val _targetDistance = MutableStateFlow(prefs.targetDistance)
    val targetDistance: StateFlow<Double> = _targetDistance.asStateFlow()

    private val _targetCalories = MutableStateFlow(prefs.targetCalories)
    val targetCalories: StateFlow<Double> = _targetCalories.asStateFlow()

    private val _voiceAnnouncement = MutableStateFlow(prefs.voiceAnnouncement)
    val voiceAnnouncement: StateFlow<Boolean> = _voiceAnnouncement.asStateFlow()

    fun setMetric(value: Boolean) {
        prefs.isMetric = value
        _isMetric.value = value
    }

    fun setVoiceAnnouncement(value: Boolean) {
        prefs.voiceAnnouncement = value
        _voiceAnnouncement.value = value
    }

    fun saveSettings(weight: Double, targetDistance: Double, targetCalories: Double) {
        prefs.userWeight = weight
        prefs.targetDistance = targetDistance
        prefs.targetCalories = targetCalories
        _userWeight.value = weight
        _targetDistance.value = targetDistance
        _targetCalories.value = targetCalories
    }
}
