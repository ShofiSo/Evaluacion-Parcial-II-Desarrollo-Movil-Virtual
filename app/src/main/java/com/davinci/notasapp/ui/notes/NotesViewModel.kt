package com.davinci.notasapp.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinci.notasapp.data.model.Note
import com.davinci.notasapp.data.repository.NotesRepository
import com.davinci.notasapp.util.Resource
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _notes = MutableLiveData<Resource<List<Note>>>()
    val notes: LiveData<Resource<List<Note>>> = _notes

    fun loadNotes() {
        _notes.value = Resource.Loading
        viewModelScope.launch {
            _notes.value = repository.getNotes()
        }
    }
}