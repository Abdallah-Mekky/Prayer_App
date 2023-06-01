package com.abdallah.alalamiyaalhuratask.di.countDownController

import com.abdallah.alalamiyaalhuratask.ui.utils.countDownControler.CountDownController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object CountDownControllerModule {

    @Provides
    @ActivityScoped
    fun provideCountDownController():CountDownController{
        return CountDownController()
    }
}