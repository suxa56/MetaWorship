package uz.suxa.metaworship.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.suxa.metaworship.databinding.BottomSheetInputBinding

class InputBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetInputBinding? = null
    private val binding get() = _binding!!

    var onSave: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.createVocalistSubmit.setOnClickListener {
            onSave?.invoke(binding.InputTIL.editText?.text.toString())
        }
    }

    fun customizeView(label: Int, inputHint: Int) {
        binding.inputLabel.text = getString(label)
        binding.InputTIL.hint = getString(inputHint)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "InputBottomSheet"
    }
}