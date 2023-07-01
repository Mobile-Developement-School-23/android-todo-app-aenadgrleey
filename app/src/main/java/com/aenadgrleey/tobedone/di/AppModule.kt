package com.aenadgrleey.tobedone.di

import android.app.Application
import android.content.Context
import com.aenadgrleey.tobedone.data.datasources.DatabaseDatasourceImpl
import com.aenadgrleey.tobedone.data.datasources.NetworkDataSourceImpl
import com.aenadgrleey.tobedone.data.datasources.TodoItemsLocalDataSource
import com.aenadgrleey.tobedone.data.datasources.TodoItemsRemoteDataSource
import com.aenadgrleey.tobedone.data.models.TodoItemData
import com.aenadgrleey.tobedone.presentation.models.TodoItem
import com.aenadgrleey.tobedone.presentation.models.TodoItemDataToTodoItem
import com.aenadgrleey.tobedone.presentation.models.TodoItemToTodoItemData
import com.aenadgrleey.tobedone.utils.Mapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

    @Singleton
    @Provides
    fun provideLocalDataSource(
        @ApplicationContext context: Context
    ): TodoItemsLocalDataSource =
//        HardcodedDataSourceImpl(context)
        DatabaseDatasourceImpl.LocalDatabase.getDatabase(context).todoItemsDao()

    @Singleton
    @Provides
    fun provideRemoteDataSource(): TodoItemsRemoteDataSource =
        NetworkDataSourceImpl()
}