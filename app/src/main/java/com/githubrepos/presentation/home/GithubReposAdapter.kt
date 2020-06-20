package com.githubrepos.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.githubrepos.R
import com.githubrepos.data.model.RepositoryModel
import com.githubrepos.databinding.ItemRepositoryBinding

class GithubReposAdapter(
    private val itemClickListener: (RepositoryModel) -> Unit
) : PagedListAdapter<RepositoryModel, GithubReposAdapter.RepositoryViewHolder>(RepositoryDiffUtils()) {

    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: ItemRepositoryBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }

        fun bind(repository: RepositoryModel) {
            binding?.repository = repository
            itemView.setOnClickListener { itemClickListener.invoke(repository) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepositoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
        )

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(repository = it)
        }
    }

    class RepositoryDiffUtils : DiffUtil.ItemCallback<RepositoryModel>() {
        override fun areItemsTheSame(oldItem: RepositoryModel, newItem: RepositoryModel): Boolean {
            return oldItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: RepositoryModel,
            newItem: RepositoryModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}