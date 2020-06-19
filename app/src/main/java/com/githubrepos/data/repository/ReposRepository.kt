package com.githubrepos.data.repository

import com.githubrepos.data.datasource.api.GithubApiDataSource
import com.githubrepos.data.datasource.api.dto.ERepository
import com.githubrepos.data.datasource.room.AppDatabase
import io.reactivex.Single
import javax.inject.Inject

class ReposRepository @Inject constructor(
    val appDatabase: AppDatabase,
    val githubApiDataSource: GithubApiDataSource
) {
    val PAGE_COUNT = 10

    fun getUserRepositories(username: String, page: Int): Single<List<ERepository>> {
        return githubApiDataSource.getUserRepositories(username, PAGE_COUNT, page)
    }
}