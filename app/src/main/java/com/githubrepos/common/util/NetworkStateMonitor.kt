package com.githubrepos.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.githubrepos.common.di.scope.PerApplication
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@PerApplication
class NetworkStateMonitor @Inject constructor(
    val context: Context
) : ConnectivityManager.NetworkCallback() {

    private val connectionSubject = BehaviorSubject.createDefault(false)

    init {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        super.onBlockedStatusChanged(network, blocked)
        if (connectionSubject.value == blocked) connectionSubject.onNext(!blocked)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        if (connectionSubject.value == true) connectionSubject.onNext(false)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        if (connectionSubject.value == true) connectionSubject.onNext(false)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        if (connectionSubject.value == false) connectionSubject.onNext(true)
    }

    fun monitorConnection(): Observable<Boolean> {
        return connectionSubject
    }
}