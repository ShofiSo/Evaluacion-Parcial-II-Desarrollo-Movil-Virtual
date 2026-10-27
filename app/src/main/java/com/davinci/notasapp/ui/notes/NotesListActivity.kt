package com.davinci.notasapp.ui.notes

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.davinci.notasapp.NotasApplication
import com.davinci.notasapp.R
import com.davinci.notasapp.databinding.ActivityNotesListBinding
import com.davinci.notasapp.util.Resource
import com.davinci.notasapp.util.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class NotesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesListBinding
    private val adapter = NotesAdapter()
    private val viewModel: NotesViewModel by viewModels {
        ViewModelFactory(application as NotasApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = adapter
        observeNotes()
    }
    override fun onResume() {
        super.onResume()
        viewModel.loadNotes()
    }

    private fun observeNotes() {
        viewModel.notes.observe(this) { state ->
            when (state) {
                Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvMessage.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(state.data)
                    binding.tvMessage.setText(R.string.notes_empty)
                    binding.tvMessage.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) { viewModel.loadNotes() }
                        .show()
                }
            }
        }
    }
}