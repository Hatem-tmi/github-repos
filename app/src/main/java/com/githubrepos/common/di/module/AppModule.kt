package com.githubrepos.common.di.module

import android.app.Application
import android.content.Context
import com.githubrepos.common.di.scope.PerApplication
import com.githubrepos.common.util.NetworkStateMonitor
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    @PerApplication
    fun provideContext(app: Application): Context = app

    @Provides
    @PerApplication
    fun provideNetworkStateMonitor(context: Context): NetworkStateMonitor =
        NetworkStateMonitor(context = context)
}