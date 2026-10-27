package com.davinci.notasapp.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinci.notasapp.data.model.Note
import com.davinci.notasapp.data.repository.NotesRepository
import com.davinci.notasapp.util.Resource
import kotlinx.coroutines.launch

class CreateNoteViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _state = MutableLiveData<Resource<Note>?>()
    val state: LiveData<Resource<Note>?> = _state

    fun saveNote(title: String, content: String) {
        _state.value = Resource.Loading
        viewModelScope.launch {
            _state.value = repository.createNote(title.trim(), content.trim())
        }
    }

    /** Se llama después de mostrar el resultado para que no se repita al rotar la pantalla. */
    fun clearState() {
        _state.value = null
    }
}