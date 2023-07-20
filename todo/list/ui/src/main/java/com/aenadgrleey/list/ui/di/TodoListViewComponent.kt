package com.aenadgrleey.list.ui.di

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aenadgrleey.core.di.FragmentContext
import com.aenadgrleey.core.di.ViewLifecycleOwner
import com.aenadgrleey.core.di.ViewScope
import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.ioc.TodoListBootstrapper
import com.aenadgrleey.todo.list.ui.databinding.ExpendableToolbarBinding
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

interface TodoListViewComponentProvider {
    fun provideTodoListViewComponent(
        fragmentContext: Context,
        viewModel: TodoListViewModel,
        coordinatorLayout: CoordinatorLayout,
        appBarLayout: AppBarLayout,
        toolbarBinding: ExpendableToolbarBinding,
        swipeRefreshLayout: SwipeRefreshLayout,
        recyclerView: RecyclerView,
        fab: FloatingActionButton,
        lifecycleOwner: LifecycleOwner,
    ): TodoListViewComponent
}