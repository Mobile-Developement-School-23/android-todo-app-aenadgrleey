package com.aenadgrleey.list.ui.ioc

import androidx.lifecycle.LifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aenadgrleey.core.di.ViewLifecycleOwner
import com.aenadgrleey.core.di.ViewScope
import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.model.UiAction
import com.aenadgrleey.list.ui.model.UiEvent
import com.aenadgrleey.list.ui.utils.toPx
import javax.inject.Inject

@ViewScope
class SwipeRefreshViewController @Inject constructor(
    private val swipeRefreshLayout: SwipeRefreshLayout,
    private val viewModel: TodoListViewModel,
    @ViewLifecycleOwner
    private val lifecycleOwner: LifecycleOwner,
) {
    fun onUiEvent(uiEvent: UiEvent) {
        swipeRefreshLayout.isRefreshing = uiEvent == UiEvent.SyncingWithServer
    }

    fun setUpSwipeRefreshLayout() {
        swipeRefreshLayout.run {
            setProgressViewOffset(false, (-128).toPx, 72.toPx)
            setOnRefreshListener { viewModel.onUiAction(UiAction.RefreshTodoItems) }
        }
    }
}