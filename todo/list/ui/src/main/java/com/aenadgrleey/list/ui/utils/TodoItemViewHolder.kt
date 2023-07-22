package com.aenadgrleey.list.ui.utils

import android.content.res.ColorStateList
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.aenadgrleey.core.ui.resolveColorAttribute
import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.list.ui.R
import com.aenadgrleey.todo.list.ui.databinding.TodoListItemBinding
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Date
import com.aenadgrleey.resources.R as CommonR
import com.google.android.material.R as MaterialR

class TodoItemViewHolder(binding: TodoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val mainView: View = binding.root
    private val body: AppCompatTextView = binding.body
    private val deadline: AppCompatTextView = binding.deadline
    private val completeIndicator: ShapeableImageView = binding.completeIndicator
    private val moreButton: ShapeableImageView = binding.moreButton
    private val context = mainView.context
    private val res = context.resources

    fun setUpItemView(onItemClick: () -> Unit) {
        mainView.setOnClickListener { onItemClick() }
    }

    fun setTextWithImportance(text: String, importance: Importance) {
        val importanceSymbol = when (importance) {
            Importance.High -> "‼️"
            Importance.Low -> "⬇️"
            else -> ""
        }
        body.text = SpannableString(importanceSymbol + text)
    }

    fun setDeadline(deadlineDate: Date?) {
        if (deadlineDate == null) {
            deadline.visibility = View.GONE
            return
        } else deadline.visibility = View.VISIBLE

        val dateFormat = SimpleDateFormat("HH:mm dd LLL yy", res.configuration.locales[0])
        deadline.text = res.getString(CommonR.string.deadline, dateFormat.format(deadlineDate))
    }

    fun setCompleted(completed: Boolean, onCompleteButtonClick: () -> Unit) {
        if (completed) {
            body.text = SpannableString(body.text).also { it.setSpan(StrikethroughSpan(), 1, body.text.length, SpannableString.SPAN_INCLUSIVE_INCLUSIVE) }
            body.setTextColor(context.resolveColorAttribute(MaterialR.attr.colorOutline))
            completeIndicator.imageTintList = ColorStateList.valueOf(res.getColor(CommonR.color.green, context.theme))
        } else {
            body.text = body.text
            body.setTextColor(context.resolveColorAttribute(MaterialR.attr.colorOnSurface))
            completeIndicator.imageTintList = ColorStateList.valueOf(context.resolveColorAttribute(MaterialR.attr.colorOutline))
        }
        completeIndicator.setOnClickListener { onCompleteButtonClick() }
    }

    fun setUpMenuButton(
        completed: Boolean,
        onCompleteActionClick: () -> Unit,
        onEditActionClick: () -> Unit,
        onDeleteActionClick: () -> Unit,
    ) {
        moreButton.setOnClickListener {
            val popupMenu = PopupMenu(context, it, Gravity.END, 0, CommonR.style.PopupMenuTodoItem)

            popupMenu.menuInflater.inflate(R.menu.todo_item_menu, popupMenu.menu)
            popupMenu.setForceShowIcon(true)
            popupMenu.menu[0].icon!!.setTint(
                when (completed) {
                    true -> res.getColor(CommonR.color.green, context.theme)
                    else -> context.resolveColorAttribute(MaterialR.attr.colorOutline)
                }
            )
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.complete_task -> onCompleteActionClick()
                    R.id.edit_task -> onEditActionClick()
                    R.id.delete_task -> onDeleteActionClick()
                }
                popupMenu.dismiss()
                true
            }
            popupMenu.show()
        }
    }
}
