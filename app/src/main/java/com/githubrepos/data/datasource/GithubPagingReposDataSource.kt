package com.githubrepos.data.datasource

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.githubrepos.R
import com.githubrepos.common.di.qualifier.Worker
import com.githubrepos.common.util.NetworkStateMonitor
import com.githubrepos.common.util.addTo
import com.githubrepos.data.datasource.api.GithubApiSource
import com.githubrepos.data.datasource.api.toRepositoryModel
import com.githubrepos.data.datasource.room.AppDatabase
import com.githubrepos.data.datasource.room.toRepositoryEntity
import com.githubrepos.data.datasource.room.toRepositoryModel
import com.githubrepos.data.model.RepositoryModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class GithubPagingReposDataSource(
    val githubApiSource: GithubApiSource,
    val appDatabase: AppDatabase,
    val networkStateMonitor: NetworkStateMonitor,
    val username: String,
    val perPageSize: Int,
    val disposable: CompositeDisposable,
    val context: Context,
    @Worker val worker: Scheduler,
    @Worker val ui: Scheduler
) : PageKeyedDataSource<Int, RepositoryModel>() {

    val loadStateLiveData by lazy {
        MutableLiveData<LoadState>()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, RepositoryModel>
    ) {

        networkStateMonitor.monitorConnection()
            .first(true)
            .flatMap { connected ->
                if (connected) {
                    fetchData(page = FIRST_PAGE)
                        .map { it to false }
                } else {
                    appDatabase.repositoryDao().get()
                        .map {
                            it.take(perPageSize).map { it.toRepositoryModel() } to true
                        }
                }
            }
            .onErrorResumeNext {
                appDatabase.repositoryDao().get()
                    .map {
                        it.map { it.toRepositoryModel() } to true
                    }
            }
            .subscribeOn(worker)
            .observeOn(ui)
            .doOnSubscribe { loadStateLiveData.postValue(LoadState.LOADING) }
            .subscribe(
                { (repos, fromCache) ->
                    // update ui with result
                    callback.onResult(
                        repos,
                        null,
                        FIRST_PAGE + 1
                    )
                    loadStateLiveData.postValue(
                        LoadState.SUCCESS(
                            if (!fromCache) {
                                context.getString(
                                    R.string.remote_fetch_new_items,
                                    repos.size.toString()
                                )
                            } else {
                                context.getString(
                                    R.string.cache_load_items,
                                    repos.size.toString()
                                )
                            }
                        )
                    )
                },
                {
                    Timber.e(it)
                    LoadState.ERROR(
                        context.getString(
                            R.string.remote_fetch_new_items,
                            it.message
                        )
                    )
                }
            )
            .addTo(disposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RepositoryModel>) {
        fetchData(page = params.key)
            .subscribeOn(worker)
            .observeOn(ui)
            .doOnSubscribe { loadStateLiveData.postValue(LoadState.LOADING) }
            .subscribe(
                {
                    // update ui with result
                    callback.onResult(
                        it,
                        params.key + 1
                    )
                    loadStateLiveData.postValue(
                        LoadState.SUCCESS(
                            context.getString(R.string.remote_fetch_new_items, it.size.toString())
                        )
                    )
                },
                {
                    Timber.e(it)
                    loadStateLiveData.postValue(
                        LoadState.ERROR(
                            context.getString(
                                R.string.remote_fetch_new_items,
                                it.message
                            )
                        )
                    )
                }
            )
            .addTo(disposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RepositoryModel>) {
    }

    private fun fetchData(page: Int): Single<List<RepositoryModel>> {
        return githubApiSource
            .getUserRepositories(
                user = username,
                perPage = perPageSize,
                page = page
            )
            .map { it.filter { it.id != -1 }.map { it.toRepositoryModel() } }
            .doOnSuccess {
                // refresh db with new data
                appDatabase.repositoryDao().run {
                    clear()
                    add(it.map { it.toRepositoryEntity() })
                }
            }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }

    sealed class LoadState {
        object LOADING : LoadState()
        data class SUCCESS(val msg: String?) : LoadState()
        data class ERROR(val msg: String?) : LoadState()
    }
}