package com.runtracker.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.runtracker.app.MainActivity
import com.runtracker.app.R
import com.runtracker.app.util.Constants
import com.runtracker.app.util.LocationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation

    private val _routePoints = MutableStateFlow<List<Pair<Double, Double>>>(emptyList())
    val routePoints: StateFlow<List<Pair<Double, Double>>> = _routePoints

    private val _totalDistance = MutableStateFlow(0.0)
    val totalDistance: StateFlow<Double> = _totalDistance

    private val _currentSpeed = MutableStateFlow(0.0)
    val currentSpeed: StateFlow<Double> = _currentSpeed

    private val _startTime = MutableStateFlow(0L)
    val startTime: StateFlow<Long> = _startTime

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private var lastLocation: Location? = null
    private var timerThread: Thread? = null

    private val binder = LocationBinder()

    inner class LocationBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationCallback()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTracking()
            ACTION_STOP -> stopTracking()
        }
        return START_STICKY
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    onNewLocation(location)
                }
            }
        }
    }

    private var filteredLocation: Location? = null

    private fun onNewLocation(location: Location) {
        if (location.accuracy > 30f) return

        val newPoint = Pair(location.latitude, location.longitude)

        if (filteredLocation == null) {
            filteredLocation = location
            _currentLocation.value = newPoint
            _routePoints.value = listOf(newPoint)
            _currentSpeed.value = location.speed * 3.6
            return
        }

        val distanceToLast = LocationUtils.calculateDistance(
            filteredLocation!!.latitude, filteredLocation!!.longitude,
            location.latitude, location.longitude
        )

        val speedKmh = location.speed * 3.6
        _currentSpeed.value = speedKmh

        if (distanceToLast > 5f && speedKmh > 1.5f) {
            _totalDistance.value += distanceToLast
            _routePoints.value = _routePoints.value + newPoint
            filteredLocation = location
            _currentLocation.value = newPoint
        }
    }

    fun startTracking() {
        _isTracking.value = true
        _startTime.value = System.currentTimeMillis()
        _totalDistance.value = 0.0
        _routePoints.value = emptyList()
        _currentLocation.value = null
        lastLocation = null
        filteredLocation = null

        startForeground(Constants.LOCATION_SERVICE_NOTIFICATION_ID, buildNotification("Menyiapkan GPS..."))
        startLocationUpdates()
        startTimer()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            Constants.LOCATION_UPDATE_INTERVAL
        ).apply {
            setMinUpdateIntervalMillis(Constants.LOCATION_FASTEST_INTERVAL)
            setWaitForAccurateLocation(true)
        }.build()

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun startTimer() {
        timerThread = Thread {
            while (_isTracking.value) {
                _elapsedTime.value = System.currentTimeMillis() - _startTime.value
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    break
                }
            }
        }.also { it.start() }
    }

    fun stopTracking() {
        _isTracking.value = false
        fusedLocationClient.removeLocationUpdates(locationCallback)
        timerThread?.interrupt()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun getStats(): RunStats {
        val distance = _totalDistance.value
        val duration = _elapsedTime.value
        return RunStats(
            distance = distance,
            duration = duration,
            avgSpeed = LocationUtils.calculateAvgSpeed(distance, duration),
            maxSpeed = _currentSpeed.value,
            calories = LocationUtils.calculateCalories(distance / 1000.0),
            avgPace = LocationUtils.calculatePace(distance, duration),
            routePoints = _routePoints.value
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.LOCATION_SERVICE_CHANNEL_ID,
                "Pelacakan Lari",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Menampilkan status pelacakan lari"
            }
            val splitChannel = NotificationChannel(
                Constants.SPLIT_NOTIFICATION_CHANNEL_ID,
                "Notifikasi Split",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi setiap kilometer"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
            manager.createNotificationChannel(splitChannel)
        }
    }

    private fun buildNotification(text: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = PendingIntent.getService(
            this, 1,
            Intent(this, LocationService::class.java).apply { action = ACTION_STOP },
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, Constants.LOCATION_SERVICE_CHANNEL_ID)
            .setContentTitle("RunTracker")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Berhenti", stopIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTracking()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    data class RunStats(
        val distance: Double,
        val duration: Long,
        val avgSpeed: Double,
        val maxSpeed: Double,
        val calories: Double,
        val avgPace: Double,
        val routePoints: List<Pair<Double, Double>>
    )
}
