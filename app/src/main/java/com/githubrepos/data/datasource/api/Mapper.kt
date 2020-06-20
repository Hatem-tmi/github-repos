package com.githubrepos.data.datasource.api

import com.githubrepos.data.datasource.api.dto.ERepository
import com.githubrepos.data.datasource.api.dto.EUser
import com.githubrepos.data.model.RepositoryModel
import com.githubrepos.data.model.UserModel

fun EUser.toUserModel(): UserModel {
    return UserModel(
        avatar = avatarUrl,
        fullName = name ?: "???",
        username = login ?: "",
        company = company ?: "",
        location = location ?: ""
    )
}


fun ERepository.toRepositoryModel(): RepositoryModel {
    return RepositoryModel(
        id = id ?: -1,
        language = language ?: "",
        name = name ?: "",
        description = description,
        url = htmlUrl ?: ""
    )
}