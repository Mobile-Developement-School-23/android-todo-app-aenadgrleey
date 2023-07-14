package com.aenadgrleey.todolist.ui.ioc

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aenadgrleey.di.FragmentContext
import com.aenadgrleey.di.ViewLifecycleOwner
import com.aenadgrleey.di.ViewScope
import com.aenadgrleey.resources.R
import com.aenadgrleey.todolist.ui.TodoListViewModel
import com.aenadgrleey.todolist.ui.databinding.ExpendableToolbarBinding
import com.aenadgrleey.todolist.ui.model.UiAction
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewScope
class ExpendableToolbarViewController @Inject constructor(
    @FragmentContext
    private val context: Context,
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

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            toolbarBinding.motionLayout.progress = seekPosition
        }
    }
}