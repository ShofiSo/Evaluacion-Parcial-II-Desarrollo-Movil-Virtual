package com.davinci.notasapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davinci.notasapp.NotasApplication
import com.davinci.notasapp.ui.login.LoginViewModel
import com.davinci.notasapp.ui.notes.CreateNoteViewModel
import com.davinci.notasapp.ui.notes.NotesViewModel

class ViewModelFactory(private val app: NotasApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(app.authRepository) as T
        modelClass.isAssignableFrom(CreateNoteViewModel::class.java) -> CreateNoteViewModel(app.notesRepository) as T
        modelClass.isAssignableFrom(NotesViewModel::class.java) -> NotesViewModel(app.notesRepository) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}