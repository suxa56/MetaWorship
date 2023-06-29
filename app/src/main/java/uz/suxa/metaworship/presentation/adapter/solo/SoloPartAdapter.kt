package uz.suxa.metaworship.presentation.adapter.solo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import uz.suxa.metaworship.databinding.CardSoloPartBinding
import uz.suxa.metaworship.domain.model.SoloPart

class SoloPartAdapter : ListAdapter<SoloPart, SoloPartViewHolder>(SoloPartDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoloPartViewHolder {
        val binding = CardSoloPartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SoloPartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SoloPartViewHolder, position: Int) {
        val soloPart = getItem(position)
        holder.binding.soloPartLabel.text = soloPart.part
        holder.binding.solo.text = soloPart.solo
    }
}