package uz.suxa.metaworship.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import uz.suxa.metaworship.R
import uz.suxa.metaworship.databinding.SongCardBinding
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality

class SongAdapter(private val context: Context) :
    ListAdapter<SongModel, SongViewHolder>(SongDiffUtil) {

    var onSongItemClickListener: ((String) -> Unit)? = null
    var onSongItemEdit: ((String) -> Unit)? = null
    var onSongItemCopy: ((SongModel) -> Unit)? = null
    var onSongItemDelete: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        with(holder.binding) {
            with(song) {
                songTitle.text = title
                if (defaultTonality == Tonality.UNDEFINED) {
                    songTonality.visibility = View.GONE
                } else {
                    songTonality.text = defaultTonality.toString()
                        .replace("_FLAT", "b")
                        .replace("_SHARP", "#")
                }
                if (lyrics.isBlank()) {
                    songLyrics.visibility = View.GONE
                } else {
                    songLyrics.text = lyrics
                }
                if (vocalistTonality.isEmpty()) {
                    materialDivider.visibility = View.GONE
                    songVocalists.visibility = View.GONE
                } else {
                    var vocalists = ""
                    vocalistTonality.forEach {
                        vocalists += "\n${it.vocalist}: ${it.tonality}"
                    }
                    songVocalists.text = vocalists.replaceFirst("\n", "")
                }
            }
            root.setOnClickListener {
                onSongItemClickListener?.invoke(song.id)
            }
            root.setOnLongClickListener {
                val popup = PopupMenu(context, root)
                popup.menuInflater.inflate(R.menu.menu_song_list, popup.menu)
                popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.editSong -> {
                            onSongItemEdit?.invoke(song.id)
                            true
                        }

                        R.id.copyChords -> {
                            onSongItemCopy?.invoke(song)
                            true
                        }


                        R.id.deleteSong -> {
                            onSongItemDelete?.invoke(song.id)
                            true
                        }

                        else -> false
                    }
                }
                popup.show()

                true
            }
        }
    }
}