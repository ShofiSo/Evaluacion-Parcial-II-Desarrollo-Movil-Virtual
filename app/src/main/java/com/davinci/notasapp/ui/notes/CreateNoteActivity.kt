package com.davinci.notasapp.ui.notes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.davinci.notasapp.NotasApplication
import com.davinci.notasapp.R
import com.davinci.notasapp.databinding.ActivityCreateNoteBinding
import com.davinci.notasapp.util.Resource
import com.davinci.notasapp.util.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding
    private val viewModel: CreateNoteViewModel by viewModels {
        ViewModelFactory(application as NotasApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener { save() }
        observeState()
    }

    private fun save() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        binding.tilTitle.error = null
        binding.tilContent.error = null

        var valid = true
        if (title.isEmpty()) {
            binding.tilTitle.error = getString(R.string.error_required); valid = false
        }
        if (content.isEmpty()) {
            binding.tilContent.error = getString(R.string.error_required); valid = false
        }
        if (valid) viewModel.saveNote(title, content)
    }

    private fun observeState() {
        viewModel.state.observe(this) { state ->
            when (state) {
                Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show()
                    binding.etTitle.text?.clear()
                    binding.etContent.text?.clear()
                    viewModel.clearState()
                }
                is Resource.Error -> {
                    setLoading(false)
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    viewModel.clearState()
                }
                null -> setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnSave.isEnabled = !loading
    }
}