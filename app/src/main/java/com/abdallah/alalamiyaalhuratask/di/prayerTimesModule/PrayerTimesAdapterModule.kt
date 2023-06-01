package com.abdallah.alalamiyaalhuratask.di.prayerTimesModule

import com.abdallah.alalamiyaalhuratask.ui.prayerTimes.PrayerTimesAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object PrayerTimesAdapterModule {

    @Provides
    @ActivityScoped
    fun providePrayerTimesAdapter(): PrayerTimesAdapter {
        return PrayerTimesAdapter()
    }
}