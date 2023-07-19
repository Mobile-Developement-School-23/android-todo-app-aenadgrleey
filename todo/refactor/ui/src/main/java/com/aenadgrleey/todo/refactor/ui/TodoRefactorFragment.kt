package com.aenadgrleey.todo.refactor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aenadgrleey.todo.refactor.domain.TodoItemId
import com.aenadgrleey.todo.refactor.domain.TodoRefactorNavigator
import com.aenadgrleey.todo.refactor.ui.composables.RefactorScreen
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiComponentProvider
import com.aenadgrleey.todo.refactor.ui.model.UiAction
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.transition.platform.MaterialSharedAxis
import javax.inject.Inject

class TodoRefactorFragment : Fragment() {

    private val todoRefactorUiComponent by lazy {
        (requireActivity() as TodoRefactorUiComponentProvider).provideTodoRefactorUiComponent()
    }

    private val viewModel: TodoRefactorViewModel by activityViewModels {
        todoRefactorUiComponent.viewModelFactory()
    }

    @Inject
    lateinit var navigator: TodoRefactorNavigator

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
        val id = requireArguments().getString(TodoItemId.TAG)
        viewModel.onUiAction(UiAction.InitTodoItem(id))
        todoRefactorUiComponent.inject(this)
        return ComposeView(requireContext()).apply {
            setContent {
                Mdc3Theme {
                    RefactorScreen(
                        lifecycleOwner = viewLifecycleOwner,
                        uiEvents = viewModel.uiEvents,
                        onUiAction = viewModel::onUiAction,
                        uiStateFlow = viewModel.uiState,
                        navigator = navigator
                    )
                }
            }
        }
    }
}