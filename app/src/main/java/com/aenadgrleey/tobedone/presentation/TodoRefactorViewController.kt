package com.aenadgrleey.tobedone.presentation

import android.app.Activity
import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import android.text.SpannableStringBuilder
import android.util.TypedValue
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import com.aenadgrleey.tobedone.R
import com.aenadgrleey.tobedone.databinding.TodoRefactorFragmentBinding
import com.aenadgrleey.tobedone.presentation.models.TodoItem
import com.aenadgrleey.tobedone.presentation.stateholders.SharedViewModel
import com.aenadgrleey.data.utils.Importance
import com.aenadgrleey.resources.R.*
import com.aenadgrleey.tobedone.utils.TodoRefactorFragmentNavigator
import com.google.android.material.snackbar.Snackbar
import java.util.Date

class TodoRefactorViewController(
    private val activity: Activity,
    var binding: TodoRefactorFragmentBinding?,
    private var refactoredItem: TodoItem? = null,
    private val navigator: TodoRefactorFragmentNavigator,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: SharedViewModel,
) {
    private var isCompleted = false
    private var optedDeadline: Date? = null

    fun setUpViews() {
        refactoredItem?.let { initialViewsSetup(it) }
        setUpDeadlineBlockBehaviour()
        setUpToolbarBehaviour()
        setUpDeleteButton()
        setUpEditText()
    }

    private fun formTodoItem(): TodoItem {
        return binding!!.run {
            TodoItem(
                id = refactoredItem?.id,
                body = body.text.toString(),
                completed = isCompleted,
                importance = when (importance.checkedButtonId) {
                    R.id.low_importance_button -> com.aenadgrleey.data.utils.Importance.Low
                    R.id.high_importance_button -> com.aenadgrleey.data.utils.Importance.High
                    else -> null
                },
                deadline = if (deadlineSwitch.isChecked) optedDeadline else null,
            )
        }
    }

    private fun initialViewsSetup(todoItem: TodoItem) {
        binding!!.apply {
            body.text = SpannableStringBuilder(todoItem.body)
            importance.check(
                when (todoItem.importance) {
                    com.aenadgrleey.data.utils.Importance.High -> R.id.high_importance_button
                    com.aenadgrleey.data.utils.Importance.Low -> R.id.low_importance_button
                    else -> R.id.common_importance_button
                }
            )
            todoItem.deadline?.let {
                val typedValue = TypedValue()
                deadlineSwitch.isChecked = true
                activity.theme
                    .resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
                binding!!.deadline
                    .run { paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() }
                binding!!.deadline.setTextColor(typedValue.data)
                optedDeadline = it
                val dateFormat = SimpleDateFormat("dd LLL yy", activity.resources.configuration.locales[0])
                binding!!.deadline.text = dateFormat.format(optedDeadline)
            }
            isCompleted = todoItem.completed ?: false
            binding!!.toolbar.menu[0].icon!!.setTint(
                if (isCompleted) activity.resources.getColor(color.green, activity.theme)
                else
                    TypedValue().apply {
                        activity.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, this, true)
                    }.data
            )

        }
    }

    private fun setUpEditText() {
        binding!!.body.addTextChangedListener {
            binding!!.bodyHolder.error =
                if (binding!!.body.text.isNullOrEmpty()) activity.resources.getText(string.noText)
                else ""
        }
    }

    private fun setUpDeleteButton() {
        binding!!.deleteButton.setOnClickListener {
            viewModel.deleteTodoItem(formTodoItem())
            navigator.navigateUp()
        }
    }

    private fun setUpDeadlineBlockBehaviour() {
        binding!!.deadline.run { paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG }
        binding!!.deadline.setOnClickListener {
            if (binding!!.deadlineSwitch.isChecked)
                navigator.toCalendar {
                    val date = Date(it)
                    val dateFormat = SimpleDateFormat("dd LLL yy", activity.resources.configuration.locales[0])
                    binding!!.deadline.text = dateFormat.format(date)
                    optedDeadline = date
                }
        }
        binding!!.deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            val typedValue = TypedValue()
            if (isChecked) {
                activity.theme
                    .resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
                binding!!.deadline.run { paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() }
            } else {
                activity.theme
                    .resolveAttribute(com.google.android.material.R.attr.colorOutlineVariant, typedValue, true)
                binding!!.deadline.run { paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG }
            }
            binding!!.deadline.setTextColor(typedValue.data)
        }
    }

    private fun setUpToolbarBehaviour() {
        val actionbarSize = activity.resources.getDimension(dimen.toolbar_size)
        binding!!.toolbar.setNavigationOnClickListener { navigator.navigateUp() }
        binding!!.run {
            nestedScroll.viewTreeObserver.addOnScrollChangedListener {
                val scrollY: Int = nestedScroll.scrollY // For // ScrollView
                toolbar.elevation =
                    scrollY.coerceAtMost(actionbarSize.toInt()).div(actionbarSize) * 10
            }
        }
        setMenuBehavior()
    }

    private fun setMenuBehavior() {
        binding!!.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> if (binding!!.body.text.isNullOrEmpty())
                    binding!!.bodyHolder.error = activity.resources.getText(string.noText)
                else {
                    viewModel.addTodoItem(formTodoItem())
                    navigator.navigateUp()
                }

                R.id.done -> {
                    isCompleted = !isCompleted
                    binding!!.toolbar.menu[0].icon!!.setTint(
                        if (isCompleted) activity.resources.getColor(color.green, activity.theme)
                        else {
                            val typedValue = TypedValue()
                            activity.theme
                                .resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
                            typedValue.data
                        }
                    )
                    Snackbar.make(
                        binding!!.root,
                        if (isCompleted) "Task was completed" else "Task was un-completed",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            true
        }
    }
}