package com.ho8278.lib.recyclerview

import androidx.recyclerview.widget.DiffUtil

class SimpleDiffUtil<T : ItemComparator> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem.areItemSame(newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.areContentSame(newItem)
}