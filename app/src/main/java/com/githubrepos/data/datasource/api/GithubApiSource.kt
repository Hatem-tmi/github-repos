package com.githubrepos.data.datasource.api

import com.githubrepos.data.datasource.api.dto.ERepository
import com.githubrepos.data.datasource.api.dto.EUser
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiSource {

    @GET("users/{user}")
    fun getUserProfile(@Path("user") user: String): Single<EUser>

    @GET("users/{user}/repos")
    fun getUserRepositories(
        @Path("user") user: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Single<List<ERepository>>
}