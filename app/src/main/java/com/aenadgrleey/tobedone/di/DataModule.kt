package com.aenadgrleey.tobedone.di

import com.aenadgrleey.tobedone.data.repositories.TodoItemRepository
import com.aenadgrleey.tobedone.data.repositories.TodoItemsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repository: TodoItemsRepositoryImpl): TodoItemRepository

}