package com.aenadgrleey.list.ui.ioc

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aenadgrleey.core.di.FragmentContext
import com.aenadgrleey.core.di.ViewLifecycleOwner
import com.aenadgrleey.core.di.ViewScope
import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.model.UiAction
import com.aenadgrleey.list.ui.model.UiEvent
import com.aenadgrleey.list.ui.recyclerview.RecyclerViewAdapter
import com.aenadgrleey.list.ui.recyclerview.RecyclerViewSwipeCallback
import com.aenadgrleey.list.ui.utils.Visibility
import com.aenadgrleey.list.ui.utils.animateVisibility
import com.aenadgrleey.list.ui.utils.toPx
import com.aenadgrleey.todo.list.ui.databinding.NothingFoundBannerBinding
import com.aenadrgleey.todo.list.domain.TodoListNavigator
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewScope
class RecyclerViewController @Inject constructor(
    @FragmentContext
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val nothingFoundBanner: NothingFoundBannerBinding,
    @ViewLifecycleOwner
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: TodoListViewModel,
    private val navigator: TodoListNavigator,
) {
    private val adapter = RecyclerViewAdapter(
        scrollUp = { viewModel.onUiAction(UiAction.ImmediateScrollUpRequest) },
        onTodoItemClick = { navigator.navigateToRefactorFragment(it.id) },
        onLastItemClick = { navigator.navigateToRefactorFragment(null) },
        onCompleteButtonClick = { viewModel.onUiAction(UiAction.AddTodoItem(it.copy(completed = !it.completed))) },
        onEditButtonClick = { navigator.navigateToRefactorFragment(it.id) },
        onDeleteButtonClick = { viewModel.onUiAction(UiAction.DeleteTodoItem(it)) }
    )

    fun setUpRecycler() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recyclerEvents.collect {
                    if (it == UiEvent.RecyclerEvent.ScrollUp) recyclerView.smoothScrollToPosition(0)
                    if (it == UiEvent.RecyclerEvent.ImmediateScrollUp) recyclerView.scrollToPosition(0)
                }
            }
        }
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoItems.collectLatest {
                    adapter.setTodoItems(it)
                    if (it.isEmpty()) {
                        recyclerView.animateVisibility(visibility = Visibility.Invisible)
                        nothingFoundBanner.root.animateVisibility(visibility = Visibility.Visible)
                    } else {
                        recyclerView.animateVisibility(visibility = Visibility.Visible)
                        nothingFoundBanner.root.animateVisibility(visibility = Visibility.Invisible)
                    }
                }
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView.addItemDecoration(
            MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                this.dividerInsetStart = 56.toPx
                this.dividerThickness = 2.toPx
                this.isLastItemDecorated = false
            }
        )

        ItemTouchHelper(
            RecyclerViewSwipeCallback(
                context = context,
                onCompleteSwipe = { pos ->
                    adapter.todoItems[pos].let { viewModel.onUiAction(UiAction.AddTodoItem(it.copy(completed = !it.completed))) }
                },
                onDeleteSwipe = { pos ->
                    adapter.todoItems[pos].let { viewModel.onUiAction(UiAction.DeleteTodoItem(it)) }
                })
        ).attachToRecyclerView(recyclerView)

        recyclerView.itemAnimator!!.apply {
            changeDuration = 300L
            removeDuration = 300L
        }
    }
}