package com.aenadgrleey.todolist.ui.di.view_component

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aenadgrleey.di.FragmentContext
import com.aenadgrleey.di.ViewLifecycleOwner
import com.aenadgrleey.di.ViewScope
import com.aenadgrleey.todolist.ui.TodoListViewModel
import com.aenadgrleey.todolist.ui.databinding.ExpendableToolbarBinding
import com.aenadgrleey.todolist.ui.ioc.TodoListBootstrapper
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.BindsInstance
import dagger.Subcomponent

@ViewScope
@Subcomponent
interface TodoListViewComponent {
    fun boot(): TodoListBootstrapper

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance @FragmentContext fragmentContext: Context,
            @BindsInstance viewModel: TodoListViewModel,
            @BindsInstance coordinatorLayout: CoordinatorLayout,
            @BindsInstance appBarLayout: AppBarLayout,
            @BindsInstance toolbarBinding: ExpendableToolbarBinding,
            @BindsInstance swipeRefreshLayout: SwipeRefreshLayout,
            @BindsInstance recyclerView: RecyclerView,
            @BindsInstance fab: FloatingActionButton,
            @BindsInstance @ViewLifecycleOwner lifecycleOwner: LifecycleOwner,

            ): TodoListViewComponent
    }
}