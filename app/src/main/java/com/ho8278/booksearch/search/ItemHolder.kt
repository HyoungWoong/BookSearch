package com.ho8278.booksearch.search

import com.ho8278.data.repository.model.Book
import com.ho8278.lib.recyclerview.ItemComparator

sealed class ItemHolder : ItemComparator {
    data class BookEntry(val book: Book) : ItemHolder() {
        override fun areItemSame(other: ItemComparator): Boolean {
            return if (other !is BookEntry) false
            else this.book.isbn13 == other.book.isbn13
        }
    }

    data object Divider : ItemHolder()
}