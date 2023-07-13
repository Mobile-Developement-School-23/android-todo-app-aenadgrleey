package com.aenadgrleey.tobedone.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.aenadgrleey.tobedone.presentation.models.TodoItem

class TodoItemsDiffCallback(
    private val oldItems: List<TodoItem>,
    private val newItems: List<TodoItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newItems[newItemPosition].id == oldItems[oldItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val new = newItems[newItemPosition]
        val old = oldItems[oldItemPosition]
        return new.created == old.created
                && new.completed == old.completed
                && new.lastModified == old.lastModified
                && new.body == old.body
                && new.deadline == old.deadline
                && new.importance == old.importance
    }

}