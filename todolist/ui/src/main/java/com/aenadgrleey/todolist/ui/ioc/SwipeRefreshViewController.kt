package com.aenadgrleey.todolist.ui.ioc

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aenadgrleey.di.ViewLifecycleOwner
import com.aenadgrleey.di.ViewScope
import com.aenadgrleey.todolist.ui.TodoListViewModel
import com.aenadgrleey.todolist.ui.model.UiAction
import com.aenadgrleey.todolist.ui.model.UiEvent
import com.aenadgrleey.todolist.ui.utils.toPx
import kotlinx.coroutines.Dispatchers
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
        Log.v("SwipeRefreshController", "init")
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                println("collectStarted")
                viewModel.swipeRefreshEvents.collect {
                    println("collected $it")
                    if (it == UiEvent.ConnectionError
                        || it == UiEvent.BadServerResponse
                        || it == UiEvent.SyncedWithServer
                        || it == null
                    ) swipeRefreshLayout.isRefreshing = false
                }
                println("collectEnded")
            }
        }
        swipeRefreshLayout.run {
            setProgressViewOffset(false, (-48).toPx, 72.toPx)
            setOnRefreshListener { viewModel.onUiAction(UiAction.RefreshTodoItems) }
        }
    }
}