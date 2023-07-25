package com.aenadgrleey.todonotify.domain

import com.aenadgrleey.todo.domain.models.TodoItemData


interface TodoNotificationDispatcher {
    fun handleTodo(todoItemData: TodoItemData)
}