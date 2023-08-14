package uz.suxa.metaworship.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import uz.suxa.metaworship.databinding.CardSongBinding
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.presentation.SongActionsBottomSheet

class SongAdapter(private val fragmentManager: FragmentManager) :
    ListAdapter<SongModel, SongViewHolder>(SongDiffUtil) {

    var onSongItemClickListener: ((String) -> Unit)? = null
    var onSongItemEdit: ((String) -> Unit)? = null
    var onSongItemCopy: ((SongModel, Tonality) -> Unit)? = null
    var onSongItemCopyLyrics: ((SongModel) -> Unit)? = null
    var onSongItemCopyInTonality: ((SongModel, Tonality) -> Unit)? = null
    var onSongAddToComposition: (() -> Unit)? = null
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
                val bottomSheet = SongActionsBottomSheet()
                bottomSheet.setSong(song)
                bottomSheet.show(
                    fragmentManager,
                    SongActionsBottomSheet.TAG
                )

                bottomSheet.onSongEdit = {
                    onSongItemEdit?.invoke(song.id)
                    bottomSheet.dismiss()
                }

                bottomSheet.onSongCopy = {
                    onSongItemCopy?.invoke(song, it)
                    bottomSheet.dismiss()
                }

                bottomSheet.onSongCopyIn = {
                    onSongItemCopyInTonality?.invoke(song, it)
                    bottomSheet.dismiss()
                }

                bottomSheet.onSongCopyLyrics = {
                    onSongItemCopyLyrics?.invoke(song)
                    bottomSheet.dismiss()
                }

                bottomSheet.onSongAddToComposition = {
                    onSongAddToComposition?.invoke()
//                    bottomSheet.dismiss()
                }

                bottomSheet.onSongDelete = {
                    bottomSheet.dismiss()
                    onSongItemDelete?.invoke(song.id)
                }
                true
            }
        }
    }
}