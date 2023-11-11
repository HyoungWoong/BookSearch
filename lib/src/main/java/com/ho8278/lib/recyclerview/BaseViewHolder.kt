package com.ho8278.lib.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseViewHolder<Item : ItemComparator, BindItem : ItemComparator>(
    binding: View
) : ViewHolder(binding) {

    @Suppress("UNCHECKED_CAST")
    fun onBindInternal(item: Item, position: Int) {
        onBind(item as BindItem, position)
    }

    abstract fun onBind(item: BindItem, position: Int)
}