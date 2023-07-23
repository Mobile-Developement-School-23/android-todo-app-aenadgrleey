package com.aenadgrleey.todo.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.domain.models.TodoItemData
import java.util.Date

@Entity(tableName = "todo_items")
internal data class TodoItemDataDb(
    @PrimaryKey
    var id: String,
    var body: String,
    var completed: Boolean,
    var importance: Importance,
    var deadline: Date?,
    var created: Date,
    var lastModified: Date,
    var color: String?,
    var lastModifiedBy: String,
) {
    fun toTodoItemData(): TodoItemData = TodoItemData(
        id,
        body,
        completed,
        importance,
        deadline,
        created,
        lastModified,
        color,
        lastModifiedBy,
    )
}