package com.githubrepos.data.datasource.api

import com.githubrepos.data.datasource.api.dto.EUser
import com.githubrepos.data.model.User

fun EUser.toUserModel(): User {
    return User(
        avatar = avatarUrl,
        fullName = name ?: "???",
        username = login ?: "",
        company = company ?: "",
        location = location ?: ""
    )
}