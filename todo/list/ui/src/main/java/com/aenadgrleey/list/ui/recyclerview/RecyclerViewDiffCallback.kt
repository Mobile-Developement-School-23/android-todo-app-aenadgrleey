package com.aenadgrleey.list.ui.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.aenadgrleey.list.ui.model.TodoItem

class RecyclerViewDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return newItem.created == oldItem.created
                && newItem.completed == oldItem.completed
                && newItem.lastModified == oldItem.lastModified
                && newItem.body == oldItem.body
                && newItem.deadline == oldItem.deadline
                && newItem.importance == oldItem.importance
    }

}