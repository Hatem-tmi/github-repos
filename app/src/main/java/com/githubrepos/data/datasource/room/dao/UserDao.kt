package com.githubrepos.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githubrepos.data.datasource.room.entity.UserEntity
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(users: List<UserEntity>)

    @Query("DELETE FROM user WHERE ref=:ref")
    fun remove(ref: String)

    @Query("DELETE FROM user")
    fun clear()

    @Query("SELECT * from user")
    fun get(): Single<List<UserEntity>>

    @Query("SELECT * from user WHERE ref = :ref")
    fun getWithRef(ref: String): Maybe<UserEntity>
}