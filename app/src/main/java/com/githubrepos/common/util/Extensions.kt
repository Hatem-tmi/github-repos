package com.githubrepos.common.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, onChanged: (T?) -> Unit) {
    liveData.observe(this, Observer(onChanged))
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.nonNullObserve(liveData: L, onChanged: (T) -> Unit) {
    liveData.observe(this, Observer {
        it?.let(onChanged)
    })
}

fun Disposable.addTo(disposable: CompositeDisposable): Disposable {
    disposable.add(this)
    return this
}