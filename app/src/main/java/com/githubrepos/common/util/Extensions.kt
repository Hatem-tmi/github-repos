package com.githubrepos.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
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

fun Context.isDeviceConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

        object : ConnectivityManager.NetworkCallback() {

        }

        return false
    } else {
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

}