package com.githubrepos.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.githubrepos.R
import com.githubrepos.common.di.Injectable
import com.githubrepos.common.di.module.ViewModelFactory
import com.githubrepos.common.util.nonNullObserve
import com.githubrepos.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        binding.viewModel = viewModel
        setSupportActionBar(binding.toolbar)

        nonNullObserve(viewModel.isLoadingLiveData) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }
        nonNullObserve(viewModel.messageLiveData) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }
        nonNullObserve(viewModel.userLiveData) {
            binding.user = it
        }
        viewModel.fetchData()
    }
}