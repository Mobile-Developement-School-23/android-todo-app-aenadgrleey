package com.aenadgrleey.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.aenadgrleey.data.remote.exceptions.DifferentRevisionsException
import com.aenadgrleey.data.remote.exceptions.NoSuchElementOnServerException
import com.aenadgrleey.data.remote.exceptions.ServerErrorException
import com.aenadgrleey.data.remote.exceptions.WrongAuthorizationException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

/*
Worker to update data on server on connection loss
 */
class SyncWorker
@AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val localDataSource: com.aenadgrleey.local.TodoItemsLocalDataSource,
    private val remoteDataSource: com.aenadgrleey.remote.TodoItemsRemoteDataSource,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            localDataSource.clearDatabase()
            remoteDataSource.getTodoItems().forEach { localDataSource.addTodoItem(it) }
            return@withContext Result.success()
        } catch (unknownHostException: java.net.UnknownHostException) {
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
        private val local: Provider<com.aenadgrleey.local.TodoItemsLocalDataSource>,
        private val remote: Provider<com.aenadgrleey.remote.TodoItemsRemoteDataSource>,
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return SyncWorker(appContext, params, local.get(), remote.get())
        }
    }
}