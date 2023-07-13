package com.aenadgrleey.tobedone.di

import android.content.Context
import com.aenadgrleey.datasources.di.DataModule
import com.aenadgrleey.di.AppContext
import com.aenadgrleey.tobedone.ToBeDone
import com.aenadgrleey.tobedone.di.modules.AppModule
import com.aenadgrleey.tobedone.di.modules.WorkModule
import com.aenadgrleey.tobedone.presentation.TodoItemsActivity
import com.aenadgrleey.tobedone.presentation.stateholders.SharedViewModel
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    fun injectIntoApplication(application: ToBeDone)

    fun injectIntoActivity(activity: TodoItemsActivity)

    fun viewModelsFactory(): SharedViewModel.ViewModelFactory


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@AppContext context: Context): Builder

        fun build(): ApplicationComponent
    }

}