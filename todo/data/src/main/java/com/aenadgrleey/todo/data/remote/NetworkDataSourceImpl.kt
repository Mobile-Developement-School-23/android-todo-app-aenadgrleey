package com.aenadgrleey.todo.data.remote

import com.aenadgrleey.auth.domain.AuthProvider
import com.aenadgrleey.auth.domain.model.AuthInfo
import com.aenadgrleey.core.domain.exceptions.DifferentRevisionsException
import com.aenadgrleey.core.domain.exceptions.NoSuchElementOnServerException
import com.aenadgrleey.core.domain.exceptions.ServerErrorException
import com.aenadgrleey.core.domain.exceptions.WrongAuthorizationException
import com.aenadgrleey.todo.data.remote.models.TodoItemDataNetwork
import com.aenadgrleey.todo.data.remote.network.URLs
import com.aenadgrleey.todo.data.remote.retrofit.RetrofitClient
import com.aenadgrleey.todo.data.remote.retrofit.requests.TodoItemRequest
import com.aenadgrleey.todo.data.remote.retrofit.requests.TodoItemsListRequest
import com.aenadgrleey.todo.data.remote.utils.DateConverter
import com.aenadgrleey.todo.data.remote.utils.ImportanceConverter
import com.aenadgrleey.todo.data.remote.utils.TodoItemDataToTodoItemNetworkMapper
import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todo.domain.remote.TodoItemsRemoteDataSource
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/*
 Implementation of remote client that uses Retrofit and okHttp
 */
class NetworkDataSourceImpl
@Inject constructor(authProvider: AuthProvider) : TodoItemsRemoteDataSource {

    private var lastKnownRevision: Int = 0

    private val mutex = Mutex()

    private val networkMapper = TodoItemDataToTodoItemNetworkMapper()

    private val authInfo = authProvider.authInfoFlow()
        .stateIn(
            CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            AuthInfo(null, null)
        )

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", (authInfo.value.authToken) ?: "dummy")
                .addHeader("X-Generate-Fails", "0")
                .build()
            chain.proceed(newRequest)
        }
        .connectTimeout(30, TimeUnit.MINUTES)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, DateConverter())
        .registerTypeAdapter(Importance::class.java, ImportanceConverter())
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
        mutex.withLock(lastKnownRevision) {
            retrofitClient.getTodoItems().run {
                checkResponseCode()
                body()!!.let {
                    checkResponseCode()
                    lastKnownRevision = it.revision
                    return it.todoItemsList.map(TodoItemDataNetwork::toTodoItemData)
                }
            }
        }
    }

    override suspend fun addTodoItems(items: List<TodoItemData>): List<TodoItemData> {
        mutex.withLock(lastKnownRevision) {
            retrofitClient.sendTodoItems(lastKnownRevision, TodoItemsListRequest(items.map(networkMapper::map))).run {
                checkResponseCode()
                lastKnownRevision = body()!!.revision
                return body()!!.todoItemsList.map(TodoItemDataNetwork::toTodoItemData)
            }
        }
    }

    override suspend fun addTodoItem(item: TodoItemData): TodoItemData {
        mutex.withLock(lastKnownRevision) {
            try {
                retrofitClient.getTodoItem(item.id!!).run {
                    checkResponseCode()
                    retrofitClient.updateTodoItem(body()!!.revision, TodoItemRequest(networkMapper.map(item))).run {
                        checkResponseCode()
                        lastKnownRevision = body()!!.revision
                        return body()!!.item.toTodoItemData()
                    }
                }
            } catch (noSuchElementOnServer: NoSuchElementOnServerException) {
                retrofitClient.addTodoItem(lastKnownRevision, TodoItemRequest(networkMapper.map(item))).run {
                    checkResponseCode()
                    lastKnownRevision = body()!!.revision
                    return body()!!.item.toTodoItemData()
                }
            }
        }
    }

    override suspend fun deleteTodoItem(item: TodoItemData) {
        mutex.withLock(lastKnownRevision) {
            retrofitClient.deleteTodoItem(lastKnownRevision, item.id!!).run {
                checkResponseCode()
                lastKnownRevision = body()!!.revision
            }
        }
    }

    private fun <T> Response<T>.checkResponseCode() {
        errorCodesToExceptionsMap[code()]?.let { throw it }
    }

    companion object {
        private const val UNSYNCHRONIZED_DATA_CODE = 400
        private const val WRONG_AUTHORIZATION_CODE = 401
        private const val NO_SUCH_ELEMENT_CODE = 404
        private const val SERVER_ERROR_CODE = 500
        private const val ANOTHER_SERVER_ERROR_CODE = 502

        private val errorCodesToExceptionsMap = mapOf(
            UNSYNCHRONIZED_DATA_CODE to DifferentRevisionsException(),
            WRONG_AUTHORIZATION_CODE to WrongAuthorizationException(),
            NO_SUCH_ELEMENT_CODE to NoSuchElementOnServerException(),
            SERVER_ERROR_CODE to ServerErrorException(),
            ANOTHER_SERVER_ERROR_CODE to ServerErrorException()
        )

    }
}