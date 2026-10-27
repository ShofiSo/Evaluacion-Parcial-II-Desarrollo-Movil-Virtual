package com.davinci.notasapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.davinci.notasapp.NotasApplication
import com.davinci.notasapp.R
import com.davinci.notasapp.databinding.ActivityLoginBinding
import com.davinci.notasapp.ui.dashboard.DashboardActivity
import com.davinci.notasapp.util.Resource
import com.davinci.notasapp.util.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory(application as NotasApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Si ya hay una sesión guardada, saltar directo al Dashboard.
        if ((application as NotasApplication).sessionManager.isLoggedIn()) {
            goToDashboard()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { attemptLogin() }
        observeState()
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        binding.tilEmail.error = null
        binding.tilPassword.error = null

        var valid = true
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.error_required); valid = false
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_required); valid = false
        }
        if (valid) viewModel.login(email, password)
    }

    private fun observeState() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.isEnabled = false
                    binding.tvError.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    goToDashboard()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    binding.tvError.text = state.message
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun goToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}