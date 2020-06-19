package com.githubrepos.common.di.module

import android.content.Context
import androidx.room.Room
import com.githubrepos.common.di.qualifier.Github
import com.githubrepos.common.di.scope.PerApplication
import com.githubrepos.data.datasource.api.GithubApiDataSource
import com.githubrepos.data.datasource.room.AppDatabase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class DataSourceModule {

    @Provides
    @PerApplication
    fun provideGithubApiDataSource(@Github retrofit: Retrofit): GithubApiDataSource =
        retrofit.create(GithubApiDataSource::class.java)

    @Provides
    @PerApplication
    fun provideAppDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "github-repos.db")
        .fallbackToDestructiveMigration()
        .build()
}