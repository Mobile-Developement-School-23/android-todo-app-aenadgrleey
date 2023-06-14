package com.aenadgrleey.tobedone.presentation

import com.aenadgrleey.tobedone.data.TodoItemData
import com.aenadgrleey.tobedone.utils.Importance
import com.aenadgrleey.tobedone.utils.Mapper
import java.util.Calendar

class TodoItemDataToTodoItem : Mapper<TodoItemData, TodoItem> {
    override fun map(input: TodoItemData) = with(input) {
        TodoItem(id!!, body, completed, importance, deadline, created, lastModified)
    }
}

class TodoItemToTodoItemData : Mapper<TodoItem, TodoItemData> {
    override fun map(input: TodoItem) = with(input) {
        TodoItemData(
            id,
            body ?: "",
            completed ?: false,
            importance ?: Importance.Common,
            deadline,
            created ?: Calendar.getInstance().time,
            lastModified ?: Calendar.getInstance().time
        )
    }
}