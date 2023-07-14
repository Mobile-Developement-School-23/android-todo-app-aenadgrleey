package com.aenadgrleey.tododata.remote.network

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.Date

/*
Adapter for remote operations
 */
internal class TodoItemDataJsonAdapter : JsonDeserializer<com.aenadgrleey.core.domain.models.TodoItemData>, JsonSerializer<com.aenadgrleey.core.domain.models.TodoItemData> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): com.aenadgrleey.core.domain.models.TodoItemData {
        val todoItemData = com.aenadgrleey.core.domain.models.TodoItemData()
        if (json != null) {
            val jsonObject = json.asJsonObject
            todoItemData.id = jsonObject.get("id").asString
            todoItemData.body = jsonObject.get("text").asString
            todoItemData.deadline = jsonObject.get("deadline")?.asLong?.let { Date(it) }
            todoItemData.completed = jsonObject.get("done").asBoolean
            todoItemData.color = jsonObject.get("color")?.asString
            todoItemData.created = Date(jsonObject.get("created_at").asLong)
            todoItemData.lastModified = Date(jsonObject.get("changed_at").asLong)
            todoItemData.lastModifiedBy = jsonObject.get("last_updated_by").asString
            jsonObject.get("importance").asString.let {
                todoItemData.importance = when (it) {
                    "low" -> com.aenadgrleey.core.domain.Importance.Low
                    "important" -> com.aenadgrleey.core.domain.Importance.High
                    else -> com.aenadgrleey.core.domain.Importance.Common
                }
            }
        }
        return todoItemData
    }

    override fun serialize(todoItemData: com.aenadgrleey.core.domain.models.TodoItemData?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val gson = Gson()
        val jsonObject = JsonObject()
        if (todoItemData != null) {
            todoItemData.id.let { jsonObject.add("id", gson.toJsonTree(it)) }
            todoItemData.body?.let { jsonObject.add("text", gson.toJsonTree(it)) }
            when (todoItemData.importance) {
                com.aenadgrleey.core.domain.Importance.Low -> "low"
                com.aenadgrleey.core.domain.Importance.High -> "important"
                else -> "basic"
            }.let { jsonObject.add("importance", gson.toJsonTree(it)) }
            todoItemData.deadline?.let { jsonObject.add("deadline", gson.toJsonTree(it.time.toLong())) }
            todoItemData.completed?.let { jsonObject.add("done", gson.toJsonTree(it)) }
            todoItemData.color?.let { jsonObject.add("color", gson.toJsonTree(it)) }
            todoItemData.created?.let { jsonObject.add("created_at", gson.toJsonTree(it.time.toLong())) }
            todoItemData.lastModified?.let { jsonObject.add("changed_at", gson.toJsonTree(it.time)) }
            todoItemData.lastModifiedBy?.let { jsonObject.add("last_updated_by", gson.toJsonTree(it)) }
        }
        return jsonObject
    }
}