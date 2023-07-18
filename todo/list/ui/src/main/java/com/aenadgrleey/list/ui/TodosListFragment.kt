package com.aenadgrleey.list.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aenadgrleey.list.ui.di.TodoListUiComponent
import com.aenadgrleey.list.ui.di.TodoListUiComponentProvider
import com.aenadgrleey.list.ui.di.view_component.TodoListViewComponent
import com.aenadgrleey.todo.list.ui.databinding.TodosListFragmentBinding
import com.google.android.material.transition.platform.MaterialSharedAxis

class TodosListFragment : Fragment() {

    private val todoListUiComponent: TodoListUiComponent by lazy {
        (requireActivity() as TodoListUiComponentProvider).provideTodoListUiComponent()
    }

    private val viewModel: TodoListViewModel by activityViewModels {
        todoListUiComponent.viewModelFactory()
    }

    private var viewComponent: TodoListViewComponent? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animDuration = resources
            .getInteger(com.google.android.material.R.integer.m3_sys_motion_duration_long4).toLong()
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            .apply { duration = animDuration }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            .apply { duration = animDuration }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            .apply { duration = animDuration }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = TodosListFragmentBinding.inflate(layoutInflater)

        viewComponent = todoListUiComponent.todoListViewComponent().create(
            fragmentContext = requireContext(),
            viewModel = viewModel,
            coordinatorLayout = binding.coordinatorLayout,
            appBarLayout = binding.appBarLayout,
            toolbarBinding = binding.toolbar,
            swipeRefreshLayout = binding.swipeRefreshLayout,
            recyclerView = binding.recyclerView,
            fab = binding.fab,
            lifecycleOwner = viewLifecycleOwner
        )
        viewComponent!!.boot()

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewComponent = null
    }


    companion object {
        const val TAG = "CatalogueFragment"
    }
}