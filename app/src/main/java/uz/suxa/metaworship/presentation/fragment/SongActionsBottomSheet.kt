package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.suxa.metaworship.databinding.BottomSheetSongActionsBinding
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.model.VocalistModel

class SongActionsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSongActionsBinding? = null
    private val binding get() = _binding!!
    private var song: SongModel? = null
    private var vocalist: VocalistModel? = null

    var onSongEdit: (() -> Unit)? = null
    var onSongCopy: ((Tonality) -> Unit)? = null
    var onSongCopyIn: ((Tonality) -> Unit)? = null
    var onSongCopyLyrics: (() -> Unit)? = null
    var onSongAddToComposition: (() -> Unit)? = null
    var onSongDelete: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSongActionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActions()
        setupClickListener()
    }

    fun setSong(song: SongModel) {
        this.song = song
    }

    fun setVocalist(vocalist: VocalistModel) {
        this.vocalist = vocalist
    }

    private fun setupActions() {
        if (song != null) {
            binding.songActionsTitle.text = song!!.title
            if (song!!.defaultTonality == Tonality.UNDEFINED) {
                binding.songActionCopyIn.visibility = View.GONE
                binding.songActionCopy.visibility = View.GONE
            }
            if (song!!.lyrics.isBlank()) {
                binding.songActionCopy.visibility = View.GONE
                binding.songActionCopyLyrics.visibility = View.GONE
            }
        } else if (vocalist != null) {
            binding.songActionsTitle.text = vocalist!!.name
            binding.songActionCopy.visibility = View.GONE
            binding.songActionCopyIn.visibility = View.GONE
            binding.songActionCopyLyrics.visibility = View.GONE
        }
    }

    private fun setupClickListener() {
        binding.songActionEdit.setOnClickListener {
            onSongEdit?.invoke()
        }

        binding.songActionCopy.setOnClickListener {
            val bottomSheet = TonalityBottomSheet()
            bottomSheet.show(
                childFragmentManager,
                TonalityBottomSheet.TAG
            )
            bottomSheet.onTonalityClick = {
                onSongCopy?.invoke(it)
            }
        }

        binding.songActionCopyIn.setOnClickListener {
            val bottomSheet = TonalityBottomSheet()
            bottomSheet.show(
                childFragmentManager,
                TonalityBottomSheet.TAG
            )
            bottomSheet.onTonalityClick = {
                onSongCopyIn?.invoke(it)
                bottomSheet.dismiss()
            }
        }

        binding.songActionCopyLyrics.setOnClickListener {
            onSongCopyLyrics?.invoke()
        }

        binding.songActionAddToComposition.setOnClickListener {
            onSongAddToComposition?.invoke()
            val bottomSheet = CompositionBottomSheet()
            bottomSheet.setSongId(song!!.id)
            bottomSheet.show(
                childFragmentManager,
                CompositionBottomSheet.TAG
            )
        }

        binding.songActionDelete.setOnClickListener {
            onSongDelete?.invoke()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "songActionsBottomSheet"
    }
}