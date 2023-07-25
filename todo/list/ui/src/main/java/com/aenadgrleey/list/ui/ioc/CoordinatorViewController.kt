package com.aenadgrleey.list.ui.ioc

import android.content.Context
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import com.aenadgrleey.core.di.FragmentContext
import com.aenadgrleey.core.di.ViewLifecycleOwner
import com.aenadgrleey.core.di.ViewScope
import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.model.UiAction
import com.aenadgrleey.list.ui.model.UiEvent
import com.aenadgrleey.resources.R
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

@ViewScope
class CoordinatorViewController @Inject constructor(
    @FragmentContext
    private val context: Context,
    private val coordinatorLayout: CoordinatorLayout,
    private val viewModel: TodoListViewModel,
    @ViewLifecycleOwner
    private val lifecycleOwner: LifecycleOwner,
) {

    suspend fun onUiEvent(uiEvent: UiEvent) {
        val snackbarMessage: String? = when (uiEvent) {
            UiEvent.ConnectionError -> context.resources.getString(R.string.noInternetError)
            UiEvent.SyncedWithServer -> context.resources.getString(R.string.synced)
            UiEvent.BadServerResponse -> context.getString(R.string.serverError)
            is UiEvent.ShowDeletedItem -> context.getString(R.string.deletedItem) + uiEvent.todoItem.body
            UiEvent.ProblemsWithAuth -> context.getString(R.string.problemAuth)
            else -> null
        }
        snackbarMessage?.let {
            val snackbar = Snackbar.make(coordinatorLayout, it, Snackbar.LENGTH_LONG)
            snackbar.setTextMaxLines(1)
            if (uiEvent is UiEvent.ShowDeletedItem) {
                snackbar.setAction(context.getString(R.string.cancel)) { view ->
                    view.performHapticFeedback(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
                        else HapticFeedbackConstants.CONTEXT_CLICK
                    )
                    viewModel.onUiAction(UiAction.UndoDeleteTodoItem(uiEvent.todoItem))
                }
            }
            snackbar.show()
        }
    }
}