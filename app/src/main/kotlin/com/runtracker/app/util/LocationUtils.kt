package com.runtracker.app.util

import android.location.Location
import kotlin.math.abs

object LocationUtils {

    fun calculateDistance(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLat, startLng, endLat, endLng, results)
        return results[0]
    }

    fun calculateTotalDistance(points: List<Pair<Double, Double>>): Double {
        if (points.size < 2) return 0.0
        var totalDistance = 0.0
        for (i in 1 until points.size) {
            val distance = calculateDistance(
                points[i - 1].first, points[i - 1].second,
                points[i].first, points[i].second
            )
            totalDistance += distance
        }
        return totalDistance
    }

    fun calculateAvgSpeed(distanceMeters: Double, durationMillis: Long): Double {
        if (durationMillis <= 0) return 0.0
        val hours = durationMillis / 3600000.0
        val km = distanceMeters / 1000.0
        return km / hours
    }

    fun calculateCalories(distanceKm: Double, weightKg: Double = 70.0): Double {
        return distanceKm * weightKg * 1.036
    }

    fun calculatePace(distanceMeters: Double, durationMillis: Long): Double {
        if (distanceMeters <= 0) return 0.0
        val minutes = durationMillis / 60000.0
        val km = distanceMeters / 1000.0
        return minutes / km
    }

    fun formatDuration(durationMillis: Long): String {
        val seconds = (durationMillis / 1000) % 60
        val minutes = (durationMillis / 60000) % 60
        val hours = (durationMillis / 3600000)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatDistance(meters: Double): String {
        return if (meters < 1000) {
            String.format("%.0f m", meters)
        } else {
            String.format("%.2f km", meters / 1000)
        }
    }

    fun formatSpeed(kmh: Double): String {
        return String.format("%.1f km/j", kmh)
    }

    fun formatPace(minutesPerKm: Double): String {
        if (minutesPerKm <= 0 || minutesPerKm > 60) return "--:--"
        val minutes = minutesPerKm.toInt()
        val seconds = ((minutesPerKm - minutes) * 60).toInt()
        return String.format("%d:%02d /km", minutes, seconds)
    }
}
