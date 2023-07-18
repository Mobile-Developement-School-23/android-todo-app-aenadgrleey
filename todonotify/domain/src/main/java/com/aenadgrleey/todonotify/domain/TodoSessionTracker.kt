package com.aenadgrleey.todonotify.domain

import android.app.Service
import com.aenadgrleey.core.domain.models.TodoItemData

abstract class TodoSessionTracker : Service() {
    abstract fun handleTodo(todoItemData: TodoItemData)
}