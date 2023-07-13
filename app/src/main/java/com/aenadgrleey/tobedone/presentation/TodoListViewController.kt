package com.aenadgrleey.tobedone.presentation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.aenadgrleey.data.remote.NetworkStatus
import com.aenadgrleey.resources.R.*
import com.aenadgrleey.tobedone.databinding.TodosListFragmentBinding
import com.aenadgrleey.tobedone.presentation.stateholders.SharedViewModel
import com.aenadgrleey.tobedone.presentation.utils.TodoItemsRecyclerViewAdapter
import com.aenadgrleey.tobedone.presentation.utils.TodoItemsSwipeCallback
import com.aenadgrleey.tobedone.utils.TodosListFragmentNavigator
import com.aenadgrleey.tobedone.utils.toPx
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TodoListViewController(
    private val activity: Activity,
    var binding: TodosListFragmentBinding?,
    private val navigator: TodosListFragmentNavigator,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: SharedViewModel,
) {
    private val adapter = createAdapter()

    init {
        collectFlows()
    }

    fun setUpViews() {
        setUpTodosRecyclerView()
        setUpToolbar()
        setUpFab()
    }

    private fun createAdapter(): TodoItemsRecyclerViewAdapter {
        return TodoItemsRecyclerViewAdapter(
            scrollUp = {
                binding!!.appBarLayout.setExpanded(true, true)
                LinearLayoutManager(activity).scrollToPosition(0)
            },
            onTodoItemClick = { navigator.navigateToRefactorFragment(it) },
            onLastItemClick = { navigator.navigateToRefactorFragment(null) },
            onCompleteButtonClick = { viewModel.addTodoItem(it.copy(completed = !it.completed!!)) },
            onEditButtonClick = { navigator.navigateToRefactorFragment(it) },
            onDeleteButtonClick = { viewModel.deleteTodoItem(it) }
        )
    }

    private fun collectFlows() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoItems.collectLatest {
                    println(it)
                    adapter.setTodoItems(it)
                }
            }
        }
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.completedCount.collectLatest {
                    binding!!.toolbar.helperText.text =
                        activity.resources.getText(string.tasksCompleted).toString() + " " + it.toString()
                }
            }
        }
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.networkStatus.collectLatest { networkStatus ->
                    binding!!.run {
                        swipeRefreshLayout.isRefreshing = false
                        when (networkStatus) {
                            NetworkStatus.NO_INTERNET -> activity.resources.getString(string.noInternetError)
                            NetworkStatus.SYNCED -> activity.resources.getString(string.synced)
                            NetworkStatus.SERVER_INTERNAL_ERROR -> activity.resources.getString(string.serverError)
                            else -> null
                        }?.let { Snackbar.make(coordinatorLayout, it, Snackbar.LENGTH_SHORT).show() }

                    }
                }
            }
        }
    }

    private fun setUpTodosRecyclerView() {
        binding!!.recyclerView.let { rv ->
            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            rv.addItemDecoration(
                MaterialDividerItemDecoration(activity, LinearLayoutManager.VERTICAL).apply {
                    this.dividerInsetStart = 56.toPx
                    this.dividerThickness = 2.toPx
                    this.isLastItemDecorated = false
                }
            )

            ItemTouchHelper(
                TodoItemsSwipeCallback(
                    context = activity,
                    onCompleteSwipe = { pos ->
                        adapter.todoItems[pos].let { viewModel.addTodoItem(it.copy(completed = !it.completed!!)) }
                    },
                    onDeleteSwipe = { pos -> adapter.todoItems[pos].let { viewModel.deleteTodoItem(it) } })
            ).attachToRecyclerView(rv)

            rv.itemAnimator!!.apply {
                changeDuration = 300L
                removeDuration = 150L
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpToolbar() {
        binding!!.swipeRefreshLayout.run {
            setProgressViewOffset(false, (-48).toPx, 72.toPx)
            setOnRefreshListener { viewModel.fetchRemoteData() }
        }

        binding!!.toolbar.visibilityIcon.setOnClickListener { viewModel.toggleShowCompleted() }

        binding!!.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            binding!!.toolbar.motionLayout.progress = seekPosition
        }
    }

    private fun setUpFab() {
        binding!!.fab.setOnClickListener { navigator.navigateToRefactorFragment(null) }
    }

}