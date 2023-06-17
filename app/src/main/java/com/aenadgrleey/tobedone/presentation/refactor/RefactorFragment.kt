package com.aenadgrleey.tobedone.presentation.refactor

import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aenadgrleey.tobedone.R
import com.aenadgrleey.tobedone.databinding.AddingFragmentBinding
import com.aenadgrleey.tobedone.presentation.SharedViewModel
import com.aenadgrleey.tobedone.presentation.TodoItem
import com.aenadgrleey.tobedone.utils.Importance
import com.google.android.material.R.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class RefactorFragment : Fragment() {
    private var binding: AddingFragmentBinding? = null
    private val viewModel by activityViewModels<SharedViewModel>()
    private var refactoredItem: TodoItem? = null
    private var isCompleted = false
    private var optedDeadline: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val animDuration = resources
            .getInteger(integer.m3_sys_motion_duration_long4).toLong()
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            .apply { duration = animDuration }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            .apply { duration = animDuration }
        super.onCreate(savedInstanceState)
    }

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddingFragmentBinding.inflate(layoutInflater)
        binding!!.deadline.run { paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG }

        refactoredItem =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                requireArguments().getParcelable("todoItem", TodoItem::class.java)
            else
                requireArguments().getParcelable("todoItem")!!

        refactoredItem?.let {
            binding!!.apply {
                body.text = SpannableStringBuilder(it.body)
                importance.check(
                    when (it.importance) {
                        Importance.High -> R.id.high_importance_button
                        Importance.Low -> R.id.low_importance_button
                        else -> R.id.common_importance_button
                    }
                )
                it.deadline?.let {
                    val typedValue = TypedValue()
                    deadlineSwitch.isChecked = true
                    requireContext().theme.resolveAttribute(attr.colorOnSurface, typedValue, true)
                    binding!!.deadline.run {
                        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                    binding!!.deadline.setTextColor(typedValue.data)
                }
                isCompleted = it.completed ?: false
                binding!!.toolbar.menu[0].icon!!.setTint(
                    if (isCompleted) resources.getColor(R.color.green, requireContext().theme)
                    else {
                        val typedValue = TypedValue()
                        requireContext().theme.resolveAttribute(
                            attr.colorOnSurface,
                            typedValue,
                            true
                        )
                        typedValue.data
                    }
                )
            }
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionbarSize = requireContext().resources.getDimension(R.dimen.toolbar_size)
        binding!!.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding!!.run {
            nestedScroll.viewTreeObserver.addOnScrollChangedListener {
                val scrollY: Int = nestedScroll.scrollY // For // ScrollView
                toolbar.elevation =
                    scrollY.coerceAtMost(actionbarSize.toInt()).div(actionbarSize) * 10
            }
        }
        binding!!.deadline.setOnClickListener {
            if (binding!!.deadlineSwitch.isChecked)
                MaterialDatePicker.Builder.datePicker().build()
                    .apply {
                        addOnPositiveButtonClickListener {
                            optedDeadline = Date(it)
                            val dateFormat =
                                SimpleDateFormat("dd LLL yy", resources.configuration.locales[0])
                            binding!!.deadline.text = dateFormat.format(optedDeadline)
                        }
                    }.show(parentFragmentManager, "DATE_PICKER")
        }
        binding!!.deadlineSwitch.setOnClickListener {
            it as MaterialSwitch

            val typedValue = TypedValue()

            if (it.isChecked) {
                requireContext().theme.resolveAttribute(attr.colorOnSurface, typedValue, true)
                binding!!.deadline.run {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            } else {
                requireContext().theme.resolveAttribute(attr.colorOutlineVariant, typedValue, true)
                binding!!.deadline.run { paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG }
            }

            binding!!.deadline.setTextColor(typedValue.data)

        }

        binding!!.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    viewModel.addTodoItem(formTodoItem())
                    parentFragmentManager.popBackStack()
                }

                R.id.done -> {
                    isCompleted = !isCompleted
                    binding!!.toolbar.menu[0].icon!!.setTint(
                        if (isCompleted) resources.getColor(R.color.green, requireContext().theme)
                        else {
                            val typedValue = TypedValue()
                            requireContext().theme.resolveAttribute(
                                attr.colorOnSurface,
                                typedValue,
                                true
                            )
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

        binding!!.deleteButton.setOnClickListener {
            viewModel.deleteTodoItem(formTodoItem())
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        requireArguments().putParcelable("todoItem", formTodoItem())
        super.onDestroy()
    }


    private fun formTodoItem(): TodoItem {
        return binding!!.run {
            TodoItem(
                id = refactoredItem?.id,
                body = body.text.toString(),
                completed = isCompleted,
                importance = when (importance.checkedButtonId) {
                    R.id.low_importance_button -> Importance.Low
                    R.id.high_importance_button -> Importance.High
                    else -> null
                },
                deadline = if (deadlineSwitch.isChecked) optedDeadline else null,
            )
        }
    }

}