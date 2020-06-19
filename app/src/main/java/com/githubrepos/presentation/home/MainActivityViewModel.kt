package com.githubrepos.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.githubrepos.BuildConfig
import com.githubrepos.common.di.qualifier.UI
import com.githubrepos.common.di.qualifier.Worker
import com.githubrepos.common.util.addTo
import com.githubrepos.data.datasource.api.dto.ERepository
import com.githubrepos.data.datasource.api.dto.EUser
import com.githubrepos.data.model.User
import com.githubrepos.data.repository.ReposRepository
import com.githubrepos.data.repository.UserRepository
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val reposRepository: ReposRepository,
    @Worker private val worker: Scheduler,
    @UI private val ui: Scheduler
) : ViewModel() {

    val disposable = CompositeDisposable()
    val isLoadingLiveData = MutableLiveData<Boolean>()
    val messageLiveData = MutableLiveData<String>()
    val userLiveData = MutableLiveData<User>()

    var currentPage = 1

    fun fetchData() {
        Single
            .zip(
                userRepository.getUserProfile(username = BuildConfig.USERNAME),
                reposRepository.getUserRepositories(
                    username = BuildConfig.USERNAME,
                    page = currentPage
                ),
                BiFunction<User, List<ERepository>, Pair<User, List<ERepository>>> { t1, t2 -> t1 to t2 }
            )
            .subscribeOn(worker)
            .observeOn(ui)
            .doOnSubscribe { isLoadingLiveData.value = true }
            .doAfterTerminate { isLoadingLiveData.value = false }
            .subscribe(
                { (user, repositories) ->
                    Timber.d(user.toString())
                    Timber.d(repositories.toString())
                    userLiveData.value = user
                },
                {
                    Timber.e(it)
                    messageLiveData.value = "Error: ${it.message}"
                }
            )
            .addTo(disposable)
    }
}