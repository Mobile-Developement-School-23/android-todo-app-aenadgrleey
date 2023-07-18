package com.aenadgrleey.todonotify.ui.di

import com.aenadgrleey.todonotify.ui.trackers.TodoSessionTrackerImpl
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [TodoNotifySubcomponent::class])
abstract class TodoNotifyModule {
    @Binds
    @IntoMap
    @ClassKey(TodoSessionTrackerImpl::class)
    abstract fun bindYourFragmentInjectorFactory(factory: TodoNotifySubcomponent.Factory): AndroidInjector.Factory<*>
}

@Subcomponent
interface TodoNotifySubcomponent : AndroidInjector<TodoSessionTrackerImpl> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<TodoSessionTrackerImpl>
}
