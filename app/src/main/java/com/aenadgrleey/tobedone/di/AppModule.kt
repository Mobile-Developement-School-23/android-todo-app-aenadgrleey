package com.aenadgrleey.tobedone.di

import android.app.Application
import com.aenadgrleey.tobedone.data.TodoItemData
import com.aenadgrleey.tobedone.data.TodoItemsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Application) = app

    @Singleton
    @Provides
    fun provideDataSource(
//        @ApplicationContext app: Application
    ): TodoItemsDataSource {
        return object : TodoItemsDataSource {
            var onUpdate: suspend () -> Unit = {}
            val items = mutableListOf(
                TodoItemData(
                    id = "abad",
                    body = "adad"
                )
            )

            override suspend fun addTodoItem(item: TodoItemData) {
                items.add(item)
                onUpdate()
            }

            override fun getTodoItems(): Flow<List<TodoItemData>> {
                return flow {
                    onUpdate = { emit(items) }
                    while (true) {
                        onUpdate()
                        delay(1000L)
                    }
                }
            }

            override suspend fun deleteTodoItem(item: TodoItemData) {
                items.remove(item)
                onUpdate()
            }
        }
    }
}