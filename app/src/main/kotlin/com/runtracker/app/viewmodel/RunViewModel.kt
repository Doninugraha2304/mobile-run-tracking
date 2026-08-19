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
import com.runtracker.app.util.PreferencesManager
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
    private val repository: RunRepository,
    val prefs: PreferencesManager
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

    private val _bestDistance = MutableStateFlow(0.0)
    val bestDistance: StateFlow<Double> = _bestDistance.asStateFlow()

    private val _bestPace = MutableStateFlow(0.0)
    val bestPace: StateFlow<Double> = _bestPace.asStateFlow()

    private val _bestSpeed = MutableStateFlow(0.0)
    val bestSpeed: StateFlow<Double> = _bestSpeed.asStateFlow()

    private val _totalRuns = MutableStateFlow(0)
    val totalRuns: StateFlow<Int> = _totalRuns.asStateFlow()

    private val _totalAllDistance = MutableStateFlow(0.0)
    val totalAllDistance: StateFlow<Double> = _totalAllDistance.asStateFlow()

    private val _totalAllCalories = MutableStateFlow(0.0)
    val totalAllCalories: StateFlow<Double> = _totalAllCalories.asStateFlow()

    private val _intervalMode = MutableStateFlow(false)
    val intervalMode: StateFlow<Boolean> = _intervalMode.asStateFlow()

    private val _intervalPhase = MutableStateFlow("")
    val intervalPhase: StateFlow<String> = _intervalPhase.asStateFlow()

    private val _intervalSetCurrent = MutableStateFlow(0)
    val intervalSetCurrent: StateFlow<Int> = _intervalSetCurrent.asStateFlow()

    private val _intervalSetTotal = MutableStateFlow(0)
    val intervalSetTotal: StateFlow<Int> = _intervalSetTotal.asStateFlow()

    private var intervalTimer: Thread? = null

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
            locationService?.isTracking?.collect { _isTracking.value = it }
        }
        viewModelScope.launch {
            locationService?.elapsedTime?.collect { _elapsedTime.value = it }
        }
        viewModelScope.launch {
            locationService?.totalDistance?.collect { _totalDistance.value = it }
        }
        viewModelScope.launch {
            locationService?.currentSpeed?.collect { _currentSpeed.value = it }
        }
        viewModelScope.launch {
            locationService?.routePoints?.collect { _routePoints.value = it }
        }
        viewModelScope.launch {
            locationService?.currentLocation?.collect { _currentLocation.value = it }
        }
    }

    fun startTracking() {
        val intent = Intent(getApplication(), LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        getApplication<Application>().startForegroundService(intent)
        if (!isBound) bindLocationService()
    }

    fun startIntervalTraining(runSec: Int, walkSec: Int, totalSets: Int) {
        _intervalMode.value = true
        _intervalSetTotal.value = totalSets
        startTracking()
        runIntervalCycle(runSec, walkSec, totalSets)
    }

    private fun runIntervalCycle(runSec: Int, walkSec: Int, totalSets: Int) {
        intervalTimer = Thread {
            var currentSet = 1
            while (currentSet <= totalSets && _isTracking.value) {
                _intervalPhase.value = "LARI"
                _intervalSetCurrent.value = currentSet
                var countdown = runSec
                while (countdown > 0 && _isTracking.value) {
                    _elapsedTime.value = _elapsedTime.value
                    Thread.sleep(1000)
                    countdown--
                }
                if (currentSet < totalSets && _isTracking.value) {
                    _intervalPhase.value = "JALAN"
                    countdown = walkSec
                    while (countdown > 0 && _isTracking.value) {
                        Thread.sleep(1000)
                        countdown--
                    }
                }
                currentSet++
            }
            _intervalPhase.value = ""
            _intervalMode.value = false
        }.also { it.start() }
    }

    fun stopTracking() {
        _intervalMode.value = false
        _intervalPhase.value = ""
        intervalTimer?.interrupt()
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
                startTime = _startTime(stats.duration),
                endTime = System.currentTimeMillis(),
                duration = stats.duration,
                distance = stats.distance,
                avgSpeed = stats.avgSpeed,
                maxSpeed = stats.maxSpeed,
                calories = stats.calories,
                routePoints = routeJson,
                avgPace = stats.avgPace,
                isInterval = _intervalMode.value
            )
            repository.insertRun(run)
        }
    }

    private fun _startTime(duration: Long): Long = System.currentTimeMillis() - duration

    private fun loadStats() {
        val weekStart = getWeekStart()
        val weekEnd = weekStart + 7 * 24 * 60 * 60 * 1000L
        val monthStart = getMonthStart()
        val monthEnd = monthStart + 30L * 24 * 60 * 60 * 1000

        viewModelScope.launch { repository.getTotalDistanceBetween(weekStart, weekEnd).collect { _weeklyDistance.value = it ?: 0.0 } }
        viewModelScope.launch { repository.getTotalCaloriesBetween(weekStart, weekEnd).collect { _weeklyCalories.value = it ?: 0.0 } }
        viewModelScope.launch { repository.getRunCountBetween(weekStart, weekEnd).collect { _weeklyCount.value = it } }
        viewModelScope.launch { repository.getTotalDurationBetween(weekStart, weekEnd).collect { _weeklyDuration.value = it ?: 0L } }
        viewModelScope.launch { repository.getTotalDistanceBetween(monthStart, monthEnd).collect { _monthlyDistance.value = it ?: 0.0 } }
        viewModelScope.launch { repository.getTotalCaloriesBetween(monthStart, monthEnd).collect { _monthlyCalories.value = it ?: 0.0 } }
        viewModelScope.launch { repository.getRunCountBetween(monthStart, monthEnd).collect { _monthlyCount.value = it } }
        viewModelScope.launch { repository.getTotalDurationBetween(monthStart, monthEnd).collect { _monthlyDuration.value = it ?: 0L } }
        viewModelScope.launch { repository.getBestDistance().collect { _bestDistance.value = it ?: 0.0 } }
        viewModelScope.launch { repository.getBestPace().collect { _bestPace.value = it ?: 0.0 } }
        viewModelScope.launch { repository.getBestSpeed().collect { _bestSpeed.value = it ?: 0.0 } }
        viewModelScope.launch { repository.getTotalRuns().collect { _totalRuns.value = it } }
        viewModelScope.launch { repository.getTotalDistance().collect { _totalAllDistance.value = it ?: 0.0 } }
        viewModelScope.launch { repository.getTotalCalories().collect { _totalAllCalories.value = it ?: 0.0 } }
    }

    private fun getWeekStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getMonthStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    override fun onCleared() {
        super.onCleared()
        if (isBound) {
            getApplication<Application>().unbindService(serviceConnection)
            isBound = false
        }
    }
}
