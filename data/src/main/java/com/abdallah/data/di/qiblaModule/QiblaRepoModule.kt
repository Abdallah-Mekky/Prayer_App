package com.abdallah.data.di.qiblaModule

import com.abdallah.data.api.WepServices
import com.abdallah.data.database.MyDataBase
import com.abdallah.data.reposImpl.qiblaRepository.QiblaOfflineDataSourceImpl
import com.abdallah.data.reposImpl.qiblaRepository.QiblaOnlineDataSourceImpl
import com.abdallah.data.reposImpl.qiblaRepository.QiblaRepoImpl
import com.abdallah.domain.repos.qiblaRepository.QiblaOfflineDataSource
import com.abdallah.domain.repos.qiblaRepository.QiblaOnlineDataSource
import com.abdallah.domain.repos.qiblaRepository.QiblaRepo
import com.abdallah.domain.utils.networkHandler.NetworkHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QiblaRepoModule {

    @Provides
    @Singleton
    fun provideQiblaOnlineDataSource(webServices: WepServices): QiblaOnlineDataSource {

        return QiblaOnlineDataSourceImpl(webServices)

    }

    @Provides
    @Singleton
    fun provideQiblaOfflineDataSource(myDataBase: MyDataBase): QiblaOfflineDataSource {

        return QiblaOfflineDataSourceImpl(myDataBase)

    }

    @Provides
    @Singleton
    fun provideQiblaRepo(
        qiblaOnlineDataSource: QiblaOnlineDataSource,
        qiblaOfflineDataSource: QiblaOfflineDataSource,
        networkHandler: NetworkHandler
    ): QiblaRepo {

        return QiblaRepoImpl(qiblaOnlineDataSource, qiblaOfflineDataSource, networkHandler)
    }
}