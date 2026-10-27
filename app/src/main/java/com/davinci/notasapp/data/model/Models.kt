package com.davinci.notasapp.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(val email: String, val password: String)

data class User(val id: Int?, val name: String?, val email: String?)

data class LoginResponse(val token: String, val user: User?)

data class NoteRequest(val title: String, val content: String)

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    @SerializedName("created_at") val createdAt: String?
)