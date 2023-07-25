package com.aenadgrleey.list.ui.ioc

import android.annotation.SuppressLint
import android.content.Context
import android.view.HapticFeedbackConstants
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aenadgrleey.core.di.FragmentContext
import com.aenadgrleey.core.di.ViewLifecycleOwner
import com.aenadgrleey.core.di.ViewScope
import com.aenadgrleey.core.ui.resolveColorAttribute
import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.model.UiAction
import com.aenadgrleey.list.ui.model.UiEvent
import com.aenadgrleey.todo.list.ui.databinding.ExpendableToolbarBinding
import com.aenadrgleey.todo.list.domain.TodoListNavigator
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aenadgrleey.resources.R as CommonR
import com.google.android.material.R as MaterialR

@ViewScope
class ExpendableToolbarViewController @Inject constructor(
    @FragmentContext
    private val context: Context,
    private val navigator: TodoListNavigator,
    private val toolbarBinding: ExpendableToolbarBinding,
    private val appBarLayout: AppBarLayout,
    private val viewModel: TodoListViewModel,
    @ViewLifecycleOwner
    private val lifecycleOwner: LifecycleOwner,
) {
    fun onUiEvent(uiEvent: UiEvent) {
        if (uiEvent !is UiEvent.VisibilityChange) return

        if (uiEvent.visible) toolbarBinding.visibilityIcon.run {
            val color = context.resolveColorAttribute(MaterialR.attr.colorPrimary)
            setImageResource(CommonR.drawable.visibility_36px)
            setColorFilter(color)
        }
        else toolbarBinding.visibilityIcon.run {
            val color = context.resolveColorAttribute(MaterialR.attr.colorOnSurfaceVariant)
            setImageResource(CommonR.drawable.visibility_off_36px)
            setColorFilter(color)
        }
    }

    fun setUpToolbar() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.completedCount.collectLatest {
                    @SuppressLint("SetTextI18n")
                    toolbarBinding.helperText.text =
                        context.resources.getText(CommonR.string.tasksCompleted).toString() + " " + it.toString()
                }
            }
        }

        toolbarBinding.visibilityIcon.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
            viewModel.onUiAction(UiAction.ToggledCompletedMark)
        }

        toolbarBinding.settingsButton.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
            navigator.navigateToSettings()
        }

        toolbarBinding.mainText.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
            viewModel.onUiAction(UiAction.SmoothScrollUpRequest)
        }

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            toolbarBinding.motionLayout.progress = seekPosition
        }
    }
}