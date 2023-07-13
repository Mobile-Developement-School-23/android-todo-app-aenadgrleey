package com.aenadgrleey.tobedone.data.network

import com.google.gson.GsonBuilder
import org.junit.Test
import java.util.Date


class TodoItemDataJsonAdapterTest {
    @Test
    fun simpleTest() {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(com.aenadgrleey.data.models.TodoItemData::class.java, com.aenadgrleey.remote.network.TodoItemDataJsonAdapter())
        builder.setPrettyPrinting()
        val gson = builder.create()

        val jsonString = "" +
                "{\n" +
                "    \"id\": \"blablabla\",\n" +
                "    \"text\": \"blablabla\",\n" +
                "    \"importance\": \"low\", \n" +
                "    \"deadline\": 1687692949, \n" +
                "    \"done\": true,\n" +
                "    \"color\": \"#FFFFFF\", \n" +
                "    \"created_at\": 1687692949,\n" +
                "    \"changed_at\": 1687692949,\n" +
                "    \"last_updated_by\": \"blablabla\"\n" +
                "  }"
        val todoItem: com.aenadgrleey.data.models.TodoItemData = gson.fromJson(jsonString, com.aenadgrleey.data.models.TodoItemData::class.java)
        assert(todoItem == com.aenadgrleey.data.models.TodoItemData().apply {
            id = "blablabla"
            body = "blablabla"
            importance = com.aenadgrleey.data.utils.Importance.Low
            deadline = Date(1687692949L)
            completed = true
            color = "#FFFFFF"
            created = Date(1687692949L)
            lastModified = Date(1687692949L)
            lastModifiedBy = "blablabla"
        })
    }
}