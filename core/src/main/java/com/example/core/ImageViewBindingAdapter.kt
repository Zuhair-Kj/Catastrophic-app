package com.example.core

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition

object ImageViewBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["binding:imageUrl", "binding:placeholder"], requireAll = true)
    fun loadImage(imageView: ImageView, url: String?, resource: Drawable) {
        if (!url.isNullOrBlank()) {
            Glide.with(imageView.context).load(url).placeholder(resource).into(imageView)
        } else {
            Glide.with(imageView)
                .load(url)
                .placeholder(resource)
                .thumbnail(0.05f)
                .override(imageView.width, imageView.height)
                .apply(RequestOptions.centerCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions.withCrossFade(android.R.integer.config_mediumAnimTime))
                .into(imageView)
        }
    }

}