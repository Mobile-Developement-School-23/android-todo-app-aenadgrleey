package com.aenadgrleey.todo.data.di

import com.aenadgrleey.core.di.AppScope
import com.aenadgrleey.todo.data.local.DatabaseDataSourceImpl
import com.aenadgrleey.todo.data.remote.NetworkDataSourceImpl
import com.aenadgrleey.todo.data.repository.TodoItemsRepositoryImpl
import com.aenadgrleey.todo.domain.local.TodoItemsLocalDataSource
import com.aenadgrleey.todo.domain.remote.TodoItemsRemoteDataSource
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import dagger.Binds
import dagger.Module

@Module
abstract class TodoDataModule {
    @Binds
    @AppScope
    abstract fun bindRepository(repository: TodoItemsRepositoryImpl): TodoItemRepository

    @Binds
    @AppScope
    abstract fun bindLocalDataSource(localDataSource: DatabaseDataSourceImpl): TodoItemsLocalDataSource

    @Binds
    @AppScope
    abstract fun bindRemoteDatasource(remoteDataSource: NetworkDataSourceImpl): TodoItemsRemoteDataSource
}