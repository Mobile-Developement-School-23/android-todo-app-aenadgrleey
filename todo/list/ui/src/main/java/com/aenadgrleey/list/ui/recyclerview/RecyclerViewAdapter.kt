package com.aenadgrleey.list.ui.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.aenadgrleey.list.ui.model.TodoItem
import com.aenadgrleey.todo.list.ui.databinding.TodoListItemBinding
import com.aenadgrleey.todo.list.ui.databinding.TodoListLastItemBinding


class RecyclerViewAdapter(
    private val scrollUp: () -> Unit,
    private val onTodoItemClick: (TodoItem) -> Unit,
    private val onLastItemClick: () -> Unit,
    private val onCompleteButtonClick: (TodoItem) -> Unit,
    private val onEditButtonClick: (TodoItem) -> Unit,
    private val onDeleteButtonClick: (TodoItem) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mDiffer: AsyncListDiffer<TodoItem> =
        AsyncListDiffer(this, RecyclerViewDiffCallback())

    val todoItems: List<TodoItem>
        get() = mDiffer.currentList

    init {
        mDiffer.addListListener { previousList, _ -> if (previousList.size == 0) scrollUp() }
    }

    fun setTodoItems(newItems: List<TodoItem>) {
        mDiffer.submitList(newItems)
    }


    override fun getItemViewType(position: Int): Int =
        if (position == mDiffer.currentList.lastIndex + 1) lastItemTag else normalItemTag

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            lastItemTag -> RecyclerViewLastItemViewHolder(TodoListLastItemBinding.inflate(LayoutInflater.from(parent.context)))
            else -> RecyclerViewRegularItemViewHolder(TodoListItemBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    //workaround for having excess item that will be handled manually
    override fun getItemCount() = mDiffer.currentList.size + 1

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerViewRegularItemViewHolder) holder.onBind(
            todoItems[position],
            onTodoItemClick,
            onCompleteButtonClick,
            onEditButtonClick, onDeleteButtonClick
        )
        if (holder is RecyclerViewLastItemViewHolder)
            holder.onBind(onLastItemClick)
    }

    companion object {
        const val normalItemTag = 1
        const val lastItemTag = 0
    }
}