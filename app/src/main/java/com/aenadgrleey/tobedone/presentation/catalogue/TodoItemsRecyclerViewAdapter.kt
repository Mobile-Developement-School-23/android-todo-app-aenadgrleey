package com.aenadgrleey.tobedone.presentation.catalogue

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Paint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.aenadgrleey.tobedone.R
import com.aenadgrleey.tobedone.databinding.TodoLastListItemBinding
import com.aenadgrleey.tobedone.databinding.TodoListItemBinding
import com.aenadgrleey.tobedone.presentation.TodoItem
import com.aenadgrleey.tobedone.utils.Importance
import com.google.android.material.imageview.ShapeableImageView


class TodoItemsRecyclerViewAdapter(
    private val onCompletionSwipe: (TodoItem) -> Unit,
    private val onDeleteButtonClick: (TodoItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val todoItems = mutableListOf<TodoItem>()

    inner class ViewHolder(binding: TodoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mainView: ConstraintLayout = binding.root
        val body: AppCompatTextView = binding.body
        val completeIndicator: ShapeableImageView = binding.completeIndicator
        val moreButton: ShapeableImageView = binding.moreButton
    }

    inner class LastItemViewHolder(binding: TodoLastListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (position == todoItems.lastIndex) {
            else -> normalItemTag
//            false -> normalItemTag
//            true -> lastItemTag
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

    override fun getItemCount() = todoItems.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val todoItem = todoItems[position]
            val context = holder.mainView.context
            holder.body.text =
                when (todoItem.importance) {
                    Importance.High -> "‼️"
                    Importance.Low -> "⬇️"
                    else -> ""
                } + todoItem.body

            if (todoItem.completed == true) {
                val colorOnSurface = TypedValue()
                context.theme
                    .resolveAttribute(
                        com.google.android.material.R.attr.colorOnSurface, colorOnSurface, true
                    )

                holder.body.run {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(colorOnSurface.data)
                }
                holder.completeIndicator.imageTintList =
                    ColorStateList.valueOf(
                        context.resources.getColor(R.color.successColor, context.theme)
                    )
            } else {
                val colorOutline = TypedValue()
                context.theme
                    .resolveAttribute(
                        com.google.android.material.R.attr.colorOutlineVariant, colorOutline, true
                    )
                holder.body.run {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    setTextColor(colorOutline.data)
                }

                holder.completeIndicator.imageTintList =
                    ColorStateList.valueOf(colorOutline.data)
            }
        }
    }

    companion object {
        const val normalItemTag = 1
        const val lastItemTag = 0
    }
}