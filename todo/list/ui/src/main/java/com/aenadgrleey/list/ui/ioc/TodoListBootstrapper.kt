package com.aenadgrleey.list.ui.ioc

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aenadgrleey.core.di.ViewLifecycleOwner
import com.aenadgrleey.core.di.ViewScope
import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.model.UiAction
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewScope
class TodoListBootstrapper
@Inject constructor(
    viewModel: TodoListViewModel,
    toolbarController: ExpendableToolbarViewController,
    fabController: FabViewController,
    recyclerViewController: RecyclerViewController,
    refreshViewController: SwipeRefreshViewController,
    coordinatorViewController: CoordinatorViewController,
    @ViewLifecycleOwner
    private val lifecycleOwner: LifecycleOwner,
) {
    init {
        toolbarController.setUpToolbar()
        fabController.setUpFab()
        recyclerViewController.setUpRecycler()
        refreshViewController.setUpSwipeRefreshLayout()
        viewModel.onUiAction(UiAction.RefreshTodoItems)
        viewModel.onUiAction(UiAction.SmoothScrollUpRequest)
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvents.collect {
                    coordinatorViewController.onUiEvent(it)
                    refreshViewController.onUiEvent(it)
                    refreshViewController.onUiEvent(it)
                    toolbarController.onUiEvent(it)
                    recyclerViewController.onUiEvent(it)
                }
            }
        }
    }
}