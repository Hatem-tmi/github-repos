package com.githubrepos.data.datasource.room

import com.githubrepos.data.datasource.room.entity.RepositoryEntity
import com.githubrepos.data.datasource.room.entity.UserEntity
import com.githubrepos.data.model.RepositoryModel
import com.githubrepos.data.model.UserModel

fun UserEntity.toUserModel(): UserModel {
    return UserModel(
        avatar = avatar,
        fullName = fullName,
        username = username,
        company = company,
        location = location
    )
}

fun UserModel.toUserEntity(): UserEntity {
    return UserEntity(
        avatar = avatar,
        fullName = fullName,
        username = username,
        company = company,
        location = location
    )
}

fun RepositoryEntity.toRepositoryModel(): RepositoryModel {
    return RepositoryModel(
        id = id,
        language = language,
        name = name,
        description = description,
        url = url
    )
}

fun RepositoryModel.toRepositoryEntity(): RepositoryEntity {
    return RepositoryEntity(
        id = id,
        language = language,
        name = name,
        description = description,
        url = url
    )
}