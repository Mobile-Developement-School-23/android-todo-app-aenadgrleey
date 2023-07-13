package com.aenadgrleey.tobedone.di.modules

import android.content.Context
import com.aenadgrleey.data.models.TodoItemData
import com.aenadgrleey.data.remote.AuthToken
import com.aenadgrleey.datasources.di.DataModule
import com.aenadgrleey.di.AppContext
import com.aenadgrleey.repositories.TodoItemRepository
import com.aenadgrleey.repositories.TodoItemsRepositoryImpl
import com.aenadgrleey.tobedone.presentation.models.TodoItem
import com.aenadgrleey.tobedone.presentation.models.TodoItemDataToTodoItem
import com.aenadgrleey.tobedone.presentation.models.TodoItemToTodoItemData
import com.aenadgrleey.tobedone.utils.Mapper
import com.aenadgrleey.tobedone.utils.SharedPreferencesTags
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [DataModule::class, WorkModule::class])
abstract class AppModule {

    @Binds
    abstract fun bindRepository(repository: TodoItemsRepositoryImpl): TodoItemRepository

    companion object {
        @Provides
        fun provideDataViewMapper(): Mapper<TodoItemData, TodoItem> = TodoItemDataToTodoItem()

        @Provides
        fun provideViewDataMapper(): Mapper<TodoItem, TodoItemData> = TodoItemToTodoItemData()

        @AuthToken
        @Provides
        fun providesUserToken(@AppContext context: Context): String {
            context.getSharedPreferences(SharedPreferencesTags.Tag, Context.MODE_PRIVATE)
                .getString(SharedPreferencesTags.Token, null).let {
                    if (it == "dummyEmptyToken" || it == null) return "durry"
                    else return it
                }

        }
    }
}