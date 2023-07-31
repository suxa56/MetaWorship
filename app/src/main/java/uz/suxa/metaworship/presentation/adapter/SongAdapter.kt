package uz.suxa.metaworship.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import uz.suxa.metaworship.R
import uz.suxa.metaworship.databinding.CardSongBinding
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality

class SongAdapter(private val context: Context) :
    ListAdapter<SongModel, SongViewHolder>(SongDiffUtil) {

    var onSongItemClickListener: ((String) -> Unit)? = null
    var onSongItemEdit: ((String) -> Unit)? = null
    var onSongItemCopy: ((SongModel) -> Unit)? = null
    var onSongItemCopyInTonality: ((SongModel, Tonality) -> Unit)? = null
    var onSongItemDelete: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = CardSongBinding.inflate(
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
                popup.show()
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

                        R.id.copyChordsIn -> {
                            val popip = PopupMenu(context, root)
                            popip.menuInflater.inflate(R.menu.menu_tonalities, popip.menu)
                            popip.show()
                            popip.setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.c -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.C)
                                        true
                                    }

                                    R.id.cSharp -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.C_SHARP)
                                        true
                                    }

                                    R.id.d -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.D)
                                        true
                                    }

                                    R.id.eFlat -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.E_FLAT)
                                        true
                                    }

                                    R.id.e -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.E)
                                        true
                                    }


                                    R.id.f -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.F)
                                        true
                                    }

                                    R.id.fSharp -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.F_SHARP)
                                        true
                                    }

                                    R.id.g -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.G)
                                        true
                                    }

                                    R.id.aFlat -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.A_FLAT)
                                        true
                                    }

                                    R.id.a -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.A)
                                        true
                                    }

                                    R.id.hFlat -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.H_FLAT)
                                        true
                                    }

                                    R.id.h -> {
                                        onSongItemCopyInTonality?.invoke(song, Tonality.H)
                                        true
                                    }

                                    else -> false
                                }
                            }
                            true
                        }

                        R.id.deleteSong -> {
                            onSongItemDelete?.invoke(song.id)
                            true
                        }

                        else -> false
                    }
                }

                true
            }
        }
    }
}