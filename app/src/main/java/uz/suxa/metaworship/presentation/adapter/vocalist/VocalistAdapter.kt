package uz.suxa.metaworship.presentation.adapter.vocalist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import uz.suxa.metaworship.databinding.CardVocalistBinding
import uz.suxa.metaworship.domain.dto.VocalistSongDto

class VocalistAdapter(private val context: Context) :
    ListAdapter<VocalistSongDto, VocalistViewHolder>(
        VocalistDiffUtil
    ) {

    var onItemClickListener: ((String) -> Unit)? = null

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
            vocalistSongCount.text = item.songsCount.toString()
        }
    }
}