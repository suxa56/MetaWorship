package uz.suxa.metaworship.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import uz.suxa.metaworship.databinding.SongCardBinding
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality

class SongAdapter : ListAdapter<SongModel, SongViewHolder>(SongDiffUtil) {

    var onSongItemClickListener: ((SongModel) -> Unit)? = null
    var onSongItemLongClickListener: ((SongModel) -> Unit)? = null

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
                onSongItemLongClickListener?.invoke(song)
                true
            }
        }
    }
}