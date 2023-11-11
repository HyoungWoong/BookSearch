package com.ho8278.lib.recyclerview

interface ItemComparator {
    fun areItemSame(other:ItemComparator) = this == other
    fun areContentSame(other:ItemComparator) = this == other
}