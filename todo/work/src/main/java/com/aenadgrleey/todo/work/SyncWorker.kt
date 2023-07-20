package com.aenadgrleey.todo.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
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
    private val repository: TodoItemRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        repository.fetchRemoteData()
        Result.success()
    }


    class Factory @Inject constructor(
        private val repository: Provider<TodoItemRepository>,
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return SyncWorker(appContext, params, repository.get())
        }
    }
}