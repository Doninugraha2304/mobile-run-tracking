package com.runtracker.app.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    var userWeight: Double
        get() = prefs.getFloat(Constants.PREF_WEIGHT, Constants.USER_WEIGHT_KG.toFloat()).toDouble()
        set(value) = prefs.edit().putFloat(Constants.PREF_WEIGHT, value.toFloat()).apply()

    var isMetric: Boolean
        get() = prefs.getBoolean(Constants.PREF_UNIT, true)
        set(value) = prefs.edit().putBoolean(Constants.PREF_UNIT, value).apply()

    var targetDistance: Double
        get() = prefs.getFloat(Constants.PREF_TARGET_DISTANCE, 0f).toDouble()
        set(value) = prefs.edit().putFloat(Constants.PREF_TARGET_DISTANCE, value.toFloat()).apply()

    var targetCalories: Double
        get() = prefs.getFloat(Constants.PREF_TARGET_CALORIES, 0f).toDouble()
        set(value) = prefs.edit().putFloat(Constants.PREF_TARGET_CALORIES, value.toFloat()).apply()

    var voiceAnnouncement: Boolean
        get() = prefs.getBoolean(Constants.PREF_VOICE_ANNOUNCEMENT, true)
        set(value) = prefs.edit().putBoolean(Constants.PREF_VOICE_ANNOUNCEMENT, value).apply()

    var intervalRun: Int
        get() = prefs.getInt(Constants.PREF_INTERVAL_RUN, 5)
        set(value) = prefs.edit().putInt(Constants.PREF_INTERVAL_RUN, value).apply()

    var intervalWalk: Int
        get() = prefs.getInt(Constants.PREF_INTERVAL_WALK, 3)
        set(value) = prefs.edit().putInt(Constants.PREF_INTERVAL_WALK, value).apply()

    var intervalSets: Int
        get() = prefs.getInt(Constants.PREF_INTERVAL_SETS, 8)
        set(value) = prefs.edit().putInt(Constants.PREF_INTERVAL_SETS, value).apply()
}
