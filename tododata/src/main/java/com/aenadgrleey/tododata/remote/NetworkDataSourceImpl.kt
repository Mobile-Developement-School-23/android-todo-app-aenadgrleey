package com.aenadgrleey.tododata.remote

import com.aenadgrleey.auth.domain.AuthProvider
import com.aenadgrleey.auth.domain.model.AuthInfo
import com.aenadgrleey.core.data.remote.exceptions.DifferentRevisionsException
import com.aenadgrleey.core.data.remote.exceptions.NoSuchElementOnServerException
import com.aenadgrleey.core.data.remote.exceptions.ServerErrorException
import com.aenadgrleey.core.data.remote.exceptions.WrongAuthorizationException
import com.aenadgrleey.tododata.remote.network.RetrofitClient
import com.aenadgrleey.tododata.remote.network.TodoItemDataJsonAdapter
import com.aenadgrleey.tododata.remote.network.URLs
import com.aenadgrleey.tododata.remote.network.requests.TodoItemRequest
import com.aenadgrleey.tododata.remote.network.requests.TodoItemsListRequest
import com.aenadgrleey.tododomain.remote.TodoItemsRemoteDataSource
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/*
 Implementation of remote client that uses Retrofit and okHttp
 */
class NetworkDataSourceImpl
@Inject constructor(authProvider: AuthProvider) : TodoItemsRemoteDataSource {

    private var items: List<com.aenadgrleey.core.domain.models.TodoItemData> = listOf()
    private var lastKnownRevision: Int = 0

    private val authInfo = authProvider.authInfoFlow()
        .stateIn(
            CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            AuthInfo(null, null)
        )

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", ("OAuth " + authInfo.value.authToken))
                .addHeader("X-Generate-Fails", "0")
                .build()
            chain.proceed(newRequest)
        }
        .connectTimeout(30, TimeUnit.MINUTES)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(com.aenadgrleey.core.domain.models.TodoItemData::class.javaObjectType, TodoItemDataJsonAdapter())
        .serializeNulls()
        .create()

    private val retrofitClient: RetrofitClient by lazy {
        Retrofit.Builder().baseUrl(URLs.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(RetrofitClient::class.java)
    }


    override suspend fun getTodoItems(): List<com.aenadgrleey.core.domain.models.TodoItemData> {
        retrofitClient.getTodoItems().run {
            checkResponseCode()
            body()!!.let {
                checkResponseCode()
                items = it.todoItemsList
                lastKnownRevision = it.revision
            }
        }
        return items
    }

    override suspend fun addTodoItems(items: List<com.aenadgrleey.core.domain.models.TodoItemData>): List<com.aenadgrleey.core.domain.models.TodoItemData> {
        retrofitClient.sendTodoItems(lastKnownRevision, TodoItemsListRequest(items)).run {
            checkResponseCode()
            lastKnownRevision = body()!!.revision
            return body()!!.todoItemsList
        }
    }

    override suspend fun addTodoItem(item: com.aenadgrleey.core.domain.models.TodoItemData): com.aenadgrleey.core.domain.models.TodoItemData {
        try {
            retrofitClient.getTodoItem(item.id!!).run {
                checkResponseCode()
                retrofitClient.updateTodoItem(body()!!.revision, TodoItemRequest(item)).run {
                    checkResponseCode()
                    lastKnownRevision = body()!!.revision
                    return body()!!.item
                }
            }
        } catch (noSuchElementOnServer: NoSuchElementOnServerException) {
            retrofitClient.addTodoItem(lastKnownRevision, TodoItemRequest(item)).run {
                checkResponseCode()
                lastKnownRevision = body()!!.revision
                return body()!!.item
            }
        }
    }

    override suspend fun deleteTodoItem(item: com.aenadgrleey.core.domain.models.TodoItemData) {
        retrofitClient.deleteTodoItem(lastKnownRevision, item.id!!).run {
            checkResponseCode()
            lastKnownRevision = body()!!.revision
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