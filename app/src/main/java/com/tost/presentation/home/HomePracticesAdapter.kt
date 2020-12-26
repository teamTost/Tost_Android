package com.tost.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tost.data.entity.Part
import com.tost.databinding.ItemPracticePartBinding

/**
 * Created By Malibin
 * on 12월 26, 2020
 */

class HomePracticesAdapter(
    private val parts: Array<Part>
) : RecyclerView.Adapter<HomePracticesAdapter.ViewHolder>() {

    private var onPracticeClickListener: ((Part) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPracticePartBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parts[position])
    }

    override fun getItemCount(): Int = parts.size

    fun setOnPracticeClickListener(listener: ((Part) -> Unit)?) {
        this.onPracticeClickListener = listener
    }

    inner class ViewHolder(
        private val binding: ItemPracticePartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(part: Part) {
            binding.part = part
            binding.root.setOnClickListener { onPracticeClickListener?.invoke(part) }
        }
    }
}
