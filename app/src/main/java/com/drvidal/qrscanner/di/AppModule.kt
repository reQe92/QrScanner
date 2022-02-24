package com.drvidal.qrscanner.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.drvidal.qrscanner.data.code.CodeDatabase
import com.drvidal.qrscanner.domain.AnalyticsRepository
import com.drvidal.qrscanner.domain.CodeRepository
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
    fun provideCodeDatabase(app: Application): CodeDatabase {
        return Room.databaseBuilder(
            app,
            CodeDatabase::class.java,
            CodeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCodeRepository(
        db: CodeDatabase,
        @ApplicationContext context: Context,
        preferences: SharedPreferences,
        analyticsRepository: AnalyticsRepository
    ): CodeRepository {
        return CodeRepository(db.codeDao, context, preferences, analyticsRepository)
    }

    @Provides
    @Singleton
    fun providePreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    @Singleton
    @Provides
    fun provideAnalyticsRepository() : AnalyticsRepository {
        return AnalyticsRepository()
    }
}