package com.githubrepos.data.repository

import com.githubrepos.data.datasource.api.GithubApiDataSource
import com.githubrepos.data.datasource.api.toUserModel
import com.githubrepos.data.datasource.room.AppDatabase
import com.githubrepos.data.model.UserModel
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    val appDatabase: AppDatabase,
    val githubApiDataSource: GithubApiDataSource
) {

    fun getUserProfile(username: String): Single<UserModel> {
        return githubApiDataSource.getUserProfile(username)
            .map { it.toUserModel() }
    }
}