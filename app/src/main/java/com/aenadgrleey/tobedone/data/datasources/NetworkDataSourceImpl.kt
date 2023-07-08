package com.aenadgrleey.tobedone.data.datasources

import com.aenadgrleey.tobedone.data.models.TodoItemData
import com.aenadgrleey.tobedone.data.models.TodoItemDataJsonAdapter
import com.aenadgrleey.tobedone.data.network.RetrofitClient
import com.aenadgrleey.tobedone.data.network.Tokens
import com.aenadgrleey.tobedone.data.network.URLs
import com.aenadgrleey.tobedone.data.network.exceptions.NetworkCodeToExceptionTransformator
import com.aenadgrleey.tobedone.data.network.exceptions.NoSuchElementException
import com.aenadgrleey.tobedone.data.network.requests.TodoItemRequest
import com.aenadgrleey.tobedone.data.network.requests.TodoItemsListRequest
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkDataSourceImpl : TodoItemsRemoteDataSource {
    private var items: List<TodoItemData> = listOf()
    private var lastKnownRevision: Int = 0

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${Tokens.token}")
                .addHeader("X-Generate-Fails", "0")
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor { chain ->
            chain.proceed(chain.request())
        }
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(TodoItemData::class.javaObjectType, TodoItemDataJsonAdapter())
        .serializeNulls()
        .create()


    private val retrofitClient: RetrofitClient by lazy {
        Retrofit.Builder().baseUrl(URLs.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(RetrofitClient::class.java)
    }


    override suspend fun getTodoItems(): List<TodoItemData> {
        NetworkCodeToExceptionTransformator.onSuccessOf(retrofitClient.getTodoItems()) { getResponse ->
            getResponse.body()!!.let {
                items = it.todoItemsList
                lastKnownRevision = it.revision
            }
        }
        return items
    }

    override suspend fun syncData() {
        retrofitClient.getTodoItems()
    }

    override suspend fun addTodoItems(items: List<TodoItemData>): List<TodoItemData> {
        var result: List<TodoItemData>? = null
        NetworkCodeToExceptionTransformator.onSuccessOf(
            retrofitClient.sendTodoItems(lastKnownRevision, TodoItemsListRequest(items))
        ) { sendResponse ->
            lastKnownRevision = sendResponse.body()!!.revision
            result = sendResponse.body()!!.todoItemsList
        }
        return result!!
    }

    override suspend fun addTodoItem(item: TodoItemData): TodoItemData {
        var result: TodoItemData? = null
        try {
            NetworkCodeToExceptionTransformator.onSuccessOf(retrofitClient.getTodoItem(item.id)) { getResponse ->
                NetworkCodeToExceptionTransformator.onSuccessOf(
                    retrofitClient.updateTodoItem(getResponse.body()!!.revision, TodoItemRequest(item))
                ) {
                    result = it.body()!!.item
                }
            }
        } catch (noSuchElementException: NoSuchElementException) {
            NetworkCodeToExceptionTransformator.onSuccessOf(
                retrofitClient.addTodoItem(lastKnownRevision, TodoItemRequest(item))
            ) {
                result = it.body()!!.item
            }
        }
        return result!!
    }

    override suspend fun deleteTodoItem(item: TodoItemData) {
        NetworkCodeToExceptionTransformator.onSuccessOf(retrofitClient.getTodoItem(item.id)) { getResponse ->
            NetworkCodeToExceptionTransformator.onSuccessOf(retrofitClient.deleteTodoItem(getResponse.body()!!.revision, item.id)) { deleteResponse ->
                lastKnownRevision = deleteResponse.body()!!.revision
            }
        }
    }

}