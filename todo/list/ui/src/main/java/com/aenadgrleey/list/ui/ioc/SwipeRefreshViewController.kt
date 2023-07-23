package com.aenadgrleey.list.ui.ioc

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aenadgrleey.core.di.ViewLifecycleOwner
import com.aenadgrleey.core.di.ViewScope
import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.model.UiAction
import com.aenadgrleey.list.ui.model.UiEvent
import com.aenadgrleey.list.ui.utils.toPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewScope
class SwipeRefreshViewController @Inject constructor(
    private val swipeRefreshLayout: SwipeRefreshLayout,
    private val viewModel: TodoListViewModel,
    @ViewLifecycleOwner
    private val lifecycleOwner: LifecycleOwner,
) {
    fun setUpSwipeRefreshLayout() {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.swipeRefreshEvents.collectLatest {
                    swipeRefreshLayout.isRefreshing = it == UiEvent.SyncingWithServer
                }
            }
        }
        swipeRefreshLayout.run {
            setProgressViewOffset(false, (-128).toPx, 72.toPx)
            setOnRefreshListener { viewModel.onUiAction(UiAction.RefreshTodoItems) }
        }
    }
}