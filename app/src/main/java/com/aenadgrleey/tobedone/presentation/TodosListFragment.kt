package com.aenadgrleey.tobedone.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.aenadgrleey.tobedone.applicationComponent
import com.aenadgrleey.tobedone.databinding.TodosListFragmentBinding
import com.aenadgrleey.tobedone.presentation.models.TodoItem
import com.aenadgrleey.tobedone.presentation.stateholders.SharedViewModel
import com.aenadgrleey.tobedone.utils.TodosListFragmentNavigator
import com.google.android.material.transition.platform.MaterialSharedAxis

class TodosListFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels {
        requireContext().applicationComponent.viewModelsFactory()
    }

    private var viewController: TodoListViewController? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchRemoteData()

        val animDuration = resources
            .getInteger(com.google.android.material.R.integer.m3_sys_motion_duration_long4).toLong()
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            .apply { duration = animDuration }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            .apply { duration = animDuration }


        val navigator = object : TodosListFragmentNavigator {
            override fun navigateToRefactorFragment(todoItem: TodoItem?) {
                this@TodosListFragment.navigateToRefactorFragment(todoItem)
            }
        }
        viewController = TodoListViewController(
            requireActivity(), null, navigator, this, viewModel
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = TodosListFragmentBinding.inflate(layoutInflater)

        viewController!!.binding = binding
        viewController!!.setUpViews()

        return binding.root
    }

    private fun navigateToRefactorFragment(todoItem: TodoItem?) {
        parentFragmentManager.commit {
            val arguments = Bundle()
            arguments.putParcelable("todoItem", todoItem)
            val aim = TodoRefactorFragment().also { it.arguments = arguments }
            replace(androidx.fragment.R.id.fragment_container_view_tag, aim, "tag")
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewController!!.binding = null
    }


    companion object {
        const val TAG = "CatalogueFragment"
    }
}