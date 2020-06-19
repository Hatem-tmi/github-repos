package com.githubrepos.data.datasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @ColumnInfo(name = "username")
    @PrimaryKey val username: String,
    @ColumnInfo(name = "avatar") var avatar: String? = null,
    @ColumnInfo(name = "fullName") var fullName: String,
    @ColumnInfo(name = "company") var company: String,
    @ColumnInfo(name = "location") var location: String
)