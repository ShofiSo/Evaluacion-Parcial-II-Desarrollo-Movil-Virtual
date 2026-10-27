package com.davinci.notasapp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davinci.notasapp.NotasApplication
import com.davinci.notasapp.R
import com.davinci.notasapp.databinding.ActivityDashboardBinding
import com.davinci.notasapp.ui.login.LoginActivity
import com.davinci.notasapp.ui.notes.CreateNoteActivity
import com.davinci.notasapp.ui.notes.NotesListActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = (application as NotasApplication).sessionManager
        binding.tvWelcome.text = getString(R.string.welcome_message, session.getUserName())

        binding.btnCreateNote.setOnClickListener {
            startActivity(Intent(this, CreateNoteActivity::class.java))
        }
        binding.btnViewNotes.setOnClickListener {
            startActivity(Intent(this, NotesListActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            session.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}