package uz.suxa.metaworship.presentation.adapter.vocalist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import uz.suxa.metaworship.databinding.CardVocalistBinding
import uz.suxa.metaworship.domain.model.VocalistModel

class VocalistAdapter :
    ListAdapter<VocalistModel, VocalistViewHolder>(
        VocalistDiffUtil
    ) {

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocalistViewHolder {
        val binding = CardVocalistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VocalistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VocalistViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            vocalistIcon.text = item.name[0].toString()
            vocalistTitle.text = item.name
            root.setOnClickListener {
                onItemClick?.invoke(item.name)
            }
        }
    }
}