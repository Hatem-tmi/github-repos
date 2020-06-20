package com.githubrepos.presentation.home

import android.content.Intent
import android.net.Uri
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
import com.githubrepos.data.datasource.GithubPagingReposDataSource
import com.githubrepos.databinding.ActivityMainBinding
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    private val reposAdapter by lazy {
        GithubReposAdapter() {
            if (it.url.isNotBlank()) {
                Intent(Intent.ACTION_VIEW, Uri.parse(it.url)).run { startActivity(this) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        binding.viewModel = viewModel
        setSupportActionBar(binding.toolbar)

        binding.recyclerView.apply {
            adapter = reposAdapter
        }

        nonNullObserve(viewModel.messageLiveData) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }
        nonNullObserve(viewModel.userLiveData) {
            binding.user = it
        }
        nonNullObserve(viewModel.pagedListLiveData.first) { pagedList ->
            reposAdapter.submitList(pagedList)
        }
        nonNullObserve(viewModel.pagedListLiveData.second) { loadState ->
            when (loadState) {
                GithubPagingReposDataSource.LoadState.LOADING -> binding.loading.visibility =
                    View.VISIBLE
                GithubPagingReposDataSource.LoadState.SUCCESS -> binding.loading.visibility =
                    View.GONE
                is GithubPagingReposDataSource.LoadState.ERROR -> {
                    binding.loading.visibility = View.GONE
                    Toast.makeText(applicationContext, loadState.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.fetchData()
    }
}