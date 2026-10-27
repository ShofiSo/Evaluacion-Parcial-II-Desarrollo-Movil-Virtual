package com.davinci.notasapp.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davinci.notasapp.data.model.Note
import com.davinci.notasapp.databinding.ItemNoteBinding

class NotesAdapter : ListAdapter<Note, NotesAdapter.NoteViewHolder>(DIFF) {

    class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        with(holder.binding) {
            tvNoteTitle.text = note.title
            tvNoteContent.text = note.content
            tvNoteDate.text = note.createdAt?.take(10).orEmpty()
        }
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
        }
    }
}