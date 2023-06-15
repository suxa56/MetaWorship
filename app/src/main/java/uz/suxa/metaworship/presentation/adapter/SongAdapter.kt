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

class SongAdapter(val context: Context) : ListAdapter<SongModel, SongViewHolder>(SongDiffUtil) {

    var onSongItemClickListener: ((SongModel) -> Unit)? = null
    var onSongItemDelete: ((SongModel) -> Unit)? = null

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
                    songVocalists.text = vocalistTonality.toString()
                }
            }
            root.setOnClickListener {
                onSongItemClickListener?.invoke(song)
            }
            root.setOnLongClickListener {
                val popup = PopupMenu(context, root)
                popup.menuInflater.inflate(R.menu.menu_song_list, popup.menu)
                popup.setOnMenuItemClickListener {menuItem: MenuItem ->
                    when(menuItem.itemId) {
                        R.id.deleteSong -> {
                            onSongItemDelete?.invoke(song)
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