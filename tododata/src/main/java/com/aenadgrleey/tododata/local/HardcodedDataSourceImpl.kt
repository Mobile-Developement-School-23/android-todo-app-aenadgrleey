package com.aenadgrleey.tododata.local

import android.content.Context
import com.aenadgrleey.local.TodoItemsLocalDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.util.Calendar
import com.aenadgrleey.resources.R as CommonR

/*
Implementation of local storage that is generating items for testing
 */
class HardcodedDataSourceImpl(private val context: Context) : TodoItemsLocalDataSource {
    var updateItemFlow: suspend () -> Unit = {}
    var updateCountFlow: suspend () -> Unit = {}

    private var counter = 0
    private val items = mutableListOf<com.aenadgrleey.core.domain.models.TodoItemData>().apply {
        val texts = listOf(
            context.getText(CommonR.string.buy_something),
            context.getString(CommonR.string.lorem_ipsum)
        )
        val completed = listOf(true, false)
        val importance = listOf(com.aenadgrleey.core.domain.Importance.Common, com.aenadgrleey.core.domain.Importance.High, com.aenadgrleey.core.domain.Importance.Low)
        val deadline = listOf(Calendar.getInstance().time, null)
        for (importanceType in 0..2) {
            for (textType in 0..1) {
                for (completenessType in 0..1) {
                    for (deadlineExistence in 0..1) {
                        this.add(
                            com.aenadgrleey.core.domain.models.TodoItemData(
                                id = counter.toString(),
                                body = texts[textType].toString(),
                                completed = completed[completenessType],
                                importance = importance[importanceType],
                                deadline = deadline[deadlineExistence],
                            )
                        )
                        counter += 1
                    }
                }
            }
        }
    }

    override suspend fun addTodoItem(todoItem: com.aenadgrleey.core.domain.models.TodoItemData) {
        var insertFlag = true
        items.forEachIndexed { index, item ->
            if (item.id == todoItem.id) {
                items[index] = todoItem
                insertFlag = false
            }
        }
        if (insertFlag) {
            todoItem.id = counter.toString()
            items.add(todoItem)
        }

        updateItemFlow()
        updateCountFlow()
    }

    override fun getTodoItems(excludeCompleted: Boolean): Flow<List<com.aenadgrleey.core.domain.models.TodoItemData>> {
        return channelFlow {
            updateItemFlow = {
                send(items.filter {
                    (!(excludeCompleted || it.completed == true)) || excludeCompleted
                })
            }
            updateItemFlow()
            awaitClose()
        }
    }

    override fun getTodoItems(): List<com.aenadgrleey.core.domain.models.TodoItemData> {
        return items
    }

    override fun clearDatabase() {
        return items.clear()
    }

    override fun completedItemsCount(): Flow<Int> = channelFlow {
        updateCountFlow = {
            send(items.filter { it.completed == true }.size)
        }
        updateCountFlow()
        awaitClose()
    }

    override suspend fun deleteTodoItem(todoItem: com.aenadgrleey.core.domain.models.TodoItemData) {
        for (index in 0..items.lastIndex) {
            if (items[index].id == todoItem.id) {
                items.removeAt(index)
                break
            }
        }
        updateItemFlow()
        updateCountFlow()
    }
}