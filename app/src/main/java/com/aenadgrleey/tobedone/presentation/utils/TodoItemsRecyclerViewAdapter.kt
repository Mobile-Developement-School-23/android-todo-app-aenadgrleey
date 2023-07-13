package com.aenadgrleey.tobedone.presentation.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.icu.text.SimpleDateFormat
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.get
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aenadgrleey.tobedone.databinding.TodoLastListItemBinding
import com.aenadgrleey.tobedone.databinding.TodoListItemBinding
import com.aenadgrleey.tobedone.presentation.models.TodoItem
import com.aenadgrleey.data.utils.Importance
import com.aenadgrleey.resources.R
import com.google.android.material.R.*
import com.google.android.material.R.string
import com.google.android.material.imageview.ShapeableImageView


class TodoItemsRecyclerViewAdapter(
    private val scrollUp: () -> Unit,
    private val onTodoItemClick: (TodoItem) -> Unit,
    private val onLastItemClick: () -> Unit,
    private val onCompleteButtonClick: (TodoItem) -> Unit,
    private val onEditButtonClick: (TodoItem) -> Unit,
    private val onDeleteButtonClick: (TodoItem) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val todoItems get() = mTodoItems
    private var mTodoItems = emptyList<TodoItem>()

    fun setTodoItems(newItems: List<TodoItem>) {
        val shouldScroll = mTodoItems.size - newItems.size != 1
        val diffCallback = TodoItemsDiffCallback(mTodoItems, newItems)
        mTodoItems = newItems
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        diffCourses.dispatchUpdatesTo(this)
        if (shouldScroll) scrollUp()
    }


    inner class ViewHolder(binding: TodoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mainView: View = binding.root
        val body: AppCompatTextView = binding.body
        val deadline: AppCompatTextView = binding.deadline
        val completeIndicator: ShapeableImageView = binding.completeIndicator
        val moreButton: ShapeableImageView = binding.moreButton
    }

    inner class LastItemViewHolder(binding: TodoLastListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (position == mTodoItems.lastIndex + 1) {
            false -> normalItemTag
            true -> lastItemTag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            lastItemTag -> LastItemViewHolder(
                TodoLastListItemBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )

            else -> ViewHolder(
                TodoListItemBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )
        }
    }

    override fun getItemCount() = mTodoItems.size + 1

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val todoItem = mTodoItems[position]
            val context = holder.mainView.context
            val res = context.resources
            holder.body.text =
                SpannableString(
                    when (todoItem.importance) {
                        Importance.High -> "‼️"
                        Importance.Low -> "⬇️"
                        else -> ""
                    } + todoItem.body
                ).apply {
                    if (todoItem.completed == true)
                        setSpan(
                            StrikethroughSpan(),
                            1,
                            this.length,
                            SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                        )

                }

            holder.deadline.visibility =
                if (todoItem.deadline == null) View.GONE
                else View.VISIBLE
            val dateFormat = SimpleDateFormat("dd LLL yy", res.configuration.locales[0])

            todoItem.deadline?.let {
                holder.deadline.text = res.getText(R.string.deadline)
                    .toString() + " " + dateFormat.format(it).toString()
            }


            val typedValue = TypedValue()
            when (todoItem.completed) {

                true -> {
                    context.theme.resolveAttribute(attr.colorOutline, typedValue, true)
                    val colorOutline = typedValue.data

                    holder.body.run {
                        setTextColor(colorOutline)
                    }
                    holder.completeIndicator.imageTintList =
                        ColorStateList.valueOf(
                            res.getColor(com.aenadgrleey.resources.R.color.green, context.theme)
                        )
                }

                else -> {
                    context.theme.resolveAttribute(attr.colorOnSurface, typedValue, true)
                    val colorOnSurface = typedValue.data
                    context.theme.resolveAttribute(attr.colorOutline, typedValue, true)
                    val colorOutline = typedValue.data
                    context.theme.resolveAttribute(attr.colorSurface, typedValue, true)

                    holder.body.run {
                        setTextColor(colorOnSurface)
                    }

                    holder.completeIndicator.imageTintList =
                        ColorStateList.valueOf(colorOutline)

                }
            }

            holder.completeIndicator.setOnClickListener { onCompleteButtonClick(todoItem) }

            holder.itemView.setOnClickListener { onTodoItemClick(todoItem) }

            holder.moreButton.setOnClickListener {
                PopupMenu(
                    holder.itemView.context,
                    holder.moreButton,
                    Gravity.END,
                    0,
                    com.aenadgrleey.resources.R.style.PopupMenuTodoItem
                ).apply {
                    menuInflater.inflate(com.aenadgrleey.tobedone.R.menu.todo_item_menu, menu)
                    setForceShowIcon(true)
                    menu[0].icon!!.setTint(
                        when (todoItem.completed) {
                            true ->
                                res.getColor(R.color.green, context.theme)

                            else -> {
                                context.theme.resolveAttribute(attr.colorOutline, typedValue, true)
                                typedValue.data
                            }
                        }
                    )
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            com.aenadgrleey.tobedone.R.id.complete_task -> onCompleteButtonClick(todoItem)
                            com.aenadgrleey.tobedone.R.id.edit_task -> onEditButtonClick(todoItem)
                            com.aenadgrleey.tobedone.R.id.delete_task -> onDeleteButtonClick(todoItem)
                        }
                        dismiss()
                        true
                    }
                }.show()
            }
        }
        if (holder is LastItemViewHolder)
            holder.itemView.setOnClickListener { onLastItemClick() }
    }

    companion object {
        const val normalItemTag = 1
        const val lastItemTag = 0
    }
}