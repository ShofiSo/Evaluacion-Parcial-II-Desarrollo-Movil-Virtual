package com.davinci.notasapp.data.repository

import com.davinci.notasapp.data.local.SessionManager
import com.davinci.notasapp.data.model.LoginRequest
import com.davinci.notasapp.data.model.LoginResponse
import com.davinci.notasapp.data.remote.ApiService
import com.davinci.notasapp.util.Resource
import com.davinci.notasapp.util.safeApiCall

class AuthRepository(private val api: ApiService, private val session: SessionManager) {

    suspend fun login(email: String, password: String): Resource<LoginResponse> {
        val result = safeApiCall(
            unauthorizedMessage = "Credenciales incorrectas",
            invalidCodes = setOf(400, 401, 403, 404)
        ) { api.login(LoginRequest(email, password)) }

        if (result is Resource.Success) {
            val name = result.data.user?.name?.takeIf { it.isNotBlank() } ?: email
            session.saveSession(result.data.token, name)
        }
        return result
    }
}