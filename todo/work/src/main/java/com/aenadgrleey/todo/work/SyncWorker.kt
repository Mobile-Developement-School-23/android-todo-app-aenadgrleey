package com.aenadgrleey.todo.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.aenadgrleey.core.domain.exceptions.DifferentRevisionsException
import com.aenadgrleey.core.domain.exceptions.NoSuchElementOnServerException
import com.aenadgrleey.core.domain.exceptions.ServerErrorException
import com.aenadgrleey.core.domain.exceptions.WrongAuthorizationException
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Provider

/*
Worker to update data on server on connection loss
 */
class SyncWorker
@AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: TodoItemRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            repository.fetchRemoteData()
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
        private val repository: Provider<TodoItemRepository>,
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return SyncWorker(appContext, params, repository.get())
        }
    }
}