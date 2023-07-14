package com.aenadgrleey.tododata.local.utils

import com.aenadgrleey.core.domain.models.TodoItemData
import com.aenadgrleey.tododata.local.models.TodoItemDataDb

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