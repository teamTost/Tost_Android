package com.tost.presentation.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tost.R
import com.tost.presentation.problem.widget.TostProgressBar
import java.text.SimpleDateFormat
import java.util.*

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

@BindingAdapter("date")
fun bindingDate(textView: TextView, date: Date?) {
    if (date == null) return
    val format = SimpleDateFormat("yyyy. MM. dd", Locale.KOREA)
    textView.text = format.format(date)
}
