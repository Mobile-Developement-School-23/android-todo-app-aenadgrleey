package com.aenadgrleey.tobedone.presentation.models

import com.aenadgrleey.tobedone.data.models.TodoItemData
import com.aenadgrleey.tobedone.utils.Importance
import com.aenadgrleey.tobedone.utils.Mapper
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
                body = body ?: "",
                completed = completed ?: false,
                importance = importance ?: Importance.Common,
                deadline = deadline,
                created = created ?: Calendar.getInstance().time,
                lastModified = lastModified ?: Calendar.getInstance().time,
            )

    }
}