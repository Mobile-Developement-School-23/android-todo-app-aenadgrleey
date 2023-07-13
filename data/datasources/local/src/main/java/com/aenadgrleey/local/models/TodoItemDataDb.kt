package com.aenadgrleey.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aenadgrleey.data.models.TodoItemData
import com.aenadgrleey.data.utils.Importance
import java.util.Date

@Entity(tableName = "todo_items")
internal data class TodoItemDataDb(
    @PrimaryKey
    var id: String,
    var body: String,
    var completed: Boolean,
    var importance: Importance,
    var deadline: Date? = null,
    var created: Date,
    var lastModified: Date,
    var color: String? = null,
    var lastModifiedBy: String,
) {
    fun copy(
        body: String = this.body,
        completed: Boolean = this.completed,
        importance: Importance = this.importance,
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