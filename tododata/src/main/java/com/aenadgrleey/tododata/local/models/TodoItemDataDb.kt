package com.aenadgrleey.tododata.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "todo_items")
internal data class TodoItemDataDb(
    @PrimaryKey
    var id: String,
    var body: String,
    var completed: Boolean,
    var importance: com.aenadgrleey.core.domain.Importance,
    var deadline: Date? = null,
    var created: Date,
    var lastModified: Date,
    var color: String? = null,
    var lastModifiedBy: String,
) {
    fun copy(
        body: String = this.body,
        completed: Boolean = this.completed,
        importance: com.aenadgrleey.core.domain.Importance = this.importance,
        deadline: Date? = this.deadline,
        created: Date = this.created,
        lastModified: Date = this.lastModified,
        color: String? = this.color,
        lastModifiedBy: String = this.lastModifiedBy,
    ): TodoItemDataDb =
        TodoItemDataDb(
            this.id,
            body,
            completed,
            importance,
            deadline,
            created,
            lastModified,
            color,
            lastModifiedBy,
        )

    fun toTodoItemData(): com.aenadgrleey.core.domain.models.TodoItemData = com.aenadgrleey.core.domain.models.TodoItemData(
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