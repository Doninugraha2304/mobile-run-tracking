package com.runtracker.app.util

object Constants {
    const val LOCATION_SERVICE_CHANNEL_ID = "location_service_channel"
    const val SPLIT_NOTIFICATION_CHANNEL_ID = "split_notification_channel"
    const val LOCATION_SERVICE_NOTIFICATION_ID = 1001
    const val SPLIT_NOTIFICATION_ID = 1002
    const val LOCATION_UPDATE_INTERVAL = 3000L
    const val LOCATION_FASTEST_INTERVAL = 1500L
    const val GPS_ACCURACY_THRESHOLD = 30f
    const val MIN_DISTANCE_THRESHOLD = 5f
    const val MIN_SPEED_THRESHOLD = 1.5f
    const val USER_WEIGHT_KG = 70.0
    const val PREF_NAME = "run_tracker_prefs"
    const val PREF_WEIGHT = "user_weight"
    const val PREF_UNIT = "unit_metric"
    const val PREF_TARGET_DISTANCE = "target_distance"
    const val PREF_TARGET_CALORIES = "target_calories"
    const val PREF_VOICE_ANNOUNCEMENT = "voice_announcement"
    const val PREF_INTERVAL_RUN = "interval_run"
    const val PREF_INTERVAL_WALK = "interval_walk"
    const val PREF_INTERVAL_SETS = "interval_sets"
    const val CALORIE_FACTOR = 1.036
    const val KM_TO_MILES = 0.621371
    const val MILES_TO_KM = 1.60934
    const val VOICE_ANNOUNCEMENT_INTERVAL_KM = 1.0
}
