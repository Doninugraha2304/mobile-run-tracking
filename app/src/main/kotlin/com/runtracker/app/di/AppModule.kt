package com.runtracker.app.di

import android.content.Context
import com.runtracker.app.data.db.RunDao
import com.runtracker.app.data.db.RunDatabase
import com.runtracker.app.data.repository.RunRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RunDatabase {
        return RunDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideRunDao(database: RunDatabase): RunDao {
        return database.runDao()
    }

    @Provides
    @Singleton
    fun provideRunRepository(runDao: RunDao): RunRepository {
        return RunRepository(runDao)
    }
}
