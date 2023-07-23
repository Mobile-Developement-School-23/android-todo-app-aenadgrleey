package com.aenadgrleey.todo.data.remote.utils

import com.aenadgrleey.todo.data.remote.models.TodoItemDataNetwork
import com.aenadgrleey.todo.domain.models.TodoItemData

class TodoItemDataToTodoItemNetworkMapper {
    fun map(todoItemData: TodoItemData): TodoItemDataNetwork = with(todoItemData) {
        TodoItemDataNetwork(
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