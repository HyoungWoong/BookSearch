package com.ho8278.booksearch.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ho8278.booksearch.R
import com.ho8278.booksearch.databinding.ItemBookBinding
import com.ho8278.booksearch.databinding.ItemDividerBinding
import com.ho8278.data.repository.model.Book
import com.ho8278.lib.recyclerview.BaseListAdapter
import com.ho8278.lib.recyclerview.BaseViewHolder

class BooksAdapter(
    private val onClick: (Book) -> Unit
) : BaseListAdapter<ItemHolder, BaseViewHolder<ItemHolder, *>>() {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ItemHolder.BookEntry -> ViewType.BOOK
            is ItemHolder.Divider -> ViewType.DIVIDER
        }.ordinal
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemHolder, *> {
        return when (viewType) {
            ViewType.BOOK.ordinal -> {
                BookViewHolder(
                    ItemBookBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onClick
                )
            }

            ViewType.DIVIDER.ordinal -> {
                DividerViewHolder(
                    ItemDividerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalArgumentException("Unknown viewtype")
        }
    }

    private enum class ViewType {
        BOOK, DIVIDER
    }
}

class BookViewHolder(
    private val binding: ItemBookBinding,
    private val onClick: (Book) -> Unit,
) : BaseViewHolder<ItemHolder, ItemHolder.BookEntry>(binding.root) {
    override fun onBind(item: ItemHolder.BookEntry, position: Int) {
        binding.root.setOnClickListener { onClick(item.book) }

        val appContext = binding.root.context.applicationContext

        binding.title.text = appContext.getString(R.string.title, item.book.title)
        binding.subtitle.text = appContext.getString(R.string.subtitle, item.book.subtitle)
        binding.isbnNumber.text = appContext.getString(R.string.isbn, item.book.isbn13)
        binding.price.text = appContext.getString(R.string.price, item.book.price)

        Glide.with(binding.image)
            .load(item.book.image)
            .into(binding.image)
    }
}

class DividerViewHolder(
    private val binding: ItemDividerBinding,
) : BaseViewHolder<ItemHolder, ItemHolder.Divider>(binding.root) {
    override fun onBind(item: ItemHolder.Divider, position: Int) = Unit
}