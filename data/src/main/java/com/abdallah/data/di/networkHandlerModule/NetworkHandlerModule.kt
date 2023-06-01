package com.abdallah.data.di.networkHandlerModule

import android.content.Context
import com.abdallah.data.utils.networkHandler.NetworkHandlerImpl
import com.abdallah.domain.utils.networkHandler.NetworkHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkHandlerModule {

    @Provides
    @Singleton
    fun provideNetworkHandler(@ApplicationContext context: Context): NetworkHandler {

        return NetworkHandlerImpl(context)
    }
}