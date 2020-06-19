package com.githubrepos.common.di.module

import com.githubrepos.common.di.qualifier.UI
import com.githubrepos.common.di.qualifier.Worker
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class SchedulersModule {

    @Provides
    @Worker
    fun provideIOScheduler(): Scheduler = Schedulers.io()

    @Provides
    @UI
    fun provideUIScheduler(): Scheduler = AndroidSchedulers.mainThread()
}