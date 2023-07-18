package com.aenadgrleey.list.ui.model

import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todo.domain.utils.Mapper
import java.util.Calendar

class TodoItemDataToTodoItem : Mapper<TodoItemData, TodoItem> {
    override fun map(input: TodoItemData) = with(input) {
        TodoItem(id, body, completed, importance, deadline, created, lastModified)
    }
}

class TodoItemToTodoItemData : Mapper<TodoItem, TodoItemData> {
    override fun map(input: TodoItem) = with(input) {
        if (id != null)
            TodoItemData(
                id = id,
                body = body ?: "",
                completed = completed ?: false,
                importance = importance ?: Importance.Common,
                deadline = deadline,
                created = created ?: Calendar.getInstance().time,
                lastModified = lastModified ?: Calendar.getInstance().time,
            )
        else
            TodoItemData(
                id = id,
                body = body ?: "",
                completed = completed ?: false,
                importance = importance ?: Importance.Common,
                deadline = deadline,
                created = created ?: Calendar.getInstance().time,
                lastModified = lastModified ?: Calendar.getInstance().time,
            )

    }
}