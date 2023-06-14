package uz.suxa.metaworship.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.suxa.metaworship.databinding.BottomSheetCreateVocalistBinding

class CreateVocalistBottomSheet: BottomSheetDialogFragment() {

    private var _binding: BottomSheetCreateVocalistBinding? = null
    private val binding get() = _binding!!

    // TODO(): create view model, use case, repo to add vocalist to db

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCreateVocalistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CreateVocalistBottomSheet"
    }
}