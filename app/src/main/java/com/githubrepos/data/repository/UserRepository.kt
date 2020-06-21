package com.githubrepos.data.repository

import com.githubrepos.common.di.scope.PerApplication
import com.githubrepos.data.datasource.api.GithubApiSource
import com.githubrepos.data.datasource.api.toUserModel
import com.githubrepos.data.datasource.room.AppDatabase
import com.githubrepos.data.datasource.room.toUserEntity
import com.githubrepos.data.datasource.room.toUserModel
import com.githubrepos.data.model.UserModel
import io.reactivex.Single
import javax.inject.Inject

@PerApplication
class UserRepository @Inject constructor(
    val appDatabase: AppDatabase,
    val githubApiSource: GithubApiSource
) {

    fun getUserProfile(username: String): Single<UserModel> {
        return appDatabase.userDao()
            .get()
            .map {
                if (it.isNullOrEmpty()) throw Exception("Empty cache") else it.first().toUserModel()
            }
            .onErrorResumeNext {
                fetchUser(username)
                    .flatMap {
                        appDatabase.userDao().add(it.toUserEntity())
                        Single.fromCallable { it }
                    }
            }
    }

    private fun fetchUser(username: String): Single<UserModel> {
        return githubApiSource.getUserProfile(username)
            .map { it.toUserModel() }
    }
}