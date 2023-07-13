package com.aenadgrleey.tobedone.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aenadgrleey.tobedone.applicationComponent
import com.aenadgrleey.tobedone.databinding.TodoRefactorFragmentBinding
import com.aenadgrleey.tobedone.presentation.models.TodoItem
import com.aenadgrleey.tobedone.presentation.stateholders.SharedViewModel
import com.aenadgrleey.tobedone.utils.TodoRefactorFragmentNavigator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.platform.MaterialSharedAxis

class TodoRefactorFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels {
        requireContext().applicationComponent.viewModelsFactory()
    }

    private var viewController: TodoRefactorViewController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animDuration = resources
            .getInteger(com.google.android.material.R.integer.m3_sys_motion_duration_long4).toLong()
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            .apply { duration = animDuration }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            .apply { duration = animDuration }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = TodoRefactorFragmentBinding.inflate(layoutInflater)

        @Suppress("DEPRECATION")
        val refactoredItem =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                requireArguments().getParcelable("todoItem", TodoItem::class.java)
            else
                requireArguments().getParcelable("todoItem")

        val navigator = object : TodoRefactorFragmentNavigator {
            override fun toCalendar(onSuccess: (Long) -> Unit) {
                MaterialDatePicker.Builder.datePicker().build()
                    .apply { addOnPositiveButtonClickListener(onSuccess) }
                    .show(parentFragmentManager, "DATE_PICKER")
            }

            override fun navigateUp() {
                parentFragmentManager.popBackStack()
            }
        }
        viewController = TodoRefactorViewController(
            requireActivity(),
            binding, refactoredItem, navigator,
            this, viewModel
        ).apply { setUpViews() }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchRemoteData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewController = null
    }


}