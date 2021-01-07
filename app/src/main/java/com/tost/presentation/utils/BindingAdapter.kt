package com.tost.presentation.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
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
    if (imageUrl.isNullOrBlank()) return
    Glide.with(imageView)
        .load(imageUrl)
        .placeholder(R.drawable.image)
        .into(imageView)
}

@BindingAdapter("progress")
fun bindingProgress(tostProgressBar: TostProgressBar, progress: Int?) {
    tostProgressBar.progress = progress ?: return
}

@BindingAdapter("date_text")
fun bindingDate(textView: TextView, date: Date?) {
    if (date == null) return
    val format = SimpleDateFormat("yyyy. MM. dd", Locale.KOREA)
    textView.text = format.format(date)
}

@BindingAdapter("html_text")
fun bindingHtmlText(textView: TextView, content: String?) {
    if (content.isNullOrBlank()) return
    textView.text = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_COMPACT)
}
