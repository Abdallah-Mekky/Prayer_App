package com.abdallah.alalamiyaalhuratask.di.prayerTimesModule

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object FusedLocationClientModule {

    @Provides
    @ActivityScoped
    fun provideFusedLocationClient(@ActivityContext context:Context): FusedLocationProviderClient{

        return LocationServices.getFusedLocationProviderClient(context)
    }
}