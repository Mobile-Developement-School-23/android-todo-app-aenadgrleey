package com.aenadgrleey.tobedone.di

import android.app.Application
import android.content.Context
import com.aenadgrleey.tobedone.R
import com.aenadgrleey.tobedone.data.TodoItemData
import com.aenadgrleey.tobedone.data.TodoItemsDataSource
import com.aenadgrleey.tobedone.presentation.TodoItem
import com.aenadgrleey.tobedone.presentation.TodoItemDataToTodoItem
import com.aenadgrleey.tobedone.presentation.TodoItemToTodoItemData
import com.aenadgrleey.tobedone.utils.Importance
import com.aenadgrleey.tobedone.utils.Mapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.util.Calendar
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
    fun provideDataSource(
        @ApplicationContext context: Context
    ): TodoItemsDataSource {
        return object : TodoItemsDataSource {
            var updateItemFlow: suspend () -> Unit = {}
            var updateCountFlow: suspend () -> Unit = {}

            var counter = 0
            val items = mutableListOf<TodoItemData>().apply {
                val texts = listOf(
                    context.getText(R.string.buy_something),
                    context.getString(R.string.lorem_ipsum)
                )
                val completed = listOf(true, false)
                val importance = listOf(Importance.Common, Importance.High, Importance.Low)
                val deadline = listOf(Calendar.getInstance().time, null)
                for (importanceType in 0..2) {
                    for (textType in 0..1) {
                        for (completenessType in 0..1) {
                            for (deadlineExistence in 0..1) {
                                this.add(
                                    TodoItemData(
                                        id = counter.toString(),
                                        body = texts[textType].toString(),
                                        completed = completed[completenessType],
                                        importance = importance[importanceType],
                                        deadline = deadline[deadlineExistence],
                                    )
                                )
                                counter += 1
                            }
                        }
                    }
                }
            }

            override suspend fun addTodoItem(todoItem: TodoItemData) {
                var insertFlag = true
                items.forEachIndexed { index, item ->
                    if (item.id == todoItem.id) {
                        items[index] = todoItem
                        insertFlag = false
                    }
                }
                if (insertFlag) {
                    todoItem.id = counter.toString()
                    items.add(todoItem)
                }

                updateItemFlow()
                updateCountFlow()
            }

            override fun getTodoItems(includeCompleted: Boolean): Flow<List<TodoItemData>> {
                return channelFlow {
                    updateItemFlow = {
                        send(items.filter {
                            (!(includeCompleted || it.completed)) || includeCompleted
                        })
                    }
                    updateItemFlow()
                    awaitClose()
                }
            }

            override fun completedItemsCount(): Flow<Int> = channelFlow {
                updateCountFlow = {
                    send(items.filter { it.completed }.size)
                }
                updateCountFlow()
                awaitClose()
            }

            override suspend fun deleteTodoItem(todoItem: TodoItemData) {
                for (index in 0..items.lastIndex) {
                    if (items[index].id == todoItem.id) {
                        items.removeAt(index)
                        break
                    }
                }
                updateItemFlow()
                updateCountFlow()
            }
        }
    }
}