package com.aenadgrleey.tobedone.presentation.models

import com.aenadgrleey.tobedone.utils.Mapper
import java.util.Calendar

class TodoItemDataToTodoItem : Mapper<com.aenadgrleey.data.models.TodoItemData, TodoItem> {
    override fun map(input: com.aenadgrleey.data.models.TodoItemData) = with(input) {
        TodoItem(id, body, completed, importance, deadline, created, lastModified)
    }
}

class TodoItemToTodoItemData : Mapper<TodoItem, com.aenadgrleey.data.models.TodoItemData> {
    override fun map(input: TodoItem) = with(input) {
        if (id != null)
            com.aenadgrleey.data.models.TodoItemData(
                id = id,
                body = body ?: "",
                completed = completed ?: false,
                importance = importance ?: com.aenadgrleey.data.utils.Importance.Common,
                deadline = deadline,
                created = created ?: Calendar.getInstance().time,
                lastModified = lastModified ?: Calendar.getInstance().time,
            )
        else
            com.aenadgrleey.data.models.TodoItemData(
                body = body ?: "",
                completed = completed ?: false,
                importance = importance ?: com.aenadgrleey.data.utils.Importance.Common,
                deadline = deadline,
                created = created ?: Calendar.getInstance().time,
                lastModified = lastModified ?: Calendar.getInstance().time,
            )

    }
}