package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.suxa.metaworship.databinding.BottomSheetAddToCompositionBinding
import uz.suxa.metaworship.presentation.viewmodel.CompositionViewModel

class CompositionBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddToCompositionBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[CompositionViewModel::class.java]
    }

    private lateinit var songId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddToCompositionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()
    }

    fun setSongId(songId: String) {
        this.songId = songId
    }

    private fun setupClickListener() {
        binding.createNewComposition.setOnClickListener {
            val bottomSheet = InputBottomSheet()
//            bottomSheet.customizeView(R.string.new_composition, R.string.new_composition_hint)
            bottomSheet.show(
                childFragmentManager,
                InputBottomSheet.TAG
            )

            bottomSheet.onSave = { label ->
                val tonalityBottomSheet = TonalityBottomSheet()
                tonalityBottomSheet.show(
                    childFragmentManager,
                    TonalityBottomSheet.TAG
                )
                tonalityBottomSheet.onTonalityClick = {
                    viewModel.addComposition(
                        label,
                        songId,
                        it.toString()
                            .replace("_SHARP", "#")
                            .replace("_FLAT", "b")
                    )
                }
                tonalityBottomSheet.dismiss()
            }
            bottomSheet.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "compositionBottomSheet"
    }
}