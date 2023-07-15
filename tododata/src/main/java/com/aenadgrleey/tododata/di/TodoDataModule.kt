package com.aenadgrleey.tododata.di

import com.aenadgrleey.di.AppScope
import com.aenadgrleey.local.TodoItemsLocalDataSource
import com.aenadgrleey.remote.TodoItemsRemoteDataSource
import com.aenadgrleey.tododata.local.DatabaseDataSourceImpl
import com.aenadgrleey.tododata.remote.NetworkDataSourceImpl
import com.aenadgrleey.tododata.repository.TodoItemsRepositoryImpl
import com.aenadgrleey.tododomain.repository.TodoItemRepository
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