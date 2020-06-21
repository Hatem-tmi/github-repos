package com.githubrepos

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Toast
import com.githubrepos.common.di.DaggerAppComponent
import com.githubrepos.common.di.Injectable
import com.githubrepos.common.util.NetworkStateMonitor
import com.githubrepos.common.util.addTo
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MainApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var networkStateMonitor: NetworkStateMonitor

    private val disposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initAppInjection()

        networkStateMonitor.monitorConnection()
            .subscribe(
                {
                    Toast.makeText(
                        applicationContext,
                        getString(if (it) R.string.device_connected else R.string.device_not_connected),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                {
                    Timber.w(it)
                }
            ).addTo(disposable)
    }

    override fun onTerminate() {
        disposable.clear()
        super.onTerminate()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private fun initAppInjection() {
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .inject(this)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is Injectable) {
                    AndroidInjection.inject(activity)
                }
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }
}