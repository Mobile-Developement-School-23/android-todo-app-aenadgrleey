package com.aenadgrleey.todolist.ui.ioc

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
import com.aenadgrleey.todolist.ui.TodoListViewModel
import com.aenadgrleey.todolist.ui.model.UiAction
import com.aenadgrleey.todolist.ui.model.UiEvent
import com.aenadgrleey.todolist.ui.utils.TodoItemsRecyclerViewAdapter
import com.aenadgrleey.todolist.ui.utils.TodoItemsSwipeCallback
import com.aenadgrleey.todolist.ui.utils.toPx
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewScope
class TodosRecyclerViewController @Inject constructor(
    @FragmentContext
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val adapter: TodoItemsRecyclerViewAdapter,
    @ViewLifecycleOwner
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: TodoListViewModel,
) {
    fun setUpRecycler() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recyclerEvents.collect {
                    if (it == UiEvent.RecyclerEvent.ScrollUp)
                        recyclerView.layoutManager?.scrollToPosition(0)
                }
            }
        }
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoItems.collectLatest { adapter.setTodoItems(it) }
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
            TodoItemsSwipeCallback(
                context = context,
                onCompleteSwipe = { pos ->
                    adapter.todoItems[pos].let { viewModel.onUiAction(UiAction.AddTodoItem(it.copy(completed = !it.completed!!))) }
                },
                onDeleteSwipe = { pos ->
                    adapter.todoItems[pos].let { viewModel.onUiAction(UiAction.DeleteTodoItem(it)) }
                })
        ).attachToRecyclerView(recyclerView)

        recyclerView.itemAnimator!!.apply {
            changeDuration = 300L
            removeDuration = 150L
        }
    }
}