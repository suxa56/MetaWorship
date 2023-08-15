package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.suxa.metaworship.databinding.BottomSheetTonalitiesBinding
import uz.suxa.metaworship.domain.model.Tonality

class TonalityBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetTonalitiesBinding? = null
    private val binding get() = _binding!!

    var onTonalityClick: ((Tonality) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTonalitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupClickListener()
    }

    private fun setupClickListener() {
        with(binding) {
            tonalityC.setOnClickListener {
                onTonalityClick?.invoke(Tonality.C)
            }
            tonalityCSharp.setOnClickListener {
                onTonalityClick?.invoke(Tonality.C_SHARP)
            }
            tonalityD.setOnClickListener {
                onTonalityClick?.invoke(Tonality.D)
            }
            tonalityEFlat.setOnClickListener {
                onTonalityClick?.invoke(Tonality.E_FLAT)
            }
            tonalityE.setOnClickListener {
                onTonalityClick?.invoke(Tonality.E)
            }
            tonalityF.setOnClickListener {
                onTonalityClick?.invoke(Tonality.F)
            }
            tonalityFSharp.setOnClickListener {
                onTonalityClick?.invoke(Tonality.F_SHARP)
            }
            tonalityG.setOnClickListener {
                onTonalityClick?.invoke(Tonality.G)
            }
            tonalityAFlat.setOnClickListener {
                onTonalityClick?.invoke(Tonality.A_FLAT)
            }
            tonalityA.setOnClickListener {
                onTonalityClick?.invoke(Tonality.A)
            }
            tonalityHFlat.setOnClickListener {
                onTonalityClick?.invoke(Tonality.H_FLAT)
            }
            tonalityH.setOnClickListener {
                onTonalityClick?.invoke(Tonality.H)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "songCopyInBottomSheet"
    }
}