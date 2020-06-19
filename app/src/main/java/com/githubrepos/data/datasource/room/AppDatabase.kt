package com.githubrepos.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.githubrepos.data.datasource.room.dao.RepositoryDao
import com.githubrepos.data.datasource.room.dao.UserDao
import com.githubrepos.data.datasource.room.entity.RepositoryEntity
import com.githubrepos.data.datasource.room.entity.UserEntity

@Database(
    entities = [UserEntity::class, RepositoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun repositoryDao(): RepositoryDao
}