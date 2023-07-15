package com.aenadgrleey.todorefactor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aenadgrleey.di.holder.scopedComponent
import com.aenadgrleey.todorefactor.ui.composables.RefactorScreen
import com.aenadgrleey.todorefactor.ui.di.TodoRefactorUiComponentProvider
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.transition.platform.MaterialSharedAxis

class TodoRefactorFragment : Fragment() {

    private val todoRefactorUiComponent by scopedComponent {
        (requireActivity() as TodoRefactorUiComponentProvider).provideTodoRefactorUiComponent()
    }

    private val viewModel: TodoRefactorViewModel by activityViewModels {
        todoRefactorUiComponent.viewModelFactory()
    }

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
    ) = ComposeView(requireContext()).apply {
        setContent {
            Mdc3Theme {
                RefactorScreen(lifecycle = viewLifecycleOwner.lifecycle, viewModel = viewModel)
            }
        }
    }
}