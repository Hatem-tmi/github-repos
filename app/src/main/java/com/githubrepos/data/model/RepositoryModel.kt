package com.githubrepos.data.model

data class RepositoryModel(
    val id: Int,
    val language: String,
    val name: String,
    val description: String?,
    val url: String
)