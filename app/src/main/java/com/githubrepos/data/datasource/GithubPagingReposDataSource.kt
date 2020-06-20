package com.githubrepos.data.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.githubrepos.common.di.qualifier.Worker
import com.githubrepos.common.util.addTo
import com.githubrepos.data.datasource.api.GithubApiSource
import com.githubrepos.data.datasource.api.toRepositoryModel
import com.githubrepos.data.model.RepositoryModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class GithubPagingReposDataSource(
    val githubApiSource: GithubApiSource,
    val username: String,
    val perPageSize: Int,
    val disposable: CompositeDisposable,
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
        fetchData(page = FIRST_PAGE) {
            callback.onResult(
                it,
                null,
                FIRST_PAGE + 1
            )
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RepositoryModel>) {
        fetchData(page = params.key) {
            callback.onResult(
                it,
                params.key + 1
            )
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RepositoryModel>) {
    }

    private fun fetchData(page: Int, resultCallback: (data: List<RepositoryModel>) -> Unit) {
        githubApiSource
            .getUserRepositories(
                user = username,
                perPage = perPageSize,
                page = page
            )
            .doOnSubscribe { loadStateLiveData.postValue(LoadState.LOADING) }
            .subscribeOn(worker)
            .observeOn(ui)
            .subscribe(
                {
                    resultCallback.invoke(
                        it.filter { it.id != -1 }.map { it.toRepositoryModel() }
                    )
                    loadStateLiveData.postValue(LoadState.SUCCESS)
                },
                {
                    Timber.e(it)
                    loadStateLiveData.postValue(LoadState.ERROR("Error: it.message"))
                }
            )
            .addTo(disposable)
    }

    companion object {
        private const val FIRST_PAGE = 1
    }

    sealed class LoadState {
        object LOADING : LoadState()
        object SUCCESS : LoadState()
        data class ERROR(val msg: String?) : LoadState()
    }
}