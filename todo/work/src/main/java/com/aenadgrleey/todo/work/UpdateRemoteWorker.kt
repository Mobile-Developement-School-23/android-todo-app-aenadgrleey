package com.aenadgrleey.todo.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.aenadgrleey.core.data.remote.exceptions.DifferentRevisionsException
import com.aenadgrleey.core.data.remote.exceptions.NoSuchElementOnServerException
import com.aenadgrleey.core.data.remote.exceptions.ServerErrorException
import com.aenadgrleey.core.data.remote.exceptions.WrongAuthorizationException
import com.aenadgrleey.todo.domain.local.TodoItemsLocalDataSource
import com.aenadgrleey.todo.domain.remote.TodoItemsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Provider

/*
Worker for periodically downloading remote data
 */
class UpdateRemoteWorker
constructor(
    context: Context,
    params: WorkerParameters,
    private val localDataSource: TodoItemsLocalDataSource,
    private val remoteDataSource: TodoItemsRemoteDataSource,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            remoteDataSource.addTodoItems(localDataSource.getTodoItems())
            return@withContext Result.success()
        } catch (unknownHostException: UnknownHostException) {
            return@withContext Result.failure()
        } catch (serverErrorException: ServerErrorException) {
            return@withContext Result.failure()
        } catch (wrongAuthorization: WrongAuthorizationException) {
            return@withContext Result.failure()
        } catch (noSuchElementException: NoSuchElementOnServerException) {
            return@withContext Result.failure()
        } catch (unsynchronizedDataException: DifferentRevisionsException) {
            return@withContext Result.retry()
        }
    }

    class Factory @Inject constructor(
        private val local: Provider<TodoItemsLocalDataSource>,
        private val remote: Provider<TodoItemsRemoteDataSource>,
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return UpdateRemoteWorker(appContext, params, local.get(), remote.get())
        }
    }
}