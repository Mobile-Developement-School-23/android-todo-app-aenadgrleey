package com.aenadgrleey.todolist.ui.ioc

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aenadgrleey.core.di.FragmentContext
import com.aenadgrleey.core.di.ViewLifecycleOwner
import com.aenadgrleey.core.di.ViewScope
import com.aenadgrleey.resources.R
import com.aenadgrleey.todolist.ui.TodoListViewModel
import com.aenadgrleey.todolist.ui.model.UiEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewScope
class CoordinatorViewController @Inject constructor(
    @FragmentContext
    private val context: Context,
    private val coordinatorLayout: CoordinatorLayout,
    private val viewModel: TodoListViewModel,
    @ViewLifecycleOwner
    private val lifecycleOwner: LifecycleOwner,
) {
    fun setUpCoordinator() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coordinatorEvents.collectLatest { event ->
                    when (event) {
                        UiEvent.ConnectionError -> context.resources.getString(R.string.noInternetError)
                        UiEvent.SyncedWithServer -> context.resources.getString(R.string.synced)
                        UiEvent.BadServerResponse -> context.resources.getString(R.string.serverError)
                        else -> null
                    }?.let { Snackbar.make(coordinatorLayout, it, Snackbar.LENGTH_SHORT).show() }
                }
            }
        }
    }
}