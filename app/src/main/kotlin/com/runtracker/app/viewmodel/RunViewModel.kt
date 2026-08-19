package com.runtracker.app.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.runtracker.app.data.db.RunEntity
import com.runtracker.app.data.repository.RunRepository
import com.runtracker.app.service.LocationService
import com.runtracker.app.util.Constants
import com.runtracker.app.util.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    application: Application,
    private val repository: RunRepository
) : AndroidViewModel(application) {

    private var locationService: LocationService? = null
    private var isBound = false

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()

    private val _totalDistance = MutableStateFlow(0.0)
    val totalDistance: StateFlow<Double> = _totalDistance.asStateFlow()

    private val _currentSpeed = MutableStateFlow(0.0)
    val currentSpeed: StateFlow<Double> = _currentSpeed.asStateFlow()

    private val _routePoints = MutableStateFlow<List<Pair<Double, Double>>>(emptyList())
    val routePoints: StateFlow<List<Pair<Double, Double>>> = _routePoints.asStateFlow()

    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation.asStateFlow()

    val allRuns = repository.getAllRuns()

    private val _weeklyDistance = MutableStateFlow(0.0)
    val weeklyDistance: StateFlow<Double> = _weeklyDistance.asStateFlow()

    private val _weeklyCalories = MutableStateFlow(0.0)
    val weeklyCalories: StateFlow<Double> = _weeklyCalories.asStateFlow()

    private val _weeklyCount = MutableStateFlow(0)
    val weeklyCount: StateFlow<Int> = _weeklyCount.asStateFlow()

    private val _weeklyDuration = MutableStateFlow(0L)
    val weeklyDuration: StateFlow<Long> = _weeklyDuration.asStateFlow()

    private val _monthlyDistance = MutableStateFlow(0.0)
    val monthlyDistance: StateFlow<Double> = _monthlyDistance.asStateFlow()

    private val _monthlyCalories = MutableStateFlow(0.0)
    val monthlyCalories: StateFlow<Double> = _monthlyCalories.asStateFlow()

    private val _monthlyCount = MutableStateFlow(0)
    val monthlyCount: StateFlow<Int> = _monthlyCount.asStateFlow()

    private val _monthlyDuration = MutableStateFlow(0L)
    val monthlyDuration: StateFlow<Long> = _monthlyDuration.asStateFlow()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val serviceBinder = binder as LocationService.LocationBinder
            locationService = serviceBinder.getService()
            isBound = true
            observeService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            locationService = null
            isBound = false
        }
    }

    init {
        bindLocationService()
        loadStats()
    }

    private fun bindLocationService() {
        val intent = Intent(getApplication(), LocationService::class.java)
        getApplication<Application>().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun observeService() {
        viewModelScope.launch {
            locationService?.isTracking?.collect { tracking ->
                _isTracking.value = tracking
            }
        }
        viewModelScope.launch {
            locationService?.elapsedTime?.collect { time ->
                _elapsedTime.value = time
            }
        }
        viewModelScope.launch {
            locationService?.totalDistance?.collect { distance ->
                _totalDistance.value = distance
            }
        }
        viewModelScope.launch {
            locationService?.currentSpeed?.collect { speed ->
                _currentSpeed.value = speed
            }
        }
        viewModelScope.launch {
            locationService?.routePoints?.collect { points ->
                _routePoints.value = points
            }
        }
        viewModelScope.launch {
            locationService?.currentLocation?.collect { loc ->
                _currentLocation.value = loc
            }
        }
    }

    fun startTracking() {
        val intent = Intent(getApplication(), LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        getApplication<Application>().startForegroundService(intent)
        if (!isBound) bindLocationService()
    }

    fun stopTracking() {
        val stats = locationService?.getStats()
        stats?.let {
            if (it.distance > 10) {
                saveRun(it)
            }
        }
        val intent = Intent(getApplication(), LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
        }
        getApplication<Application>().startService(intent)
        loadStats()
    }

    private fun saveRun(stats: LocationService.RunStats) {
        viewModelScope.launch {
            val routeJson = stats.routePoints.joinToString(";") { "${it.first},${it.second}" }
            val run = RunEntity(
                startTime = stats.duration,
                endTime = System.currentTimeMillis(),
                duration = stats.duration,
                distance = stats.distance,
                avgSpeed = stats.avgSpeed,
                maxSpeed = stats.maxSpeed,
                calories = stats.calories,
                routePoints = routeJson,
                avgPace = stats.avgPace
            )
            repository.insertRun(run)
        }
    }

    private fun loadStats() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val weekStart = cal.timeInMillis

        cal.add(Calendar.WEEK_OF_YEAR, 1)
        val weekEnd = cal.timeInMillis

        val monthCal = Calendar.getInstance()
        monthCal.set(Calendar.DAY_OF_MONTH, 1)
        monthCal.set(Calendar.HOUR_OF_DAY, 0)
        monthCal.set(Calendar.MINUTE, 0)
        monthCal.set(Calendar.SECOND, 0)
        monthCal.set(Calendar.MILLISECOND, 0)
        val monthStart = monthCal.timeInMillis

        monthCal.add(Calendar.MONTH, 1)
        val monthEnd = monthCal.timeInMillis

        viewModelScope.launch {
            repository.getTotalDistanceBetween(weekStart, weekEnd).collect { _weeklyDistance.value = it ?: 0.0 }
        }
        viewModelScope.launch {
            repository.getTotalCaloriesBetween(weekStart, weekEnd).collect { _weeklyCalories.value = it ?: 0.0 }
        }
        viewModelScope.launch {
            repository.getRunCountBetween(weekStart, weekEnd).collect { _weeklyCount.value = it }
        }
        viewModelScope.launch {
            repository.getTotalDurationBetween(weekStart, weekEnd).collect { _weeklyDuration.value = it ?: 0L }
        }
        viewModelScope.launch {
            repository.getTotalDistanceBetween(monthStart, monthEnd).collect { _monthlyDistance.value = it ?: 0.0 }
        }
        viewModelScope.launch {
            repository.getTotalCaloriesBetween(monthStart, monthEnd).collect { _monthlyCalories.value = it ?: 0.0 }
        }
        viewModelScope.launch {
            repository.getRunCountBetween(monthStart, monthEnd).collect { _monthlyCount.value = it }
        }
        viewModelScope.launch {
            repository.getTotalDurationBetween(monthStart, monthEnd).collect { _monthlyDuration.value = it ?: 0L }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (isBound) {
            getApplication<Application>().unbindService(serviceConnection)
            isBound = false
        }
    }
}
