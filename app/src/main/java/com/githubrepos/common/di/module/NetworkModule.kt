package com.githubrepos.common.di.module

import com.githubrepos.BuildConfig
import com.githubrepos.common.di.qualifier.Github
import com.githubrepos.common.di.qualifier.Worker
import com.githubrepos.common.di.scope.PerApplication
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @PerApplication
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    @Provides
    @PerApplication
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @PerApplication
    @Github
    fun provideGithubRetrofit(client: OkHttpClient, @Worker scheduler: Scheduler): Retrofit =
        Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(scheduler))
            .addConverterFactory(
                Json(JsonConfiguration(ignoreUnknownKeys = true)).asConverterFactory(
                    contentType = "application/json".toMediaType()
                )
            )
            .baseUrl(BuildConfig.GITHUB_API_URL)
            .build()
}