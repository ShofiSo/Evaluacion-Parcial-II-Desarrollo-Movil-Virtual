package com.davinci.notasapp.data.repository

import com.davinci.notasapp.data.model.Note
import com.davinci.notasapp.data.model.NoteRequest
import com.davinci.notasapp.data.remote.ApiService
import com.davinci.notasapp.util.Resource
import com.davinci.notasapp.util.safeApiCall

class NotesRepository(private val api: ApiService) {

    suspend fun createNote(title: String, content: String): Resource<Note> =
        safeApiCall { api.createNote(NoteRequest(title, content)) }

    suspend fun getNotes(): Resource<List<Note>> =
        safeApiCall { api.getNotes() }
}