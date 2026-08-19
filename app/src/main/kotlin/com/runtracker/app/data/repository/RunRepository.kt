package com.runtracker.app.data.repository

import com.runtracker.app.data.db.RunDao
import com.runtracker.app.data.db.RunEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunRepository @Inject constructor(
    private val runDao: RunDao
) {
    fun getAllRuns(): Flow<List<RunEntity>> = runDao.getAllRuns()

    fun getRunsBetween(start: Long, end: Long): Flow<List<RunEntity>> =
        runDao.getRunsBetween(start, end)

    fun getTotalDistanceBetween(start: Long, end: Long): Flow<Double?> =
        runDao.getTotalDistanceBetween(start, end)

    fun getTotalCaloriesBetween(start: Long, end: Long): Flow<Double?> =
        runDao.getTotalCaloriesBetween(start, end)

    fun getRunCountBetween(start: Long, end: Long): Flow<Int> =
        runDao.getRunCountBetween(start, end)

    fun getTotalDurationBetween(start: Long, end: Long): Flow<Long?> =
        runDao.getTotalDurationBetween(start, end)

    fun getAvgSpeedBetween(start: Long, end: Long): Flow<Double?> =
        runDao.getAvgSpeedBetween(start, end)

    fun getBestDistance(): Flow<Double?> = runDao.getBestDistance()

    fun getBestPace(): Flow<Double?> = runDao.getBestPace()

    fun getBestSpeed(): Flow<Double?> = runDao.getBestSpeed()

    fun getLongestDuration(): Flow<Long?> = runDao.getLongestDuration()

    fun getTotalDistance(): Flow<Double?> = runDao.getTotalDistance()

    fun getTotalRuns(): Flow<Int> = runDao.getTotalRuns()

    fun getTotalCalories(): Flow<Double?> = runDao.getTotalCalories()

    fun getTotalDuration(): Flow<Long?> = runDao.getTotalDuration()

    suspend fun insertRun(run: RunEntity): Long = runDao.insertRun(run)

    suspend fun getRunById(id: Long): RunEntity? = runDao.getRunById(id)

    suspend fun deleteRunById(id: Long) = runDao.deleteRunById(id)
}
