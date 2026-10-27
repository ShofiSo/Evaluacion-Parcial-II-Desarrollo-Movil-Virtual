package com.davinci.notasapp.util

import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(
    unauthorizedMessage: String = "Sesión no válida. Inicia sesión nuevamente",
    invalidCodes: Set<Int> = setOf(401, 403),
    call: suspend () -> T
): Resource<T> = try {
    Resource.Success(call())
} catch (e: CancellationException) {
    throw e
} catch (e: HttpException) {
    if (e.code() in invalidCodes) Resource.Error(unauthorizedMessage)
    else Resource.Error("Error del servidor (código ${e.code()})")
} catch (e: IOException) {
    Resource.Error("Error de conexión. Verifica tu red e inténtalo de nuevo.")
} catch (e: Exception) {
    Resource.Error("Ocurrió un error inesperado: ${e.localizedMessage}")
}