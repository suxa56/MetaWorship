package uz.suxa.metaworship.presentation.adapter.vocalist

import androidx.recyclerview.widget.DiffUtil
import uz.suxa.metaworship.domain.model.VocalistModel

object VocalistDiffUtil: DiffUtil.ItemCallback<VocalistModel>() {

    override fun areItemsTheSame(oldItem: VocalistModel, newItem: VocalistModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: VocalistModel, newItem: VocalistModel): Boolean {
        return oldItem == newItem
    }
}