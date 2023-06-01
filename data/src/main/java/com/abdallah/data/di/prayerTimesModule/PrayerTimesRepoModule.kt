package com.abdallah.data.di.prayerTimesModule

import com.abdallah.data.api.WepServices
import com.abdallah.data.database.MyDataBase
import com.abdallah.data.reposImpl.prayerTimesRepository.PrayerTimesOfflineDataSourceImpl
import com.abdallah.data.reposImpl.prayerTimesRepository.PrayerTimesOnlineDataSourceImpl
import com.abdallah.data.reposImpl.prayerTimesRepository.PrayerTimesRepoImpl
import com.abdallah.domain.repos.prayerTimesRepository.PrayerTimesOfflineDataSource
import com.abdallah.domain.repos.prayerTimesRepository.PrayerTimesOnlineDataSource
import com.abdallah.domain.repos.prayerTimesRepository.PrayerTimesRepo
import com.abdallah.domain.utils.networkHandler.NetworkHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PrayerTimesRepoModule {

    @Provides
    @Singleton
    fun providePrayerTimesOnlineDataSource(
        webServices: WepServices
    ): PrayerTimesOnlineDataSource {

        return PrayerTimesOnlineDataSourceImpl(webServices)
    }

    @Provides
    @Singleton
    fun providePrayerTimesOfflineDataSource(
        myDataBase: MyDataBase
    ): PrayerTimesOfflineDataSource {

        return PrayerTimesOfflineDataSourceImpl(myDataBase)
    }

    @Provides
    @Singleton
    fun providePrayerTimesRepo(
        prayerTimesOnlineDataSource: PrayerTimesOnlineDataSource,
        prayerTimesOfflineDataSource: PrayerTimesOfflineDataSource,
        networkHandler: NetworkHandler
    ): PrayerTimesRepo {

        return PrayerTimesRepoImpl(
            prayerTimesOnlineDataSource,
            prayerTimesOfflineDataSource,
            networkHandler
        )
    }
}