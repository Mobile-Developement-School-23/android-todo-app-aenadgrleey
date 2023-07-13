package com.aenadgrleey.local.utils

import com.aenadgrleey.data.models.TodoItemData
import com.aenadgrleey.local.models.TodoItemDataDb

internal class TodoItemDataToTodoItemDataDbMapper {
    fun map(todoItemData: TodoItemData): TodoItemDataDb = with(todoItemData) {
        TodoItemDataDb(
            id,
            body!!,
            completed!!,
            importance!!,
            deadline,
            created!!,
            lastModified!!,
            color,
            lastModifiedBy!!,
        )
    }
}