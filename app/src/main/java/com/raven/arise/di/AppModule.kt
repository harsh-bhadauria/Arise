package com.raven.arise.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.raven.arise.data.local.alarm.AlarmDao
import com.raven.arise.data.local.alarm.AlarmSchedulerImpl
import com.raven.arise.data.local.AriseDatabase
import com.raven.arise.data.repositories.AlarmRepositoryImpl
import com.raven.arise.domain.models.AlarmScheduler
import com.raven.arise.domain.repositories.AlarmRepository
import com.raven.arise.domain.usecases.AlarmUseCases
import com.raven.arise.domain.usecases.DeleteAlarmUseCase
import com.raven.arise.domain.usecases.GetAlarmsUseCase
import com.raven.arise.domain.usecases.SaveAlarmUseCase
import com.raven.arise.domain.usecases.ScheduleWakeUpCheckUseCase
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
    fun provideDatabase(app: Application): AriseDatabase {
        return Room.databaseBuilder(
            app,
            AriseDatabase::class.java,
            "arise_db"
        ).build()
    }

    @Provides
    fun provideAlarmDao(db: AriseDatabase): AlarmDao = db.alarmDao()

    @Provides
    @Singleton
    fun provideAlarmRepository(dao: AlarmDao): AlarmRepository =
        AlarmRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAlarmUseCases(
        repository: AlarmRepository,
        scheduler: AlarmScheduler
    ): AlarmUseCases {
        return AlarmUseCases(
            getAlarmsUseCase = GetAlarmsUseCase(repository),
            saveAlarmUseCase = SaveAlarmUseCase(repository, scheduler),
            deleteAlarmUseCase = DeleteAlarmUseCase(repository),
            scheduleWakeUpCheckUseCase = ScheduleWakeUpCheckUseCase(scheduler)
        )
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(
        @ApplicationContext context: Context
    ): AlarmScheduler = AlarmSchedulerImpl(context)
}