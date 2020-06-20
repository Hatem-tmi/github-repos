package com.githubrepos.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.githubrepos.common.di.qualifier.Worker
import com.githubrepos.data.datasource.GithubPagingReposDataSource
import com.githubrepos.data.datasource.api.GithubApiSource
import com.githubrepos.data.datasource.room.AppDatabase
import com.githubrepos.data.model.RepositoryModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ReposRepository @Inject constructor(
    val appDatabase: AppDatabase,
    val githubApiSource: GithubApiSource,
    @Worker val worker: Scheduler,
    @Worker val ui: Scheduler,
    val context: Context
) {

    fun getUserRepositories(
        username: String,
        perPageSize: Int = 10,
        prefetchDistance: Int = 3,
        disposable: CompositeDisposable
    ): Pair<LiveData<PagedList<RepositoryModel>>, LiveData<GithubPagingReposDataSource.LoadState>> {

        val reposDataSourceLiveData = MutableLiveData<GithubPagingReposDataSource>()

        val dataSourceFactory = object : DataSource.Factory<Int, RepositoryModel>() {
            override fun create(): DataSource<Int, RepositoryModel> {
                return GithubPagingReposDataSource(
                    githubApiSource = githubApiSource,
                    username = username,
                    perPageSize = perPageSize,
                    disposable = disposable,
                    worker = worker,
                    ui = ui
                ).also {
                    reposDataSourceLiveData.postValue(it)
                }
            }
        }

        val reposPagedListLiveData = LivePagedListBuilder(
            dataSourceFactory,
            PagedList.Config.Builder()
                .setPageSize(perPageSize)
                .setEnablePlaceholders(false)
                .setPrefetchDistance(prefetchDistance)
                .build()
        ).build()


        val loadStateLiveData =
            Transformations.switchMap(reposDataSourceLiveData) { it.loadStateLiveData }

        return reposPagedListLiveData to loadStateLiveData
    }
}