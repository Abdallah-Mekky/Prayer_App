package com.abdallah.alalamiyaalhuratask.di.prayerTimesModule

import android.content.Context
import com.abdallah.alalamiyaalhuratask.model.CalendarCardViewDetails
import com.abdallah.alalamiyaalhuratask.ui.utils.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object LocationServicesModule {

    @Provides
    @ActivityScoped
    fun providesLocationServices(@ActivityContext context: Context):LocationService{

        return LocationService(context)
    }
}