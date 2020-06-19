package com.githubrepos.data.datasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repository")
data class RepositoryEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "language") var language: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "url") var url: String
)