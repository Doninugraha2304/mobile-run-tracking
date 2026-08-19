package com.runtracker.app.util

import android.location.Location

class GpsKalmanFilter {
    private var isInitialized = false
    private var kalmanLat = 0.0
    private var kalmanLon = 0.0
    private var pLat = 0.0
    private var pLon = 0.0

    private var stationaryCount = 0
    private var isStationary = false
    private var stationaryLat = 0.0
    private var stationaryLon = 0.0

    private var lastFilteredLat = 0.0
    private var lastFilteredLon = 0.0
    private var lastSpeed = 0.0f

    data class FilterResult(
        val latitude: Double,
        val longitude: Double,
        val isStationary: Boolean
    )

    fun reset() {
        isInitialized = false
        stationaryCount = 0
        isStationary = false
    }

    fun process(location: Location): FilterResult {
        val lat = location.latitude
        val lon = location.longitude
        val accuracy = location.accuracy.toDouble().coerceAtLeast(1.0)
        val speed = location.speed

        if (!isInitialized) {
            kalmanLat = lat
            kalmanLon = lon
            pLat = accuracy * accuracy
            pLon = accuracy * accuracy
            lastFilteredLat = lat
            lastFilteredLon = lon
            stationaryLat = lat
            stationaryLon = lon
            lastSpeed = speed
            isInitialized = true
            return FilterResult(lat, lon, false)
        }

        val r = accuracy * accuracy

        pLat += PROCESS_NOISE
        pLon += PROCESS_NOISE

        val kLat = pLat / (pLat + r)
        val kLon = pLon / (pLon + r)

        kalmanLat += kLat * (lat - kalmanLat)
        kalmanLon += kLon * (lon - kalmanLon)

        pLat = (1.0 - kLat) * pLat
        pLon = (1.0 - kLon) * pLon

        val speedKmh = speed * 3.6
        val distFromStationary = calcDistance(stationaryLat, stationaryLon, kalmanLat, kalmanLon)

        val isMoving = speedKmh > STATIONARY_SPEED_KMH || distFromStationary > STATIONARY_DISTANCE_M

        if (isMoving) {
            stationaryCount = 0
            isStationary = false
            stationaryLat = kalmanLat
            stationaryLon = kalmanLon
        } else {
            stationaryCount++
            if (stationaryCount >= STATIONARY_COUNT) {
                isStationary = true
            }
        }

        val finalLat: Double
        val finalLon: Double

        if (isStationary) {
            finalLat = stationaryLat
            finalLon = stationaryLon
        } else {
            finalLat = kalmanLat
            finalLon = kalmanLon
        }

        lastFilteredLat = finalLat
        lastFilteredLon = finalLon
        lastSpeed = speed

        return FilterResult(finalLat, finalLon, isStationary)
    }

    private fun calcDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0].toDouble()
    }

    companion object {
        private const val PROCESS_NOISE = 0.5
        private const val STATIONARY_SPEED_KMH = 1.0
        private const val STATIONARY_DISTANCE_M = 8.0
        private const val STATIONARY_COUNT = 5
    }
}
