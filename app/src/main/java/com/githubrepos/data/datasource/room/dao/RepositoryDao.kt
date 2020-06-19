package com.githubrepos.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githubrepos.data.datasource.room.entity.RepositoryEntity
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(repository: RepositoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(repositories: List<RepositoryEntity>)

    @Query("DELETE FROM repository WHERE id=:id")
    fun remove(id: Int)

    @Query("DELETE FROM repository")
    fun clear()

    @Query("SELECT * from repository")
    fun get(): Single<List<RepositoryEntity>>

    @Query("SELECT * from repository WHERE id = :id")
    fun getWithRef(id: String): Maybe<RepositoryEntity>
}