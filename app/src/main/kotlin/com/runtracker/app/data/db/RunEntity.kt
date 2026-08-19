package com.runtracker.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class RunEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val duration: Long,
    val distance: Double,
    val avgSpeed: Double,
    val maxSpeed: Double,
    val calories: Double,
    val routePoints: String,
    val avgPace: Double
)
