package com.aenadgrleey.list.ui.recyclerview

import android.content.res.ColorStateList
import android.os.Build
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.aenadgrleey.core.ui.resolveColorAttribute
import com.aenadgrleey.list.ui.model.TodoItem
import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.list.ui.R
import com.aenadgrleey.todo.list.ui.databinding.TodoListItemBinding
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Date
import com.aenadgrleey.resources.R as CommonR
import com.google.android.material.R as MaterialR

class RecyclerViewRegularItemViewHolder(binding: TodoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val mainView: View = binding.root
    private val body: AppCompatTextView = binding.body
    private val deadline: AppCompatTextView = binding.deadline
    private val completeIndicator: ShapeableImageView = binding.completeIndicator
    private val moreButton: ShapeableImageView = binding.moreButton
    private val context = mainView.context
    private val res = context.resources

    fun onBind(
        todoItem: TodoItem,
        onTodoItemClick: (TodoItem) -> Unit,
        onCompleteButtonClick: (TodoItem) -> Unit,
        onEditButtonClick: (TodoItem) -> Unit,
        onDeleteButtonClick: (TodoItem) -> Unit,
    ) {
        this.setUpItemView(onItemClick = { onTodoItemClick(todoItem) })
        this.setTextWithImportance(text = todoItem.body, importance = todoItem.importance)
        this.setDeadline(todoItem.deadline)
        this.setCompleted(
            completed = todoItem.completed,
            onCompleteButtonClick = { onCompleteButtonClick(todoItem) })
        this.setUpMenuButton(
            completed = todoItem.completed,
            onCompleteActionClick = { onCompleteButtonClick(todoItem) },
            onEditActionClick = { onEditButtonClick(todoItem) },
            onDeleteActionClick = { onDeleteButtonClick(todoItem) }
        )
    }

    fun setUpItemView(onItemClick: () -> Unit) {
        mainView.setOnClickListener {
            it.performHapticFeedback(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
                else HapticFeedbackConstants.CONTEXT_CLICK
            )
            onItemClick()
        }
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
        completeIndicator.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
            onCompleteButtonClick()
        }
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
                    R.id.edit_task -> {
                        onEditActionClick()
                        it.performHapticFeedback(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
                            else HapticFeedbackConstants.CONTEXT_CLICK
                        )
                    }

                    R.id.delete_task -> {
                        onDeleteActionClick()
                        it.performHapticFeedback(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.REJECT
                            else HapticFeedbackConstants.CONTEXT_CLICK
                        )
                    }

                    R.id.complete_task -> {
                        onCompleteActionClick()
                        it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                    }
                }
                popupMenu.dismiss()
                true
            }
            popupMenu.show()
        }
    }
}
