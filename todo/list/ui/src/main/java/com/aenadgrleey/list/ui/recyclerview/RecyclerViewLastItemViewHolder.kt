package com.aenadgrleey.list.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.aenadgrleey.todo.list.ui.databinding.TodoListLastItemBinding

class RecyclerViewLastItemViewHolder(binding: TodoListLastItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val mainView = binding.root
    fun onBind(onClick: () -> Unit) {
        mainView.setOnClickListener { onClick() }
    }
}