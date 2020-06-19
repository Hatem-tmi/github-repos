package com.githubrepos.common.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bind:imageUrl", "bind:placeholder")
fun setImageUrl(imageView: ImageView, url: String?, placeholder: Drawable) {
    Glide.with(imageView.context)
        .load(url)
        .placeholder(placeholder)
        .error(placeholder)
        .circleCrop()
        .into(imageView)
}