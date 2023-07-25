package com.aenadgrleey.todo.data.local.utils

import com.aenadgrleey.todo.data.local.models.TodoItemDataDb
import com.aenadgrleey.todo.domain.models.TodoItemData

internal class TodoItemDataToTodoItemDataDbMapper {
    fun map(todoItemData: TodoItemData): TodoItemDataDb = with(todoItemData) {
        TodoItemDataDb(
            id!!,
            body,
            completed,
            importance,
            deadline,
            created!!,
            lastModified!!,
            color,
            lastModifiedBy!!,
        )
    }
}