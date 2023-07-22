package com.aenadgrleey.list.ui.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.aenadgrleey.list.ui.model.TodoItem
import com.aenadgrleey.todo.list.ui.databinding.TodoListItemBinding
import com.aenadgrleey.todo.list.ui.databinding.TodoListLastItemBinding
import kotlin.math.abs


class TodoItemsRecyclerViewAdapter(
    private val scrollUp: () -> Unit,
    private val onTodoItemClick: (TodoItem) -> Unit,
    private val onLastItemClick: () -> Unit,
    private val onCompleteButtonClick: (TodoItem) -> Unit,
    private val onEditButtonClick: (TodoItem) -> Unit,
    private val onDeleteButtonClick: (TodoItem) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mDiffer: AsyncListDiffer<TodoItem> =
        AsyncListDiffer(this, TodoItemsDiffCallback())

    val todoItems: List<TodoItem>
        get() = mDiffer.currentList

    init {
        mDiffer.addListListener { previousList, currentList ->
            if (abs(previousList.size - currentList.size) >= 1) scrollUp()
        }
    }

    fun setTodoItems(newItems: List<TodoItem>) {
        mDiffer.submitList(newItems)
    }

    inner class LastItemViewHolder(binding: TodoListLastItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int =
        if (position == mDiffer.currentList.lastIndex + 1) lastItemTag else normalItemTag

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            lastItemTag -> LastItemViewHolder(TodoListLastItemBinding.inflate(LayoutInflater.from(parent.context)))
            else -> TodoItemViewHolder(TodoListItemBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    //workaround for having excess item that will be handled manually
    override fun getItemCount() = mDiffer.currentList.size + 1

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoItemViewHolder) {
            val todoItem = todoItems[position]
            holder.setUpItemView(onItemClick = { onTodoItemClick(todoItem) })
            holder.setTextWithImportance(text = todoItem.body, importance = todoItem.importance)
            holder.setDeadline(todoItem.deadline)
            holder.setCompleted(
                completed = todoItem.completed,
                onCompleteButtonClick = { onCompleteButtonClick(todoItem) })
            holder.setUpMenuButton(
                completed = todoItem.completed,
                onCompleteActionClick = { onCompleteButtonClick(todoItem) },
                onEditActionClick = { onEditButtonClick(todoItem) },
                onDeleteActionClick = { onDeleteButtonClick(todoItem) }
            )
        }
        if (holder is LastItemViewHolder)
            holder.itemView.setOnClickListener { onLastItemClick() }
    }

    companion object {
        const val normalItemTag = 1
        const val lastItemTag = 0
    }
}