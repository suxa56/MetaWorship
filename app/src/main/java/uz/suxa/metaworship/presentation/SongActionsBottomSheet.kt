package uz.suxa.metaworship.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.suxa.metaworship.databinding.BottomSheetSongActionsBinding
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.presentation.fragment.HomeFragmentDirections

class SongActionsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSongActionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var song: SongModel

    var onSongCopy: ((Tonality) -> Unit)? = null
    var onSongCopyIn: ((Tonality) -> Unit)? = null
    var onSongCopyLyrics: (() -> Unit)? = null
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
        setupClickListener()
    }

    fun setSong(song: SongModel) {
        this.song = song
    }

    private fun setupClickListener() {
        binding.songActionEdit.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNewSongFragment(
                    song.id
                )
            )
        }

        binding.songActionCopy.setOnClickListener {
            val bottomSheet = SongCopyInBottomSheet()
            bottomSheet.show(
                childFragmentManager,
                SongCopyInBottomSheet.TAG
            )
            bottomSheet.onTonalityClick = {
                onSongCopy?.invoke(it)
            }
        }

        binding.songActionCopyIn.setOnClickListener {
            val bottomSheet = SongCopyInBottomSheet()
            bottomSheet.show(
                childFragmentManager,
                SongCopyInBottomSheet.TAG
            )
            bottomSheet.onTonalityClick = {
                onSongCopyIn?.invoke(it)
            }
        }

        binding.songActionCopyLyrics.setOnClickListener {
            onSongCopyLyrics?.invoke()
        }

        binding.songActionDelete.setOnClickListener {
            onSongDelete?.invoke()
        }
    }

    companion object {
        const val TAG = "songActionsBottomSheet"
    }
}