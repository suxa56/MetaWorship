package uz.suxa.metaworship.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import uz.suxa.metaworship.domain.model.SongModel

object SongDiffUtil: DiffUtil.ItemCallback<SongModel>() {

    override fun areItemsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
        return oldItem == newItem
    }
}