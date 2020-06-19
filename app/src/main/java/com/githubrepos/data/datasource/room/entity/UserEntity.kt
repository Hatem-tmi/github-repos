package com.githubrepos.data.datasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @ColumnInfo(name = "ref")
    @PrimaryKey val ref: String,
    @ColumnInfo(name = "thumbnail") var thumbnail: String? = null,
    @ColumnInfo(name = "price") var price: Float? = null,
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "title") var title: String? = null
)