package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.suxa.metaworship.databinding.BottomSheetInputBinding

class InputBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetInputBinding? = null
    private val binding get() = _binding!!

    private var vocalist: String = ""

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
        if (vocalist.isNotBlank()) {
            (binding.InputTIL.editText as TextView).text = vocalist
        }
    }

    fun getVocalist(vocalist: String) {
        this.vocalist = vocalist
    }

    private fun setupClickListener() {
        binding.createVocalistSubmit.setOnClickListener {
            onSave?.invoke(binding.InputTIL.editText?.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "InputBottomSheet"
    }
}