package com.davinci.notasapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinci.notasapp.data.model.LoginResponse
import com.davinci.notasapp.data.repository.AuthRepository
import com.davinci.notasapp.util.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<LoginResponse>>()
    val loginState: LiveData<Resource<LoginResponse>> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = Resource.Loading
        viewModelScope.launch {
            _loginState.value = repository.login(email.trim(), password)
        }
    }
}