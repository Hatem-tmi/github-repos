package com.githubrepos.presentation.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.githubrepos.BuildConfig
import com.githubrepos.R
import com.githubrepos.common.di.qualifier.UI
import com.githubrepos.common.di.qualifier.Worker
import com.githubrepos.common.util.addTo
import com.githubrepos.data.model.UserModel
import com.githubrepos.data.repository.ReposRepository
import com.githubrepos.data.repository.UserRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val reposRepository: ReposRepository,
    @Worker private val worker: Scheduler,
    @UI private val ui: Scheduler,
    private val context: Context
) : ViewModel() {

    val disposable = CompositeDisposable()
    val messageLiveData = MutableLiveData<String>()
    val userLiveData = MutableLiveData<UserModel>()

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    val pagedListLiveData = reposRepository.getUserRepositories(
        username = BuildConfig.USERNAME,
        disposable = disposable
    )

    fun fetchData() {
        userRepository.getUserProfile(username = BuildConfig.USERNAME)
            .subscribeOn(worker)
            .observeOn(ui)
            .subscribe(
                { user ->
                    Timber.d(user.toString())
                    userLiveData.value = user
                },
                {
                    Timber.e(it)
                    messageLiveData.value = context.getString(R.string.error_message, it.message)
                }
            )
            .addTo(disposable)
    }
}