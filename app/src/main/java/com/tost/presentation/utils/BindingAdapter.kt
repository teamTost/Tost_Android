package com.tost.presentation.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tost.R
import com.tost.presentation.problem.widget.TostProgressBar

/**
 * Created By Malibin
 * on 12월 03, 2020
 */

@BindingAdapter("image_url")
fun bindingImageUrl(imageView: ImageView, imageUrl: String?) {
    if (imageUrl == null) return
    Glide.with(imageView)
        .load(imageUrl)
        .placeholder(R.drawable.image)
        .into(imageView)
}

@BindingAdapter("progress")
fun bindingProgress(tostProgressBar: TostProgressBar, progress: Int?) {
    tostProgressBar.progress = progress ?: return
}
