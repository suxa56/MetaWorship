package uz.suxa.metaworship.presentation.adapter.solo

import androidx.recyclerview.widget.DiffUtil
import uz.suxa.metaworship.domain.model.SoloPart

object SoloPartDiffUtil: DiffUtil.ItemCallback<SoloPart>() {

    override fun areItemsTheSame(oldItem: SoloPart, newItem: SoloPart): Boolean {
        return oldItem.part == newItem.part
    }

    override fun areContentsTheSame(oldItem: SoloPart, newItem: SoloPart): Boolean {
        return oldItem == newItem
    }
}