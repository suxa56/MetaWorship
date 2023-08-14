package uz.suxa.metaworship.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.suxa.metaworship.databinding.BottomSheetSongActionsBinding
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality

class SongActionsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSongActionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var song: SongModel

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
        binding.songActionsTitle.text = song.title
        setupActions()
        setupClickListener()
    }

    fun setSong(song: SongModel) {
        this.song = song
    }

    private fun setupActions() {
        if (song.defaultTonality == Tonality.UNDEFINED) {
            binding.songActionCopyIn.visibility = View.GONE
            binding.songActionCopy.visibility = View.GONE
        }
        if (song.lyrics.isBlank()) {
            binding.songActionCopy.visibility = View.GONE
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
            bottomSheet.setSongId(song.id)
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