package com.aenadgrleey.tobedone.data.datasources

import android.content.Context
import com.aenadgrleey.tobedone.data.db.LocalDatabase
import com.aenadgrleey.tobedone.data.models.TodoItemData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
) : TodoItemsLocalDataSource {

    private val dao = LocalDatabase.getDatabase(context).todoItemsDao()
    override fun getTodoItems(excludeCompleted: Boolean): Flow<List<TodoItemData>> = dao.getTodoItems(excludeCompleted)

    override fun getTodoItems(): List<TodoItemData> = dao.getTodoItems()

    override fun clearDatabase() {
        dao.clearDatabase()
    }

    override fun completedItemsCount(): Flow<Int> = dao.completedItemsCount()

    override suspend fun addTodoItem(todoItem: TodoItemData) {
        dao.addTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItemData) {
        dao.deleteTodoItem(todoItem)
    }

}