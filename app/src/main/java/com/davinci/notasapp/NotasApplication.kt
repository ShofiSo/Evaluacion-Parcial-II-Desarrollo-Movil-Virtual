package com.davinci.notasapp

import android.app.Application
import com.davinci.notasapp.data.local.SessionManager
import com.davinci.notasapp.data.remote.ApiService
import com.davinci.notasapp.data.remote.RetrofitClient
import com.davinci.notasapp.data.repository.AuthRepository
import com.davinci.notasapp.data.repository.NotesRepository

/** Contenedor simple de dependencias (una sola instancia por proceso). */
class NotasApplication : Application() {
    val sessionManager: SessionManager by lazy { SessionManager(this) }
    private val api: ApiService by lazy { RetrofitClient.create(sessionManager) }
    val authRepository: AuthRepository by lazy { AuthRepository(api, sessionManager) }
    val notesRepository: NotesRepository by lazy { NotesRepository(api) }
}