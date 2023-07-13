package com.aenadgrleey.datasources.di

import com.aenadgrleey.local.DatabaseDataSourceImpl
import com.aenadgrleey.local.TodoItemsLocalDataSource
import com.aenadgrleey.remote.NetworkDataSourceImpl
import com.aenadgrleey.remote.TodoItemsRemoteDataSource
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun bindLocalDataSource(localDataSource: DatabaseDataSourceImpl): TodoItemsLocalDataSource

    @Binds
    abstract fun bindRemoteDatasource(remoteDataSource: NetworkDataSourceImpl): TodoItemsRemoteDataSource
}