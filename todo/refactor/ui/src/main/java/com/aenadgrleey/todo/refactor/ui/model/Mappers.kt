package com.aenadgrleey.todo.refactor.ui.model

import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todo.domain.utils.Mapper

class TodoItemDataToUiStateMapper : Mapper<TodoItemData?, UiState> {
    override fun map(input: TodoItemData?): UiState = with(input) {
        UiState(
            text = this?.body,
            completeness = this?.completed == true,
            importance = this?.importance ?: Importance.Common,
            deadlineEnabled = this?.deadline != null,
            deadline = this?.deadline,
            color = this?.color,
            id = this?.id,
            lastModified = this?.lastModified,
            created = this?.created,
            lastModifiedBy = this?.lastModifiedBy
        )
    }
}

class UiStateToTodoItemData : Mapper<UiState, TodoItemData> {
    override fun map(input: UiState): TodoItemData = with(input) {
        TodoItemData(
            id = id,
            body = text!!,
            completed = completeness,
            importance = importance,
            deadline = if (deadlineEnabled) deadline else null,
            created = created,
            lastModified = lastModified,
            color = color,
            lastModifiedBy = lastModifiedBy
        )
    }

}