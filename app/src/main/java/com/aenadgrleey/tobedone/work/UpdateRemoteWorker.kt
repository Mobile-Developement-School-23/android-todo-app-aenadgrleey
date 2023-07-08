package com.aenadgrleey.tobedone.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aenadgrleey.tobedone.data.datasources.TodoItemsLocalDataSource
import com.aenadgrleey.tobedone.data.datasources.TodoItemsRemoteDataSource
import com.aenadgrleey.tobedone.data.network.exceptions.NetworkCodeToExceptionTransformator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class UpdateRemoteWorker
@AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val localDataSource: TodoItemsLocalDataSource,
    private val remoteDataSource: TodoItemsRemoteDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        var result: Result? = null
        val networkHandler = NetworkCodeToExceptionTransformator(
            onConnectionAbsence = {
                result = Result.retry()
            },
            onServerError = {
                result = Result.retry()
            },
            onBadAuth = {
                result = Result.retry()
            },
            onElementAbsence = {
                result = Result.failure()
            },
            onBadSync = {
                result = Result.retry()
            },
            onFinal = {}
        )
        networkHandler.tryRemote {
            remoteDataSource.addTodoItems(localDataSource.getLocalTodoItems())
            result = Result.success()
        }
        return@withContext result!!
    }
}