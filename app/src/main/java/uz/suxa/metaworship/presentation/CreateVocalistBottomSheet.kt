package uz.suxa.metaworship.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.suxa.metaworship.databinding.BottomSheetCreateVocalistBinding
import uz.suxa.metaworship.presentation.viewmodel.VocalistViewModel

class CreateVocalistBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCreateVocalistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[VocalistViewModel::class.java]
    }

    var onSave: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCreateVocalistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.createVocalistSubmit.setOnClickListener {
            if (viewModel.createVocalist(binding.createVocalistTIL.editText?.text.toString())) {
                onSave?.invoke()
            }
            dismiss()
        }

        binding.createVocalistTIL.editText?.addTextChangedListener {
            binding.createVocalistTIL.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CreateVocalistBottomSheet"
    }
}