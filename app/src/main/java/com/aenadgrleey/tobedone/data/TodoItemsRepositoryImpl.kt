package com.aenadgrleey.tobedone.data

import java.util.Calendar
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(private val dataSource: TodoItemsDataSource) :
    TodoItemRepository {

    override fun todoItems() =
        dataSource.getTodoItems()

    override suspend fun addTodoItem(todoItem: TodoItemData) {
        todoItem.lastModified = Calendar.getInstance().time
        dataSource.addTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItemData) =
        dataSource.deleteTodoItem(todoItem)
}