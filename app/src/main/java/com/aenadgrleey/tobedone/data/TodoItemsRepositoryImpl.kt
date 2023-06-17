package com.aenadgrleey.tobedone.data

import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(private val dataSource: TodoItemsDataSource) :
    TodoItemRepository {

    override fun todoItems(includeCompleted: Boolean): Flow<List<TodoItemData>> =
        dataSource.getTodoItems(includeCompleted)

    override fun completedItemsCount(): Flow<Int> =
        dataSource.completedItemsCount()

    override suspend fun addTodoItem(todoItem: TodoItemData) {
        todoItem.lastModified = Calendar.getInstance().time
        dataSource.addTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItemData) =
        dataSource.deleteTodoItem(todoItem)
}