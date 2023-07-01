package com.aenadgrleey.tobedone.data.network.exceptions

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NetworkCodeToExceptionTransformator(
    val onConnectionAbsence: suspend (suspend () -> Unit) -> Unit,
    val onServerError: suspend (suspend () -> Unit) -> Unit,
    val onBadAuth: suspend (suspend () -> Unit) -> Unit,
    val onElementAbsence: suspend (suspend () -> Unit) -> Unit,
    val onBadSync: suspend (suspend () -> Unit) -> Unit,
    val onFinal: suspend (suspend () -> Unit) -> Unit
) {
    suspend fun tryRemote(block: suspend () -> Unit) {
        try {
            block()
        } catch (unknownHostException: java.net.UnknownHostException) {
            withContext(Dispatchers.Main) { Log.e("NetworkError", unknownHostException.toString()) }
            this.tryRemote { onConnectionAbsence(block) }
        } catch (serverErrorException: ServerErrorException) {
            withContext(Dispatchers.Main) { Log.e("NetworkError", serverErrorException.toString()) }
            this.tryRemote { onServerError(block) }
        } catch (wrongAuthorization: WrongAuthorizationException) {
            withContext(Dispatchers.Main) { Log.e("NetworkError", wrongAuthorization.toString()) }
            this.tryRemote { onBadAuth(block) }
        } catch (noSuchElementException: NoSuchElementException) {
            withContext(Dispatchers.Main) { Log.e("NetworkError", noSuchElementException.toString()) }
            this.tryRemote { onElementAbsence(block) }
        } catch (unsynchronizedDataException: DifferentRevisionsException) {
            withContext(Dispatchers.Main) { Log.e("NetworkError", unsynchronizedDataException.toString()) }
            this.tryRemote { onBadSync(block) }
        } finally {
            onFinal(block)
        }
    }

    companion object {
        const val UNSYNCHRONIZED_DATA_CODE = 400
        const val WRONG_AUTHORIZATION_CODE = 401
        const val NO_SUCH_ELEMENT_CODE = 404
        const val SERVER_ERROR_CODE = 500

        private val errorCodesToExceptionsMap = mapOf(
            UNSYNCHRONIZED_DATA_CODE to DifferentRevisionsException(),
            WRONG_AUTHORIZATION_CODE to WrongAuthorizationException(),
            NO_SUCH_ELEMENT_CODE to NoSuchElementException(),
            SERVER_ERROR_CODE to ServerErrorException()
        )

        suspend fun <T> onSuccessOf(response: Response<T>, block: suspend (Response<T>) -> Unit) {
            errorCodesToExceptionsMap[response.code()]?.let { throw it }
            block(response)
        }
    }
}