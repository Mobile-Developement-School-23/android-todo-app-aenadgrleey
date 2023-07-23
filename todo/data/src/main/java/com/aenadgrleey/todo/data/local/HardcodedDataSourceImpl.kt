package com.aenadgrleey.todo.data.local

import android.content.Context
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.todo.domain.local.TodoItemsLocalDataSource
import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.domain.models.TodoItemData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import com.aenadgrleey.resources.R as CommonR

/*
Implementation of local storage that is generating items for testing
FOR DEBUG ONLY!!!!
it doesn't support several data receivers
on any function retuning Flow it won't create new Flow, but will return existing one
 */
class HardcodedDataSourceImpl @Inject constructor(@AppContext private val context: Context) : TodoItemsLocalDataSource {

    private val mTodoItemsChannel = Channel<List<TodoItemData>>()
    private val mTodoItemsCompletedCountChannel = Channel<Int>()

    private var counter = 0
    private val items = mutableListOf<TodoItemData>().apply {
        val texts = listOf(
            context.getText(CommonR.string.buy_something),
            context.getString(CommonR.string.lorem_ipsum)
        )
        val completed = listOf(true, false)
        val importance = listOf(Importance.Common, Importance.High, Importance.Low)
        val deadline = listOf(Date(Calendar.getInstance().timeInMillis + 24 * 60 * 60 * 1000), null)
        val created = Calendar.getInstance().time
        for (importanceType in 0..2) {
            for (textType in 0..1) {
                for (completenessType in 0..1) {
                    for (deadlineExistence in 0..1) {
                        this.add(
                            TodoItemData(
                                id = counter.toString(),
                                body = texts[textType].toString(),
                                completed = completed[completenessType],
                                importance = importance[importanceType],
                                deadline = deadline[deadlineExistence],
                                created = created,
                                lastModified = created,
                                lastModifiedBy = "debug"
                            )
                        )
                        counter += 1
                    }
                }
            }
        }
    }

    override suspend fun addTodoItem(todoItem: TodoItemData) {
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
        mTodoItemsChannel.send(items)
        mTodoItemsCompletedCountChannel.send(items.count(TodoItemData::completed))

    }

    override fun getTodoItems(excludeCompleted: Boolean): Flow<List<TodoItemData>> = mTodoItemsChannel.receiveAsFlow()
        .onStart {
            mTodoItemsChannel.send(items)
            mTodoItemsCompletedCountChannel.send(items.count(TodoItemData::completed))
        }

    override suspend fun getTodoItems(): List<TodoItemData> = items

    override suspend fun todoItem(id: String): TodoItemData? = items.find { it.id == id }

    override fun clearDatabase() {
        items.clear()
    }

    override fun completedItemsCount(): Flow<Int> = mTodoItemsCompletedCountChannel.receiveAsFlow()
        .onStart {
            mTodoItemsChannel.send(items)
            mTodoItemsCompletedCountChannel.send(items.count(TodoItemData::completed))
        }

    override suspend fun deleteTodoItem(todoItem: TodoItemData) {
        for (index in 0..items.lastIndex) {
            if (items[index].id == todoItem.id) {
                items.removeAt(index)
                break
            }
        }
        mTodoItemsChannel.send(items)
        mTodoItemsCompletedCountChannel.send(items.count(TodoItemData::completed))
    }
}