package com.ho8278.lib.recyclerview

import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<T : ItemComparator, VH : BaseViewHolder<T, *>> :
    ListAdapter<T, VH>(SimpleDiffUtil<T>()) {
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBindInternal(getItem(position), position)
    }
}