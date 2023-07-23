package com.aenadgrleey.list.ui.model

import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todo.domain.utils.Mapper

class TodoItemDataToTodoItem : Mapper<TodoItemData, TodoItem> {
    override fun map(input: TodoItemData) = with(input) {
        TodoItem(id!!, body, completed, importance, deadline, created!!, lastModified!!, lastModifiedBy!!)
    }
}

class TodoItemToTodoItemData : Mapper<TodoItem, TodoItemData> {
    override fun map(input: TodoItem) = with(input) {
        TodoItemData(
            id = id,
            body = body,
            completed = completed,
            importance = importance,
            deadline = deadline,
            created = created,
            lastModified = lastModified,
            lastModifiedBy = lastModifiedBy
        )

    }
}