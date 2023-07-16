package com.aenadgrleey.tobedone.di

import android.content.Context
import com.aenadgrleey.auth.data.di.AuthProviderScope
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.core.di.AppScope
import com.aenadgrleey.tobedone.ToBeDone
import com.aenadgrleey.tobedone.di.view_component.TodoActivityComponent
import dagger.BindsInstance
import dagger.Component

@AppScope
@AuthProviderScope
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    fun injectIntoApplication(application: ToBeDone)

    fun todoActivityComponent(): TodoActivityComponent.Factory


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@AppContext context: Context): Builder

        fun build(): ApplicationComponent
    }

}