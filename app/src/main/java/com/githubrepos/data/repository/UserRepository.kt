package com.githubrepos.data.repository

import com.githubrepos.data.datasource.api.GithubApiDataSource
import com.githubrepos.data.datasource.api.dto.EUser
import com.githubrepos.data.datasource.room.AppDatabase
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    val appDatabase: AppDatabase,
    val githubApiDataSource: GithubApiDataSource
) {

    fun getUserProfile(username: String): Single<EUser> {
        return githubApiDataSource.getUserProfile(username)
    }
}