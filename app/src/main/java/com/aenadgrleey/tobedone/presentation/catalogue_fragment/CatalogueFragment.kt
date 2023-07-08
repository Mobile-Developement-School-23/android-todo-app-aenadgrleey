package com.aenadgrleey.tobedone.presentation.catalogue_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.aenadgrleey.tobedone.R
import com.aenadgrleey.tobedone.data.network.NetworkStatus
import com.aenadgrleey.tobedone.databinding.CatalogueFragmentBinding
import com.aenadgrleey.tobedone.presentation.SharedViewModel
import com.aenadgrleey.tobedone.presentation.catalogue_fragment.utils.TodoItemsRecyclerViewAdapter
import com.aenadgrleey.tobedone.presentation.catalogue_fragment.utils.TodoItemsSwipeCallback
import com.aenadgrleey.tobedone.presentation.models.TodoItem
import com.aenadgrleey.tobedone.presentation.refactor_fragment.RefactorFragment
import com.aenadgrleey.tobedone.utils.toPx
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogueFragment : Fragment() {
    private var binding: CatalogueFragmentBinding? = null
    private val viewModel by viewModels<SharedViewModel>()
    private var adapter: TodoItemsRecyclerViewAdapter? = null
    private var layoutManager: LinearLayoutManager? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animDuration = resources
            .getInteger(com.google.android.material.R.integer.m3_sys_motion_duration_long4).toLong()
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            .apply { duration = animDuration }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            .apply { duration = animDuration }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoItems.collectLatest { adapter!!.setTodoItems(it) }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.completedCount.collectLatest {
                    binding!!.toolbar.helperText.text = resources.getText(R.string.tasksCompleted).toString() + " " + it.toString()
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.networkStatus.collectLatest { networkStatus ->
                    binding!!.run {
                        swipeRefreshLayout.isRefreshing = false
                        if (networkStatus == NetworkStatus.NO_INTERNET) Snackbar.make(
                            coordinatorLayout,
                            resources.getString(R.string.noInternetError),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        if (networkStatus == NetworkStatus.SYNCED)
                            Snackbar.make(
                                coordinatorLayout,
                                resources.getString(R.string.synced),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        if (networkStatus == NetworkStatus.SERVER_INTERNAL_ERROR)
                            Snackbar.make(
                                coordinatorLayout,
                                resources.getString(R.string.serverError),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        if (networkStatus == NetworkStatus.SYNCING)
                            Snackbar.make(
                                coordinatorLayout,
                                "Syncing",
                                Snackbar.LENGTH_SHORT
                            ).show()
                    }
                }
            }
        }

        viewModel.fetchRemoteData()

        adapter = TodoItemsRecyclerViewAdapter(
            scrollUp = {
                binding!!.appBarLayout.setExpanded(true, true)
                layoutManager!!.scrollToPosition(0)
            },
            onTodoItemClick = { todoItem -> navigateToRefactorFragment(todoItem) },
            onLastItemClick = { navigateToRefactorFragment(null) },
            onCompleteButtonClick = { todoItem ->
                viewModel.addTodoItem(todoItem.copy(completed = !todoItem.completed!!))
            },
            onEditButtonClick = {
                navigateToRefactorFragment(it)
            },
            onDeleteButtonClick = {
                viewModel.deleteTodoItem(it)
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CatalogueFragmentBinding.inflate(layoutInflater)

        layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding!!.recyclerView.let { rv ->
            rv.adapter = this@CatalogueFragment.adapter

            rv.layoutManager = layoutManager

            rv.addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                ).apply {
                    this.dividerInsetStart = 56.toPx
                    this.dividerThickness = 2.toPx
                    this.isLastItemDecorated = false
                }
            )

            ItemTouchHelper(
                TodoItemsSwipeCallback(
                    context = requireContext(),
                    onCompleteSwipe = { pos ->
                        adapter!!.todoItems[pos].let { viewModel.addTodoItem(it.copy(completed = !it.completed!!)) }
                    },
                    onDeleteSwipe = { pos ->
                        adapter!!.todoItems[pos].let { viewModel.deleteTodoItem(it) }
                    })
            ).attachToRecyclerView(rv)

            rv.itemAnimator!!.apply {
                changeDuration = 300L
                removeDuration = 150L
            }
        }

        binding!!.toolbar.visibilityIcon.setOnClickListener {
            viewModel.toggleShowCompleted()
        }

        binding!!.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            binding!!.toolbar.motionLayout.progress = seekPosition
        }

        binding!!.fab.setOnClickListener {
            navigateToRefactorFragment(null)
        }

        binding!!.swipeRefreshLayout.run {
            setProgressViewOffset(false, (-48).toPx, 72.toPx)
            setOnRefreshListener {
                viewModel.fetchRemoteData()
            }
        }

        return binding!!.root
    }

    override fun onPause() {
        super.onPause()
        binding!!.swipeRefreshLayout.isRefreshing = false
    }


    private fun navigateToRefactorFragment(todoItem: TodoItem?) {
        parentFragmentManager.commit {
            val arguments = Bundle()
            arguments.putParcelable("todoItem", todoItem)
            val aim = RefactorFragment().also { it.arguments = arguments }
            replace(
                androidx.fragment.R.id.fragment_container_view_tag,
                aim,
                "tag"
            )
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        binding = null
        layoutManager = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "CatalogueFragment"
    }
}