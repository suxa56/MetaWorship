package uz.suxa.metaworship.presentation.adapter.vocalist

import androidx.recyclerview.widget.DiffUtil
import uz.suxa.metaworship.domain.dto.VocalistSongDto

object VocalistDiffUtil: DiffUtil.ItemCallback<VocalistSongDto>() {

    override fun areItemsTheSame(oldItem: VocalistSongDto, newItem: VocalistSongDto): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: VocalistSongDto, newItem: VocalistSongDto): Boolean {
        return oldItem == newItem
    }
}