package com.davinci.notasapp.data.remote

import com.davinci.notasapp.data.model.LoginRequest
import com.davinci.notasapp.data.model.LoginResponse
import com.davinci.notasapp.data.model.Note
import com.davinci.notasapp.data.model.NoteRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("notes")
    suspend fun createNote(@Body request: NoteRequest): Note

    @GET("notes")
    suspend fun getNotes(): List<Note>
}