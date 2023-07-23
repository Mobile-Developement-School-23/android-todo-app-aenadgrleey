package com.aenadgrleey.todo.data.remote.utils

import com.aenadgrleey.todo.domain.models.Importance
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ImportanceConverter : JsonDeserializer<Importance>, JsonSerializer<Importance> {
    private val gson = Gson()
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Importance {
        return json!!.asString.let {
            when (it) {
                "low" -> Importance.Low
                "important" -> Importance.High
                else -> Importance.Common
            }
        }
    }

    override fun serialize(src: Importance, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return when (src) {
            Importance.Low -> "low"
            Importance.High -> "important"
            Importance.Common -> "basic"
        }.let { JsonPrimitive(it) }
    }
}