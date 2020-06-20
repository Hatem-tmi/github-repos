package com.githubrepos.data.repository

import com.githubrepos.data.datasource.api.GithubApiSource
import com.githubrepos.data.datasource.api.toUserModel
import com.githubrepos.data.datasource.room.AppDatabase
import com.githubrepos.data.model.UserModel
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    val appDatabase: AppDatabase,
    val githubApiSource: GithubApiSource
) {

    fun getUserProfile(username: String): Single<UserModel> {
        return githubApiSource.getUserProfile(username)
            .map { it.toUserModel() }
    }
}