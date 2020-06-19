package com.githubrepos.data.repository

import com.githubrepos.data.datasource.api.GithubApiDataSource
import com.githubrepos.data.datasource.api.toRepositoryModel
import com.githubrepos.data.datasource.room.AppDatabase
import com.githubrepos.data.model.RepositoryModel
import io.reactivex.Single
import javax.inject.Inject

class ReposRepository @Inject constructor(
    val appDatabase: AppDatabase,
    val githubApiDataSource: GithubApiDataSource
) {
    val PAGE_COUNT = 10

    fun getUserRepositories(username: String, page: Int): Single<List<RepositoryModel>> {
        return githubApiDataSource.getUserRepositories(username, PAGE_COUNT, page)
            .map { it.map { it.toRepositoryModel() } }
    }
}