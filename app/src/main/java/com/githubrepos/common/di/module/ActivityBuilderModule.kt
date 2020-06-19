package com.githubrepos.common.di.module

import com.githubrepos.common.di.scope.PerActivity
import com.githubrepos.presentation.home.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [])
    abstract fun bindMainActivity(): MainActivity
}