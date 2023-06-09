package uz.suxa.metaworship.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import uz.suxa.metaworship.databinding.SongCardBinding
import uz.suxa.metaworship.domain.model.SongModel

class SongAdapter : ListAdapter<SongModel, SongViewHolder>(SongDiffUtil) {

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
                songTonality.text = defaultTonality.toString()
                songLyrics.text = lyrics
                songVocalists.text = vocalistTonality.toString()
            }
        }
    }
}