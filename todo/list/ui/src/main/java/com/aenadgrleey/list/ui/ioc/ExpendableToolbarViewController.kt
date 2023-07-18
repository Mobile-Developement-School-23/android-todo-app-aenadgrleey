package com.aenadgrleey.list.ui.ioc

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aenadgrleey.core.di.FragmentContext
import com.aenadgrleey.core.di.ViewLifecycleOwner
import com.aenadgrleey.core.di.ViewScope
import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.model.UiAction
import com.aenadgrleey.resources.R
import com.aenadgrleey.todo.list.ui.databinding.ExpendableToolbarBinding
import com.aenadrgleey.todo.list.domain.TodoListNavigator
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    fun setUpToolbar() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isShowingCompleted.collect { show ->
                    if (show) toolbarBinding.visibilityIcon.setImageResource(R.drawable.visibility_48px)
                    else toolbarBinding.visibilityIcon.setImageResource(R.drawable.visibility_off_48px)
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.completedCount.collectLatest {
                    @SuppressLint("SetTextI18n")
                    toolbarBinding.helperText.text =
                        context.resources.getText(R.string.tasksCompleted).toString() + " " + it.toString()
                }
            }
        }

        toolbarBinding.visibilityIcon.setOnClickListener { viewModel.onUiAction(UiAction.ToggledCompletedMark) }

        toolbarBinding.settingsButton.setOnClickListener { navigator.navigateToSettings() }

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            toolbarBinding.motionLayout.progress = seekPosition
        }
    }
}