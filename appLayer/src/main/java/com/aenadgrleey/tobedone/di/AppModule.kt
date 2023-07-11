package com.aenadgrleey.tobedone.di

import android.app.Application
import android.content.Context
import com.aenadgrleey.tobedone.data.models.TodoItemData
import com.aenadgrleey.tobedone.data.network.Tokens
import com.aenadgrleey.tobedone.presentation.models.TodoItem
import com.aenadgrleey.tobedone.presentation.models.TodoItemDataToTodoItem
import com.aenadgrleey.tobedone.presentation.models.TodoItemToTodoItemData
import com.aenadgrleey.tobedone.utils.Mapper
import com.aenadgrleey.tobedone.utils.SharedPreferencesTags
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Application) = app

    @Singleton
    @Provides
    fun provideDataViewMapper(): Mapper<TodoItemData, TodoItem> = TodoItemDataToTodoItem()

    @Singleton
    @Provides
    fun provideViewDataMapper(): Mapper<TodoItem, TodoItemData> = TodoItemToTodoItemData()

    @Named("authHeader")
    @Provides
    fun providesUserToken(@ApplicationContext context: Context): String {
        context.getSharedPreferences(SharedPreferencesTags.Tag, Context.MODE_PRIVATE).getString(SharedPreferencesTags.Token, null).let {
            if (it == "dummyEmptyToken") {
                return "Bearer ${Tokens.token}"
            } else
                return "OAuth $it"
        }
    }

}