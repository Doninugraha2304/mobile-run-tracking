package com.runtracker.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Insert
    suspend fun insertRun(run: RunEntity): Long

    @Query("SELECT * FROM runs ORDER BY startTime DESC")
    fun getAllRuns(): Flow<List<RunEntity>>

    @Query("SELECT * FROM runs WHERE startTime BETWEEN :start AND :end ORDER BY startTime DESC")
    fun getRunsBetween(start: Long, end: Long): Flow<List<RunEntity>>

    @Query("SELECT SUM(distance) FROM runs WHERE startTime BETWEEN :start AND :end")
    fun getTotalDistanceBetween(start: Long, end: Long): Flow<Double?>

    @Query("SELECT SUM(calories) FROM runs WHERE startTime BETWEEN :start AND :end")
    fun getTotalCaloriesBetween(start: Long, end: Long): Flow<Double?>

    @Query("SELECT COUNT(*) FROM runs WHERE startTime BETWEEN :start AND :end")
    fun getRunCountBetween(start: Long, end: Long): Flow<Int>

    @Query("SELECT SUM(duration) FROM runs WHERE startTime BETWEEN :start AND :end")
    fun getTotalDurationBetween(start: Long, end: Long): Flow<Long?>

    @Query("SELECT AVG(avgSpeed) FROM runs WHERE startTime BETWEEN :start AND :end")
    fun getAvgSpeedBetween(start: Long, end: Long): Flow<Double?>

    @Query("SELECT MAX(distance) FROM runs")
    fun getBestDistance(): Flow<Double?>

    @Query("SELECT MIN(avgPace) FROM runs WHERE avgPace > 0")
    fun getBestPace(): Flow<Double?>

    @Query("SELECT MAX(avgSpeed) FROM runs")
    fun getBestSpeed(): Flow<Double?>

    @Query("SELECT MAX(duration) FROM runs")
    fun getLongestDuration(): Flow<Long?>

    @Query("SELECT SUM(distance) FROM runs")
    fun getTotalDistance(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM runs")
    fun getTotalRuns(): Flow<Int>

    @Query("SELECT SUM(calories) FROM runs")
    fun getTotalCalories(): Flow<Double?>

    @Query("SELECT SUM(duration) FROM runs")
    fun getTotalDuration(): Flow<Long?>

    @Query("SELECT * FROM runs WHERE id = :id")
    suspend fun getRunById(id: Long): RunEntity?

    @Query("DELETE FROM runs WHERE id = :id")
    suspend fun deleteRunById(id: Long)

    @Query("SELECT * FROM runs WHERE startTime BETWEEN :start AND :end ORDER BY startTime ASC")
    fun getRunsBetweenAsc(start: Long, end: Long): Flow<List<RunEntity>>
}
