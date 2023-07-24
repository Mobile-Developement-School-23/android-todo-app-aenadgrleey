package com.aenadgrleey.tobedone

import android.content.Context
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.R
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aenadgrleey.auth.ui.di.AuthViewComponent
import com.aenadgrleey.auth.ui.di.AuthViewComponentProvider
import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.di.TodoListViewComponent
import com.aenadgrleey.list.ui.di.TodoListViewComponentProvider
import com.aenadgrleey.settings.ui.di.SettingsViewComponent
import com.aenadgrleey.settings.ui.di.SettingsViewComponentProvider
import com.aenadgrleey.tobedone.di.view_component.TodoActivityViewComponent
import com.aenadgrleey.todo.list.ui.databinding.ExpendableToolbarBinding
import com.aenadgrleey.todo.list.ui.databinding.NothingFoundBannerBinding
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorViewComponent
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorViewComponentProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TodoActivity : AppCompatActivity(),
    AuthViewComponentProvider,
    SettingsViewComponentProvider,
    TodoListViewComponentProvider,
    TodoRefactorViewComponentProvider {

    private lateinit var activityComponent: TodoActivityViewComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FragmentContainerView(this).apply { id = R.id.fragment_container_view_tag })
        activityComponent = applicationComponent.todoActivityComponent().create(
            permissionGrantLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {},
            activity = this,
            fragmentManager = supportFragmentManager,
            lifecycleOwner = this
        )
        activityComponent.boot()
    }

    override fun provideAuthViewComponent(): AuthViewComponent =
        activityComponent.authViewComponent().create()

    override fun provideSettingsViewComponent(): SettingsViewComponent =
        activityComponent.settingsViewComponent().create()

    override fun provideTodoListViewComponent(
        fragmentContext: Context,
        viewModel: TodoListViewModel,
        coordinatorLayout: CoordinatorLayout,
        appBarLayout: AppBarLayout,
        toolbarBinding: ExpendableToolbarBinding,
        swipeRefreshLayout: SwipeRefreshLayout,
        nothingFoundBanner: NothingFoundBannerBinding,
        recyclerView: RecyclerView,
        fab: FloatingActionButton,
        lifecycleOwner: LifecycleOwner,
    ): TodoListViewComponent =
        activityComponent.todoListViewComponent().create(
            fragmentContext,
            viewModel,
            coordinatorLayout,
            appBarLayout,
            toolbarBinding,
            swipeRefreshLayout,
            nothingFoundBanner,
            recyclerView,
            fab,
            lifecycleOwner
        )

    override fun todoRefactorViewComponentProvider(): TodoRefactorViewComponent =
        activityComponent.todoRefactorViewComponent().create()


}